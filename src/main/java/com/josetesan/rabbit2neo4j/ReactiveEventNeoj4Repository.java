package com.josetesan.rabbit2neo4j;

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;

public interface ReactiveEventNeoj4Repository extends ReactiveNeo4jRepository<EventDocument, String> {
}
