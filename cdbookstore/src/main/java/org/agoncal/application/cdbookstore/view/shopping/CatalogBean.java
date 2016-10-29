package org.agoncal.application.cdbookstore.view.shopping;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.agoncal.application.cdbookstore.model.Item;
import org.agoncal.application.cdbookstore.util.Auditable;

import lombok.Getter;
import lombok.Setter;

@Named
@RequestScoped
@Transactional
public class CatalogBean {
    @Inject
    private FacesContext facesContext;

    @Inject
    private EntityManager entityManager;

    @Getter
    @Setter
    private String keyword;

    @Getter
    @Setter
    private List<Item> items;

    @Getter
    @Setter
    private Item item;

    @Getter
    @Setter
    private Long itemId;

    @Auditable
    public String doSearch() {
        final TypedQuery<Item> typedQuery = entityManager.createNamedQuery(Item.SEARCH, Item.class);
        typedQuery.setParameter("keyword", "%" + keyword.toUpperCase() + "%");
        items = typedQuery.getResultList();
        return null;
    }

    public String doViewItemById() {
        item = entityManager.find(Item.class, itemId);
        return null;
    }
}
