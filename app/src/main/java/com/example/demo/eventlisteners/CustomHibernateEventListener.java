package com.example.demo.eventlisteners;

import datadog.trace.api.Trace;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomHibernateEventListener
        implements PreInsertEventListener,
                PostInsertEventListener,
                PreUpdateEventListener,
                PostUpdateEventListener,
                PreDeleteEventListener,
                PostDeleteEventListener,
                FlushEventListener {

    private static final Logger logger =
            LoggerFactory.getLogger(CustomHibernateEventListener.class);

    @Trace
    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        logEvent("PreInsert", event.getEntity());
        return false; // Return false to allow the insert
    }

    @Trace
    @Override
    public void onPostInsert(PostInsertEvent event) {
        logEvent("PostInsert", event.getEntity());
    }

    @Trace
    @Override
    public boolean onPreUpdate(PreUpdateEvent event) {
        logEvent("PreUpdate", event.getEntity());
        return false; // Return false to allow the update
    }

    @Trace
    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        logEvent("PostUpdate", event.getEntity());
    }

    @Trace
    @Override
    public boolean onPreDelete(PreDeleteEvent event) {
        logEvent("PreDelete", event.getEntity());
        return false; // Return false to allow the delete
    }

    @Trace
    @Override
    public void onPostDelete(PostDeleteEvent event) {
        logEvent("PostDelete", event.getEntity());
    }

    @Trace
    @Override
    public void onFlush(FlushEvent event) {
        logger.info("Flush event triggered");
    }

    private void logEvent(String eventType, Object entity) {
        logger.info("{} event for entity: {}", eventType, entity);
    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister entityPersister) {
        return false;
    }
}
