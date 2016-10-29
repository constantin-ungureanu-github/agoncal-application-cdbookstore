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

import org.agoncal.application.cdbookstore.model.Genre;
import org.agoncal.application.cdbookstore.util.Loggable;

/**
 * Backing bean for Genre entities.
 * <p/>
 * This class provides CRUD functionality for all Genre entities. It focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for state
 * management, <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or custom base
 * class.
 */

@Named
@Stateful
@ConversationScoped
@Loggable
public class GenreBean implements Serializable {
    private static final long serialVersionUID = 8606214696220974612L;
    private Long id;
    private Genre genre;
    @Inject
    private Conversation conversation;
    @PersistenceContext(unitName = "applicationCDBookStorePU", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;
    private int page;
    private long count;
    private List<Genre> pageItems;
    private Genre example = new Genre();
    @Resource
    private SessionContext sessionContext;
    private Genre add = new Genre();

    public Long getId() {
        return id;
    }

    /*
     * Support updating and deleting Genre entities
     */

    public void setId(final Long id) {
        this.id = id;
    }

    public Genre getGenre() {
        return genre;
    }

    /*
     * Support searching Genre entities with pagination
     */

    public void setGenre(final Genre genre) {
        this.genre = genre;
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
            genre = example;
        } else {
            genre = findById(getId());
        }
    }

    public Genre findById(final Long id) {

        return entityManager.find(Genre.class, id);
    }

    public String update() {
        conversation.end();

        try {
            if (id == null) {
                entityManager.persist(genre);
                return "search?faces-redirect=true";
            } else {
                entityManager.merge(genre);
                return "view?faces-redirect=true&id=" + genre.getId();
            }
        } catch (final Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public String delete() {
        conversation.end();

        try {
            final Genre deletableEntity = findById(getId());

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

    public Genre getExample() {
        return example;
    }

    public void setExample(final Genre example) {
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
        Root<Genre> root = countCriteria.from(Genre.class);
        countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root));
        count = entityManager.createQuery(countCriteria).getSingleResult();

        // Populate this.pageItems

        final CriteriaQuery<Genre> criteria = builder.createQuery(Genre.class);
        root = criteria.from(Genre.class);
        final TypedQuery<Genre> query = entityManager.createQuery(criteria.select(root).where(getSearchPredicates(root)));
        query.setFirstResult(page * getPageSize()).setMaxResults(getPageSize());
        pageItems = query.getResultList();
    }

    private Predicate[] getSearchPredicates(final Root<Genre> root) {

        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final List<Predicate> predicatesList = new ArrayList<>();

        final String name = example.getName();
        if ((name != null) && !"".equals(name)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("name")), '%' + name.toLowerCase() + '%'));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }

    /*
     * Support listing and POSTing back Genre entities (e.g. from inside an HtmlSelectOneMenu)
     */

    public List<Genre> getPageItems() {
        return pageItems;
    }

    public long getCount() {
        return count;
    }

    public List<Genre> getAll() {

        final CriteriaQuery<Genre> criteria = entityManager.getCriteriaBuilder().createQuery(Genre.class);
        return entityManager.createQuery(criteria.select(criteria.from(Genre.class))).getResultList();
    }

    /*
     * Support adding children to bidirectional, one-to-many tables
     */

    public Converter getConverter() {

        final GenreBean ejbProxy = sessionContext.getBusinessObject(GenreBean.class);

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

                return String.valueOf(((Genre) value).getId());
            }
        };
    }

    public Genre getAdd() {
        return add;
    }

    public Genre getAdded() {
        final Genre added = add;
        add = new Genre();
        return added;
    }
}
