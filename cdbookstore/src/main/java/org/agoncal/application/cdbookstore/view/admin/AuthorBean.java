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

import org.agoncal.application.cdbookstore.model.Author;
import org.agoncal.application.cdbookstore.util.Language;
import org.agoncal.application.cdbookstore.util.Loggable;

/**
 * Backing bean for Author entities.
 * <p/>
 * This class provides CRUD functionality for all Author entities. It focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for state
 * management, <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or custom base
 * class.
 */

@Named
@Stateful
@ConversationScoped
@Loggable
public class AuthorBean implements Serializable {
    private static final long serialVersionUID = -471294071835338826L;
    private Long id;
    private Author author;
    @Inject
    private Conversation conversation;
    @PersistenceContext(unitName = "applicationCDBookStorePU", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;
    private int page;
    private long count;
    private List<Author> pageItems;
    private Author example = new Author();
    @Resource
    private SessionContext sessionContext;
    private Author add = new Author();

    public Long getId() {
        return id;
    }

    /*
     * Support updating and deleting Author entities
     */

    public void setId(final Long id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(final Author author) {
        this.author = author;
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
            author = example;
        } else {
            author = findById(getId());
        }
    }

    public Author findById(final Long id) {
        return entityManager.find(Author.class, id);
    }

    public String update() {
        conversation.end();

        try {
            if (id == null) {
                entityManager.persist(author);
                return "search?faces-redirect=true";
            } else {
                entityManager.merge(author);
                return "view?faces-redirect=true&id=" + author.getId();
            }
        } catch (final Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public String delete() {
        conversation.end();

        try {
            final Author deletableEntity = findById(getId());

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

    public Author getExample() {
        return example;
    }

    public void setExample(final Author example) {
        this.example = example;
    }

    public String search() {
        page = 0;
        return null;
    }

    public void paginate() {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        Root<Author> root = countCriteria.from(Author.class);
        countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root));
        count = entityManager.createQuery(countCriteria).getSingleResult();

        final CriteriaQuery<Author> criteria = builder.createQuery(Author.class);
        root = criteria.from(Author.class);
        final TypedQuery<Author> query = entityManager.createQuery(criteria.select(root).where(getSearchPredicates(root)));
        query.setFirstResult(page * getPageSize()).setMaxResults(getPageSize());
        pageItems = query.getResultList();
    }

    private Predicate[] getSearchPredicates(final Root<Author> root) {

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
        final String bio = example.getBio();
        if ((bio != null) && !"".equals(bio)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("bio")), '%' + bio.toLowerCase() + '%'));
        }
        final Integer age = example.getAge();
        if ((age != null) && (age.intValue() != 0)) {
            predicatesList.add(builder.equal(root.get("age"), age));
        }
        final Language preferredLanguage = example.getPreferredLanguage();
        if (preferredLanguage != null) {
            predicatesList.add(builder.equal(root.get("preferredLanguage"), preferredLanguage));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }

    /*
     * Support listing and POSTing back Author entities (e.g. from inside an HtmlSelectOneMenu)
     */

    public List<Author> getPageItems() {
        return pageItems;
    }

    public long getCount() {
        return count;
    }

    public List<Author> getAll() {

        final CriteriaQuery<Author> criteria = entityManager.getCriteriaBuilder().createQuery(Author.class);
        return entityManager.createQuery(criteria.select(criteria.from(Author.class))).getResultList();
    }

    public Converter getConverter() {
        final AuthorBean ejbProxy = sessionContext.getBusinessObject(AuthorBean.class);

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

                return String.valueOf(((Author) value).getId());
            }
        };
    }

    public Author getAdd() {
        return add;
    }

    public Author getAdded() {
        final Author added = add;
        add = new Author();
        return added;
    }
}
