package org.agoncal.application.cdbookstore.util;

import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JPASchemaGen {
    private static final Logger logger = LoggerFactory.getLogger(JPASchemaGen.class.getName());
    private static String PERSISTENCE_UNIT_NAME = "applicationCDBookStoreGenPU";

    public static void main(final String[] args) {
        Persistence.generateSchema(PERSISTENCE_UNIT_NAME, null);

        logger.info("DDL have been generated");
    }
}
