package org.agoncal.application.cdbookstore.view.shopping;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.agoncal.application.cdbookstore.model.Item;
import org.agoncal.application.cdbookstore.util.Auditable;
import org.slf4j.Logger;

import lombok.Getter;
import lombok.Setter;

@Named
@RequestScoped
@Transactional
public class RatedItemsBean {

    @Inject
    private FacesContext facesContext;

    @Inject
    private Logger logger;

    @Inject
    private EntityManager entityManager;

    @Getter
    @Setter
    private List<Item> topRatedItems;
    private final Set<Item> randomItems = new HashSet<>();

    @PostConstruct
    private void init() {
        doFindTopRated();
        doFindRandomThree();

    }

    @Auditable
    private void doFindRandomThree() {
        final int min = entityManager.createQuery("select min (i.id) from Item i", Long.class).getSingleResult().intValue();
        final int max = entityManager.createQuery("select max (i.id) from Item i", Long.class).getSingleResult().intValue();

        while (randomItems.size() < 3) {
            final long id = new Random().nextInt((max - min) + 1) + min;
            final Item item = entityManager.find(Item.class, id);
            if (item != null) {
                randomItems.add(item);
            }
        }
    }

    @Auditable
    private void doFindTopRated() {
        Response response;

        try {
            response = ClientBuilder.newClient().target("http://localhost:8080/applicationToprated/toprateditems").request(MediaType.APPLICATION_JSON).get();
        } catch (final Exception e) {
            response = ClientBuilder.newClient().target("http://localhost:8085/applicationToprated/toprateditems").request(MediaType.APPLICATION_JSON).get();
        }

        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            return;
        }

        final String body = response.readEntity(String.class);

        final List<Long> topRateditemIds = new ArrayList<>();
        try (JsonReader reader = Json.createReader(new StringReader(body))) {
            final JsonArray array = reader.readArray();
            for (int i = 0; i < array.size(); i++) {
                topRateditemIds.add((long) array.getJsonObject(i).getInt("id"));
            }
        }

        if (!topRateditemIds.isEmpty()) {
            logger.info("Top rated books ids {}", topRateditemIds);
            final TypedQuery<Item> query = entityManager.createNamedQuery(Item.FIND_TOP_RATED, Item.class);
            query.setParameter("ids", topRateditemIds);
            topRatedItems = query.getResultList();
            logger.info("Number of top rated items found {}", topRatedItems.size());
        }
    }

    public List<Item> getRandomItems() {
        return new ArrayList<>(randomItems);
    }
}
