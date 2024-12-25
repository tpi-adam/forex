package com.tpi.forexapi.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartUpApplicationListener {

    private final MongoTemplate mongoTemplate;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // db migration
    }

}
