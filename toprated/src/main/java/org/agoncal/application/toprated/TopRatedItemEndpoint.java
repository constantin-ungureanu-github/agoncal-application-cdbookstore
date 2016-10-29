package org.agoncal.application.toprated;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;

@Path("/toprateditems")
@Transactional
public class TopRatedItemEndpoint {

    @Inject
    private EntityManager entityManager;

    @Inject
    private Logger logger;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public RatedItems TopRatedItems() {
        final RatedItems results = new RatedItems();
        final TypedQuery<RatedItem> query = entityManager.createNamedQuery(RatedItem.FIND_TOP_ITEMS, RatedItem.class);
        results.addAll(query.getResultList());

        logger.info("Top Items are {}", results);

        return results;
    }
}
