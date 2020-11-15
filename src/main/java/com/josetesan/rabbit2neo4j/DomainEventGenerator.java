package com.josetesan.rabbit2neo4j;

import com.ingdirect.databus.domain.event.EventMetadata;
import com.ingdirect.databus.domain.event.EventSource;
import com.ingdirect.databus.domain.event.EventType;
import com.ingdirect.databus.domain.event.Header;
import com.ingdirect.databus.domain.event.Payload;
import com.ingdirect.databus.domain.event.id.EventIdGenerator;
import com.ingdirect.databus.domain.event.types.domain.DomainEvent;
import com.ingdirect.databus.domain.event.types.domain.DomainEventType;

import java.util.Date;

public class DomainEventGenerator {


    public static DomainEvent generate(Integer seed) {

        String _seed = String.valueOf(seed);

        EventMetadata.Builder eventMetadataBuilder = EventMetadata.newBuilder()
            .withName("NAME")
            .withType(EventType.Domain)
            .withSubtype(DomainEventType.DebitTransaction.name())
            .withVersion("1.0");


        Header.Builder headerBuilder = Header.newBuilder()
            .withAckRequired(true)
            .withEventCreationDate(new Date())
            .withEventSource(EventSource.CustomerRelationshipManagement.name())
            .withId(EventIdGenerator.generateId(_seed));

        Payload.Builder payloadBuilder = Payload.newBuilder()
            .addToPayload("name",_seed)
            .addToPayload("surname",_seed)
            .addToPayload("test",_seed);

        DomainEvent.Builder builder = DomainEvent.newBuilder();

        return builder
            .withEventMetadataBuilder(eventMetadataBuilder)
            .withHeaderBuilder(headerBuilder)
            .withPayloadBuilder(payloadBuilder)
            .withTag("TAG")
            .build();
    }

}
