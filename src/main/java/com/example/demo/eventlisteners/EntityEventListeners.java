package com.example.demo.eventlisteners;

import datadog.trace.api.Trace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EntityEventListeners {
    private static final Logger logger = LoggerFactory.getLogger(EntityEventListeners.class);

    @Trace
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEvent(MyCustomEvent event) {
        logger.info("Transaction committed, processing event: {}", event.getMessage());
    }
}
