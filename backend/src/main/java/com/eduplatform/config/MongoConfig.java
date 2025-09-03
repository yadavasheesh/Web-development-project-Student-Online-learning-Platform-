package com.eduplatform.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.concurrent.TimeUnit;

/**
 * MongoDB Configuration for optimized performance
 * Implements connection pooling and query optimization
 */
@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Bean
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(mongoUri);

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToConnectionPoolSettings(builder -> 
                    builder.maxSize(100)  // Increased for 200+ concurrent sessions
                           .minSize(10)
                           .maxWaitTime(5, TimeUnit.SECONDS)
                           .maxConnectionIdleTime(60, TimeUnit.SECONDS)
                           .maxConnectionLifeTime(300, TimeUnit.SECONDS))
                .applyToSocketSettings(builder -> 
                    builder.connectTimeout(10, TimeUnit.SECONDS)
                           .readTimeout(15, TimeUnit.SECONDS))
                .applyToServerSettings(builder -> 
                    builder.heartbeatFrequency(10, TimeUnit.SECONDS)
                           .minHeartbeatFrequency(500, TimeUnit.MILLISECONDS))
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        MongoTemplate template = new MongoTemplate(mongoClient(), databaseName);

        // Remove _class field for cleaner documents
        MappingMongoConverter converter = (MappingMongoConverter) template.getConverter();
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));

        return template;
    }
}