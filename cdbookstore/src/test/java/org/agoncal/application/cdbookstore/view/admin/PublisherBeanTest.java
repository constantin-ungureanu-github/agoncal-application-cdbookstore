package org.agoncal.application.cdbookstore.view.admin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.agoncal.application.cdbookstore.model.Publisher;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class PublisherBeanTest {

    // ======================================
    // = Injection Points =
    // ======================================

    @Inject
    private PublisherBean publisherBean;

    // ======================================
    // = Deployment methods =
    // ======================================

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class).addClass(PublisherBean.class).addClass(Publisher.class)
                .addAsManifestResource("META-INF/persistence-test.xml", "persistence.xml").addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    // ======================================
    // = Test methods =
    // ======================================

    @Test
    public void should_be_deployed() {
        assertNotNull(publisherBean);
    }

    @Test
    public void should_crud() {
        // Creates an object
        Publisher publisher = new Publisher();
        publisher.setName("Dummy value");

        // Inserts the object into the database
        publisherBean.setPublisher(publisher);
        publisherBean.create();
        publisherBean.update();
        publisher = publisherBean.getPublisher();
        assertNotNull(publisher.getId());

        // Finds the object from the database and checks it's the right one
        publisher = publisherBean.findById(publisher.getId());
        assertEquals("Dummy value", publisher.getName());

        // Deletes the object from the database and checks it's not there
        // anymore
        publisherBean.setId(publisher.getId());
        publisherBean.create();
        publisherBean.delete();
        publisher = publisherBean.findById(publisher.getId());
        assertNull(publisher);
    }

    @Test
    public void should_paginate() {
        // Creates an empty example
        final Publisher example = new Publisher();

        // Paginates through the example
        publisherBean.setExample(example);
        publisherBean.paginate();
        assertTrue((publisherBean.getPageItems().size() == publisherBean.getPageSize()) || (publisherBean.getPageItems().size() == publisherBean.getCount()));
    }
}
