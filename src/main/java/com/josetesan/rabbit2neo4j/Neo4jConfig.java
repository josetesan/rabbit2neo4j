package com.josetesan.rabbit2neo4j;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.core.transaction.ReactiveNeo4jTransactionManager;
import org.springframework.data.neo4j.repository.config.EnableReactiveNeo4jRepositories;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableReactiveNeo4jRepositories
@EnableTransactionManagement
public class Neo4jConfig {

    @Bean
    TransactionManager reactiveTransactionManager() {
        Driver driver = GraphDatabase
            .driver("neo4j://localhost:7687", AuthTokens.basic("neo4j", "secret"));

        return new ReactiveNeo4jTransactionManager(driver);
    }
}
