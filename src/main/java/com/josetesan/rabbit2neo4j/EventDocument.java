package com.josetesan.rabbit2neo4j;

import com.ingdirect.databus.domain.event.Event;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document
@Data
public class EventDocument {

    @Id
    private final String id;
    private final String type;
    private final String source;
    private final Map<String,String> payload;

    public EventDocument(Event event) {

        this.id = event.getHeader().getId().toString();
        this.source = event.getHeader().getSource();
        this.type = event.getMetadata().getName();
        this.payload = event.getPayload().asMap();

    }


}
