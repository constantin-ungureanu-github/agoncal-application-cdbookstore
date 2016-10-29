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

import org.agoncal.application.cdbookstore.model.Publisher;
import org.agoncal.application.cdbookstore.util.Loggable;

/**
 * Backing bean for Publisher entities.
 * <p/>
 * This class provides CRUD functionality for all Publisher entities. It focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for state
 * management, <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or custom base
 * class.
 */

@Named
@Stateful
@ConversationScoped
@Loggable
public class PublisherBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Publisher publisher;
    @Inject
    private Conversation conversation;
    @PersistenceContext(unitName = "applicationCDBookStorePU", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;
    private int page;
    private long count;
    private List<Publisher> pageItems;
    private Publisher example = new Publisher();
    @Resource
    private SessionContext sessionContext;
    private Publisher add = new Publisher();

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(final Publisher publisher) {
        this.publisher = publisher;
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
            publisher = example;
        } else {
            publisher = findById(getId());
        }
    }

    public Publisher findById(final Long id) {
        return entityManager.find(Publisher.class, id);
    }

    public String update() {
        conversation.end();

        try {
            if (id == null) {
                entityManager.persist(publisher);
                return "search?faces-redirect=true";
            } else {
                entityManager.merge(publisher);
                return "view?faces-redirect=true&id=" + publisher.getId();
            }
        } catch (final Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public String delete() {
        conversation.end();

        try {
            final Publisher deletableEntity = findById(getId());

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

    public Publisher getExample() {
        return example;
    }

    public void setExample(final Publisher example) {
        this.example = example;
    }

    public String search() {
        page = 0;
        return null;
    }

    public void paginate() {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        // Populate this.count

        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        Root<Publisher> root = countCriteria.from(Publisher.class);
        countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root));
        count = entityManager.createQuery(countCriteria).getSingleResult();

        // Populate this.pageItems

        final CriteriaQuery<Publisher> criteria = builder.createQuery(Publisher.class);
        root = criteria.from(Publisher.class);
        final TypedQuery<Publisher> query = entityManager.createQuery(criteria.select(root).where(getSearchPredicates(root)));
        query.setFirstResult(page * getPageSize()).setMaxResults(getPageSize());
        pageItems = query.getResultList();
    }

    private Predicate[] getSearchPredicates(final Root<Publisher> root) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final List<Predicate> predicatesList = new ArrayList<>();

        final String name = example.getName();
        if ((name != null) && !"".equals(name)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("name")), '%' + name.toLowerCase() + '%'));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }

    public List<Publisher> getPageItems() {
        return pageItems;
    }

    public long getCount() {
        return count;
    }

    public List<Publisher> getAll() {

        final CriteriaQuery<Publisher> criteria = entityManager.getCriteriaBuilder().createQuery(Publisher.class);
        return entityManager.createQuery(criteria.select(criteria.from(Publisher.class))).getResultList();
    }

    public Converter getConverter() {
        final PublisherBean ejbProxy = sessionContext.getBusinessObject(PublisherBean.class);

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

                return String.valueOf(((Publisher) value).getId());
            }
        };
    }

    public Publisher getAdd() {
        return add;
    }

    public Publisher getAdded() {
        final Publisher added = add;
        add = new Publisher();
        return added;
    }
}
