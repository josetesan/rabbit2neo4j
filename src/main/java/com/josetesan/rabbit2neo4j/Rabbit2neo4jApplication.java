package com.josetesan.rabbit2neo4j;

import com.ingdirect.databus.eda.event.marshaller.Marshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.rabbitmq.Receiver;

import java.io.IOException;
import java.util.Objects;

@SpringBootApplication
public class Rabbit2neo4jApplication implements CommandLineRunner {


	@Autowired
	private Receiver receiver;

	@Autowired
	private ReactiveEventMongoRepository reactiveEventMongoRepository;

	@Autowired
	private ReactiveEventNeoj4Repository reactiveEventNeoj4Repository;

	@Autowired
	private Marshaller marshaller;

	public static void main(String[] args) {
		SpringApplication.run(Rabbit2neo4jApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

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
			.subscribe( event -> {
				reactiveEventMongoRepository.insert(event);
				reactiveEventNeoj4Repository.save(event);
			});

	}
}
