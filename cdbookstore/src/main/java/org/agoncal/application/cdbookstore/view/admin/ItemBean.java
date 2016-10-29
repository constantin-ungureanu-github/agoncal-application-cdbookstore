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

import org.agoncal.application.cdbookstore.model.Item;
import org.agoncal.application.cdbookstore.util.Loggable;

/**
 * Backing bean for Item entities.
 * <p/>
 * This class provides CRUD functionality for all Item entities. It focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for state
 * management, <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or custom base
 * class.
 */

@Named
@Stateful
@ConversationScoped
@Loggable
public class ItemBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /*
     * Support creating and retrieving Item entities
     */

    private Long id;
    private Item item;
    @Inject
    private Conversation conversation;
    @PersistenceContext(unitName = "applicationCDBookStorePU", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;
    private int page;
    private long count;
    private List<Item> pageItems;
    private Item example = new Item();
    @Resource
    private SessionContext sessionContext;
    private Item add = new Item();

    public Long getId() {
        return id;
    }

    /*
     * Support updating and deleting Item entities
     */

    public void setId(final Long id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    /*
     * Support searching Item entities with pagination
     */

    public void setItem(final Item item) {
        this.item = item;
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
            item = example;
        } else {
            item = findById(getId());
        }
    }

    public Item findById(final Long id) {

        return entityManager.find(Item.class, id);
    }

    public String update() {
        conversation.end();

        try {
            if (id == null) {
                entityManager.persist(item);
                return "search?faces-redirect=true";
            } else {
                entityManager.merge(item);
                return "view?faces-redirect=true&id=" + item.getId();
            }
        } catch (final Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public String delete() {
        conversation.end();

        try {
            final Item deletableEntity = findById(getId());

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

    public Item getExample() {
        return example;
    }

    public void setExample(final Item example) {
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
        Root<Item> root = countCriteria.from(Item.class);
        countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root));
        count = entityManager.createQuery(countCriteria).getSingleResult();

        // Populate this.pageItems

        final CriteriaQuery<Item> criteria = builder.createQuery(Item.class);
        root = criteria.from(Item.class);
        final TypedQuery<Item> query = entityManager.createQuery(criteria.select(root).where(getSearchPredicates(root)));
        query.setFirstResult(page * getPageSize()).setMaxResults(getPageSize());
        pageItems = query.getResultList();
    }

    private Predicate[] getSearchPredicates(final Root<Item> root) {

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

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }

    /*
     * Support listing and POSTing back Item entities (e.g. from inside an HtmlSelectOneMenu)
     */

    public List<Item> getPageItems() {
        return pageItems;
    }

    public long getCount() {
        return count;
    }

    public List<Item> getAll() {

        final CriteriaQuery<Item> criteria = entityManager.getCriteriaBuilder().createQuery(Item.class);
        return entityManager.createQuery(criteria.select(criteria.from(Item.class))).getResultList();
    }

    /*
     * Support adding children to bidirectional, one-to-many tables
     */

    public Converter getConverter() {

        final ItemBean ejbProxy = sessionContext.getBusinessObject(ItemBean.class);

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

                return String.valueOf(((Item) value).getId());
            }
        };
    }

    public Item getAdd() {
        return add;
    }

    public Item getAdded() {
        final Item added = add;
        add = new Item();
        return added;
    }
}
