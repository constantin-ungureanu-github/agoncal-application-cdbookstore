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

import org.agoncal.application.cdbookstore.model.Category;
import org.agoncal.application.cdbookstore.util.Loggable;

/**
 * Backing bean for Category entities.
 * <p/>
 * This class provides CRUD functionality for all Category entities. It focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for state
 * management, <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or custom base
 * class.
 */

@Named
@Stateful
@ConversationScoped
@Loggable
public class CategoryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /*
     * Support creating and retrieving Category entities
     */

    private Long id;
    private Category category;
    @Inject
    private Conversation conversation;
    @PersistenceContext(unitName = "applicationCDBookStorePU", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;
    private int page;
    private long count;
    private List<Category> pageItems;
    private Category example = new Category();
    @Resource
    private SessionContext sessionContext;
    private Category add = new Category();

    public Long getId() {
        return id;
    }

    /*
     * Support updating and deleting Category entities
     */

    public void setId(final Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    /*
     * Support searching Category entities with pagination
     */

    public void setCategory(final Category category) {
        this.category = category;
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
            category = example;
        } else {
            category = findById(getId());
        }
    }

    public Category findById(final Long id) {

        return entityManager.find(Category.class, id);
    }

    public String update() {
        conversation.end();

        try {
            if (id == null) {
                entityManager.persist(category);
                return "search?faces-redirect=true";
            } else {
                entityManager.merge(category);
                return "view?faces-redirect=true&id=" + category.getId();
            }
        } catch (final Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public String delete() {
        conversation.end();

        try {
            final Category deletableEntity = findById(getId());

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

    public Category getExample() {
        return example;
    }

    public void setExample(final Category example) {
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
        Root<Category> root = countCriteria.from(Category.class);
        countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root));
        count = entityManager.createQuery(countCriteria).getSingleResult();

        // Populate this.pageItems

        final CriteriaQuery<Category> criteria = builder.createQuery(Category.class);
        root = criteria.from(Category.class);
        final TypedQuery<Category> query = entityManager.createQuery(criteria.select(root).where(getSearchPredicates(root)));
        query.setFirstResult(page * getPageSize()).setMaxResults(getPageSize());
        pageItems = query.getResultList();
    }

    private Predicate[] getSearchPredicates(final Root<Category> root) {

        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final List<Predicate> predicatesList = new ArrayList<>();

        final String name = example.getName();
        if ((name != null) && !"".equals(name)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("name")), '%' + name.toLowerCase() + '%'));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }

    /*
     * Support listing and POSTing back Category entities (e.g. from inside an HtmlSelectOneMenu)
     */

    public List<Category> getPageItems() {
        return pageItems;
    }

    public long getCount() {
        return count;
    }

    public List<Category> getAll() {

        final CriteriaQuery<Category> criteria = entityManager.getCriteriaBuilder().createQuery(Category.class);
        return entityManager.createQuery(criteria.select(criteria.from(Category.class))).getResultList();
    }

    /*
     * Support adding children to bidirectional, one-to-many tables
     */

    public Converter getConverter() {

        final CategoryBean ejbProxy = sessionContext.getBusinessObject(CategoryBean.class);

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

                return String.valueOf(((Category) value).getId());
            }
        };
    }

    public Category getAdd() {
        return add;
    }

    public Category getAdded() {
        final Category added = add;
        add = new Category();
        return added;
    }
}
