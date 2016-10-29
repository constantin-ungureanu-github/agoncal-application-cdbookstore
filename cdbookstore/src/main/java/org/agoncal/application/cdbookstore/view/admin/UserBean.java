package org.agoncal.application.cdbookstore.view.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.agoncal.application.cdbookstore.model.User;
import org.agoncal.application.cdbookstore.util.Loggable;

/**
 * Backing bean for User entities.
 * <p/>
 * This class provides CRUD functionality for all User entities. It focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for state
 * management, <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or custom base
 * class.
 */

@Named
@Stateful
@ConversationScoped
@Loggable
public class UserBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private User user;

    @Inject
    private Conversation conversation;

    @PersistenceContext(unitName = "applicationCDBookStorePU", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;
    private int page;
    private long count;
    private List<User> pageItems;
    private User example = new User();

    @Resource
    private SessionContext sessionContext;
    private User add = new User();

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public String create() {
        conversation.begin();
        conversation.setTimeout(1800000L);
        return "create?faces-redirect=true";
    }

    public void retrieve() {
        if (FacesContext.getCurrentInstance().isPostback()) {
            return;
        }

        if (conversation.isTransient()) {
            conversation.begin();
            conversation.setTimeout(1800000L);
        }

        if (id == null) {
            user = example;
        } else {
            user = findById(getId());
        }
    }

    public User findById(final Long id) {
        return entityManager.find(User.class, id);
    }

    public String update() {
        conversation.end();

        try {
            if (id == null) {
                entityManager.persist(user);
                return "search?faces-redirect=true";
            } else {
                entityManager.merge(user);
                return "view?faces-redirect=true&id=" + user.getId();
            }
        } catch (final Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public String delete() {
        conversation.end();

        try {
            final User deletableEntity = findById(getId());

            entityManager.remove(deletableEntity);
            entityManager.flush();
            return "search?faces-redirect=true";
        } catch (final Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public int getPage() {
        return page;
    }

    public void setPage(final int page) {
        this.page = page;
    }

    public int getPageSize() {
        return 10;
    }

    public User getExample() {
        return example;
    }

    public void setExample(final User example) {
        this.example = example;
    }

    public String search() {
        page = 0;
        return null;
    }

    public void paginate() {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        Root<User> root = countCriteria.from(User.class);
        countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root));
        count = entityManager.createQuery(countCriteria).getSingleResult();

        final CriteriaQuery<User> criteria = builder.createQuery(User.class);
        root = criteria.from(User.class);
        final TypedQuery<User> query = entityManager.createQuery(criteria.select(root).where(getSearchPredicates(root)));
        query.setFirstResult(page * getPageSize()).setMaxResults(getPageSize());
        pageItems = query.getResultList();
    }

    private Predicate[] getSearchPredicates(final Root<User> root) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final List<Predicate> predicatesList = new ArrayList<>();

        final String firstName = example.getFirstName();
        if ((firstName != null) && !"".equals(firstName)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("firstName")), '%' + firstName.toLowerCase() + '%'));
        }
        final String lastName = example.getLastName();
        if ((lastName != null) && !"".equals(lastName)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("lastName")), '%' + lastName.toLowerCase() + '%'));
        }
        final String telephone = example.getTelephone();
        if ((telephone != null) && !"".equals(telephone)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("telephone")), '%' + telephone.toLowerCase() + '%'));
        }
        final String email = example.getEmail();
        if ((email != null) && !"".equals(email)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("email")), '%' + email.toLowerCase() + '%'));
        }
        final String login = example.getLogin();
        if ((login != null) && !"".equals(login)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("login")), '%' + login.toLowerCase() + '%'));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }

    public List<User> getPageItems() {
        return pageItems;
    }

    public long getCount() {
        return count;
    }

    public List<User> getAll() {

        final CriteriaQuery<User> criteria = entityManager.getCriteriaBuilder().createQuery(User.class);
        return entityManager.createQuery(criteria.select(criteria.from(User.class))).getResultList();
    }

    public Converter getConverter() {
        final UserBean ejbProxy = sessionContext.getBusinessObject(UserBean.class);

        return new Converter() {
            @Override
            public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
                return ejbProxy.findById(Long.valueOf(value));
            }

            @Override
            public String getAsString(final FacesContext context, final UIComponent component, final Object value) {
                if (value == null) {
                    return "";
                }

                return String.valueOf(((User) value).getId());
            }
        };
    }

    public User getAdd() {
        return add;
    }

    public User getAdded() {
        final User added = add;
        add = new User();
        return added;
    }
}
