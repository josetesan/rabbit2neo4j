package com.josetesan.rabbit2neo4j;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactiveEventMongoRepository extends ReactiveMongoRepository<EventDocument, String> {


}
