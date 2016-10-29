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

import org.agoncal.application.cdbookstore.model.Book;
import org.agoncal.application.cdbookstore.util.Language;
import org.agoncal.application.cdbookstore.util.Loggable;
import org.agoncal.application.cdbookstore.util.NumberGenerator;
import org.agoncal.application.cdbookstore.util.ThirteenDigits;

/**
 * Backing bean for Book entities.
 * <p/>
 * This class provides CRUD functionality for all Book entities. It focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for state
 * management, <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or custom base
 * class.
 */

@Named
@Stateful
@ConversationScoped
@Loggable
public class BookBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Book book;
    @Inject
    private Conversation conversation;
    @PersistenceContext(unitName = "applicationCDBookStorePU", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;
    private int page;
    private long count;
    private List<Book> pageItems;
    private Book example = new Book();
    @Resource
    private SessionContext sessionContext;
    private Book add = new Book();
    @Inject
    @ThirteenDigits
    private NumberGenerator generator;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(final Book book) {
        this.book = book;
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
            book = example;
        } else {
            book = findById(getId());
        }
    }

    public Book findById(final Long id) {
        return entityManager.find(Book.class, id);
    }

    public String update() {
        conversation.end();

        try {
            if (id == null) {
                book.setIsbn(generator.generateNumber());
                entityManager.persist(book);
                return "search?faces-redirect=true";
            } else {
                entityManager.merge(book);
                return "view?faces-redirect=true&id=" + book.getId();
            }
        } catch (final Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public String delete() {
        conversation.end();

        try {
            final Book deletableEntity = findById(getId());

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

    public Book getExample() {
        return example;
    }

    public void setExample(final Book example) {
        this.example = example;
    }

    public String search() {
        page = 0;
        return null;
    }

    public void paginate() {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        Root<Book> root = countCriteria.from(Book.class);
        countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root));
        count = entityManager.createQuery(countCriteria).getSingleResult();

        final CriteriaQuery<Book> criteria = builder.createQuery(Book.class);
        root = criteria.from(Book.class);
        final TypedQuery<Book> query = entityManager.createQuery(criteria.select(root).where(getSearchPredicates(root)));
        query.setFirstResult(page * getPageSize()).setMaxResults(getPageSize());
        pageItems = query.getResultList();
    }

    private Predicate[] getSearchPredicates(final Root<Book> root) {
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

        final String isbn = example.getIsbn();
        if ((isbn != null) && !"".equals(isbn)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("isbn")), '%' + isbn.toLowerCase() + '%'));
        }

        final Integer nbOfPage = example.getNbOfPage();
        if ((nbOfPage != null) && (nbOfPage.intValue() != 0)) {
            predicatesList.add(builder.equal(root.get("nbOfPage"), nbOfPage));
        }

        final Language language = example.getLanguage();
        if (language != null) {
            predicatesList.add(builder.equal(root.get("language"), language));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }

    public List<Book> getPageItems() {
        return pageItems;
    }

    public long getCount() {
        return count;
    }

    public List<Book> getAll() {
        final CriteriaQuery<Book> criteria = entityManager.getCriteriaBuilder().createQuery(Book.class);
        return entityManager.createQuery(criteria.select(criteria.from(Book.class))).getResultList();
    }

    public Converter getConverter() {
        final BookBean ejbProxy = sessionContext.getBusinessObject(BookBean.class);

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

                return String.valueOf(((Book) value).getId());
            }
        };
    }

    public Book getAdd() {
        return add;
    }

    public Book getAdded() {
        final Book added = add;
        add = new Book();
        return added;
    }
}
