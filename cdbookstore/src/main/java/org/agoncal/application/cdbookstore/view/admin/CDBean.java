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

import org.agoncal.application.cdbookstore.model.CD;
import org.agoncal.application.cdbookstore.model.Genre;
import org.agoncal.application.cdbookstore.model.Label;
import org.agoncal.application.cdbookstore.util.Loggable;

/**
 * Backing bean for CD entities.
 * <p/>
 * This class provides CRUD functionality for all CD entities. It focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for state
 * management, <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or custom base
 * class.
 */
@Named("CDBean")
@Stateful
@ConversationScoped
@Loggable
public class CDBean implements Serializable {
    private static final long serialVersionUID = -5675194999757434351L;
    private Long id;
    private CD CD;
    @Inject
    private Conversation conversation;
    @PersistenceContext(unitName = "applicationCDBookStorePU", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;
    private int page;
    private long count;
    private List<CD> pageItems;
    private CD example = new CD();
    @Resource
    private SessionContext sessionContext;
    private CD add = new CD();

    public Long getId() {
        return id;
    }

    /*
     * Support updating and deleting CD entities
     */

    public void setId(final Long id) {
        this.id = id;
    }

    public CD getCD() {
        return CD;
    }

    /*
     * Support searching CD entities with pagination
     */

    public void setCD(final CD CD) {
        this.CD = CD;
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
            CD = example;
        } else {
            CD = findById(getId());
        }
    }

    public CD findById(final Long id) {

        return entityManager.find(CD.class, id);
    }

    public String update() {
        conversation.end();

        try {
            if (id == null) {
                entityManager.persist(CD);
                return "search?faces-redirect=true";
            } else {
                entityManager.merge(CD);
                return "view?faces-redirect=true&id=" + CD.getId();
            }
        } catch (final Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public String delete() {
        conversation.end();

        try {
            final CD deletableEntity = findById(getId());

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

    public CD getExample() {
        return example;
    }

    public void setExample(final CD example) {
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
        Root<CD> root = countCriteria.from(CD.class);
        countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root));
        count = entityManager.createQuery(countCriteria).getSingleResult();

        // Populate this.pageItems

        final CriteriaQuery<CD> criteria = builder.createQuery(CD.class);
        root = criteria.from(CD.class);
        final TypedQuery<CD> query = entityManager.createQuery(criteria.select(root).where(getSearchPredicates(root)));
        query.setFirstResult(page * getPageSize()).setMaxResults(getPageSize());
        pageItems = query.getResultList();
    }

    private Predicate[] getSearchPredicates(final Root<CD> root) {

        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final List<Predicate> predicatesList = new ArrayList<>();

        final String title = example.getTitle();
        if ((title != null) && !"".equals(title)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("title")), '%' + title.toLowerCase() + '%'));
        }
        final String description = example.getDescription();
        if ((description != null) && !"".equals(description)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("description")), '%' + description.toLowerCase() + '%'));
        }
        final Label label = example.getLabel();
        if (label != null) {
            predicatesList.add(builder.equal(root.get("label"), label));
        }
        final Genre genre = example.getGenre();
        if (genre != null) {
            predicatesList.add(builder.equal(root.get("genre"), genre));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }

    /*
     * Support listing and POSTing back CD entities (e.g. from inside an HtmlSelectOneMenu)
     */

    public List<CD> getPageItems() {
        return pageItems;
    }

    public long getCount() {
        return count;
    }

    public List<CD> getAll() {

        final CriteriaQuery<CD> criteria = entityManager.getCriteriaBuilder().createQuery(CD.class);
        return entityManager.createQuery(criteria.select(criteria.from(CD.class))).getResultList();
    }

    /*
     * Support adding children to bidirectional, one-to-many tables
     */

    public Converter getConverter() {

        final CDBean ejbProxy = sessionContext.getBusinessObject(CDBean.class);

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

                return String.valueOf(((CD) value).getId());
            }
        };
    }

    public CD getAdd() {
        return add;
    }

    public CD getAdded() {
        final CD added = add;
        add = new CD();
        return added;
    }
}
