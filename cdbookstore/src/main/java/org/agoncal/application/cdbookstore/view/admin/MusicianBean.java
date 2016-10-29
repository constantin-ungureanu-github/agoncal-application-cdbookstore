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

import org.agoncal.application.cdbookstore.model.Musician;
import org.agoncal.application.cdbookstore.util.Loggable;

/**
 * Backing bean for Musician entities.
 * <p/>
 * This class provides CRUD functionality for all Musician entities. It focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for state
 * management, <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or custom base
 * class.
 */

@Named
@Stateful
@ConversationScoped
@Loggable
public class MusicianBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Musician musician;
    @Inject
    private Conversation conversation;
    @PersistenceContext(unitName = "applicationCDBookStorePU", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;
    private int page;
    private long count;
    private List<Musician> pageItems;
    private Musician example = new Musician();
    @Resource
    private SessionContext sessionContext;
    private Musician add = new Musician();

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Musician getMusician() {
        return musician;
    }

    public void setMusician(final Musician musician) {
        this.musician = musician;
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
            musician = example;
        } else {
            musician = findById(getId());
        }
    }

    public Musician findById(final Long id) {

        return entityManager.find(Musician.class, id);
    }

    public String update() {
        conversation.end();

        try {
            if (id == null) {
                entityManager.persist(musician);
                return "search?faces-redirect=true";
            } else {
                entityManager.merge(musician);
                return "view?faces-redirect=true&id=" + musician.getId();
            }
        } catch (final Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public String delete() {
        conversation.end();

        try {
            final Musician deletableEntity = findById(getId());

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

    public Musician getExample() {
        return example;
    }

    public void setExample(final Musician example) {
        this.example = example;
    }

    public String search() {
        page = 0;
        return null;
    }

    public void paginate() {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        Root<Musician> root = countCriteria.from(Musician.class);
        countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root));
        count = entityManager.createQuery(countCriteria).getSingleResult();

        final CriteriaQuery<Musician> criteria = builder.createQuery(Musician.class);
        root = criteria.from(Musician.class);
        final TypedQuery<Musician> query = entityManager.createQuery(criteria.select(root).where(getSearchPredicates(root)));
        query.setFirstResult(page * getPageSize()).setMaxResults(getPageSize());
        pageItems = query.getResultList();
    }

    private Predicate[] getSearchPredicates(final Root<Musician> root) {

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
        final String preferredInstrument = example.getPreferredInstrument();
        if ((preferredInstrument != null) && !"".equals(preferredInstrument)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("preferredInstrument")), '%' + preferredInstrument.toLowerCase() + '%'));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }

    /*
     * Support listing and POSTing back Musician entities (e.g. from inside an HtmlSelectOneMenu)
     */

    public List<Musician> getPageItems() {
        return pageItems;
    }

    public long getCount() {
        return count;
    }

    public List<Musician> getAll() {

        final CriteriaQuery<Musician> criteria = entityManager.getCriteriaBuilder().createQuery(Musician.class);
        return entityManager.createQuery(criteria.select(criteria.from(Musician.class))).getResultList();
    }

    /*
     * Support adding children to bidirectional, one-to-many tables
     */

    public Converter getConverter() {

        final MusicianBean ejbProxy = sessionContext.getBusinessObject(MusicianBean.class);

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

                return String.valueOf(((Musician) value).getId());
            }
        };
    }

    public Musician getAdd() {
        return add;
    }

    public Musician getAdded() {
        final Musician added = add;
        add = new Musician();
        return added;
    }
}
