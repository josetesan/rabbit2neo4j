package com.josetesan.rabbit2neo4j;

import com.ingdirect.databus.eda.event.marshaller.Marshaller;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.OutboundMessageResult;
import reactor.rabbitmq.QueueSpecification;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.Sender;

import java.io.IOException;
import java.util.Objects;

@SpringBootApplication
@Slf4j
public class Rabbit2neo4jApplication implements CommandLineRunner {


	@Autowired
	private Receiver receiver;

	@Autowired
	private Sender sender;

	@Autowired
	private ReactiveEventNeoj4Repository reactiveEventNeoj4Repository;

	@Autowired
	private Marshaller marshaller;

	public static void main(String[] args) {
		SpringApplication.run(Rabbit2neo4jApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

		int count = 1000;

		Flux<OutboundMessageResult> confirmations = sender.sendWithPublishConfirms(Flux.range(1, count)
			.map(DomainEventGenerator::generate)
			.map( domainEvent -> {
				try {
					return marshaller.marshal(domainEvent);
				} catch (IOException e) {
					return null;
				}
			})
			.filter(Objects::nonNull)
			.map(bytes -> new OutboundMessage("", RabbitConsumer.QUEUE, bytes)));

		sender.declareQueue(QueueSpecification.queue(RabbitConsumer.QUEUE))
			.thenMany(confirmations)
			.doOnError(e -> log.error("Send failed", e))
			.subscribe(r -> {
				if (r.isAck()) {
					log.info("Message sent successfully");
				}
			});

		receiver.consumeAutoAck(RabbitConsumer.QUEUE)
			.map(m -> {
				try {
					return marshaller.unmarshal(m.getBody());
				} catch (IOException e) {
					return null;
				}
			})
			.filter(Objects::nonNull)
			.map(EventDocument::new)
			.map(reactiveEventNeoj4Repository::save)
			.subscribe(eventDocumentMono -> log.info("Evento guardado "));



		receiver.close();
		sender.close();


	}
}
