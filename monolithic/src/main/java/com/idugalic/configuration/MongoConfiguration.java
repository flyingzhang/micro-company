package com.idugalic.configuration;

import com.idugalic.configuration.domain.MongoSslProperties;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import org.apache.commons.lang.StringUtils;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.extensions.mongo.DefaultMongoTemplate;
import org.axonframework.extensions.mongo.MongoTemplate;
import org.axonframework.extensions.mongo.eventhandling.saga.repository.MongoSagaStore;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.extensions.mongo.eventsourcing.tokenstore.MongoTokenStore;
import org.axonframework.monitoring.NoOpMessageMonitor;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Optional;

@Configuration
//@ConditionalOnProperty(value = "axon.eventsourcing.storage", havingValue = "mongo")
@EnableConfigurationProperties(MongoSslProperties.class)
public class MongoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(MongoConfiguration.class);

    private ResourceLoader resourceLoader;

    public MongoConfiguration(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public EmbeddedEventStore eventStore(EventStorageEngine storageEngine, AxonConfiguration configuration) {
        return EmbeddedEventStore.builder()
                .storageEngine(storageEngine)
                .messageMonitor(NoOpMessageMonitor.instance())
                .build();
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return DefaultMongoTemplate.builder().mongoDatabase(mongoClient).build();
    }

    // The `MongoEventStorageEngine` stores each event in a separate MongoDB document
    @Bean
    public EventStorageEngine storageEngine(MongoTemplate mongoTemplate, Serializer serializer) {
        return MongoEventStorageEngine.builder()
                .mongoTemplate(mongoTemplate)
                .eventSerializer(serializer)
                .snapshotSerializer(serializer)
                .build();
    }

    @Bean
    public MongoSagaStore mongoSagaStore(MongoTemplate mongoTemplate, Serializer serializer) {
        return MongoSagaStore.builder()
                .mongoTemplate(mongoTemplate)
                .serializer(serializer)
                .build();
    }

    @Bean
    public MongoTokenStore mongoTokenStore(MongoTemplate mongoTemplate, Serializer serializer) {
        return MongoTokenStore.builder()
                .mongoTemplate(mongoTemplate)
                .serializer(serializer)
                .build();
    }

    @Bean
    public MongoClient mongoClient(MongoProperties mongoProperties, MongoSslProperties mongoSslProperties, Environment environment) throws UnknownHostException {
        SSLContext sslContext = buildSslContext(mongoSslProperties);
        return mongoProperties.createMongoClient(MongoClientOptions.builder().sslContext(buildSslContext(mongoSslProperties)).build(), environment);
    }


    private SSLContext buildSslContext(MongoSslProperties mongoDBConfig) {
        if (StringUtils.isNotBlank(mongoDBConfig.getKeyStorePath())) {
            if (StringUtils.isBlank(mongoDBConfig.getKeyStorePassword())) {
                throw new RuntimeException("mongo dbconfig: keyStorePassword is empy for " + mongoDBConfig.getKeyStorePath());
            }
            SSLContextBuilder builder = SSLContexts.custom();
            try {
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(resourceLoader.getResource(mongoDBConfig.getKeyStorePath()).getInputStream(),mongoDBConfig.getKeyStorePassword().toCharArray());
                builder.loadKeyMaterial(keyStore, Optional.ofNullable(mongoDBConfig.getKeyPassword()).map(String::toCharArray).get());
                builder.loadTrustMaterial(keyStore, TrustSelfSignedStrategy.INSTANCE);
                return builder.build();
            } catch (NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException | CertificateException | KeyManagementException e) {
                log.error("security configuration error: {}", e);
                throw new RuntimeException("Security Configuration Exception: ", e);
            } catch (IOException e) {
                log.error("security configuration error: accessing key store error: {}", e);
                throw new RuntimeException("Security Configuration Exception: cannot access configured key store.", e);
            }
        } else {
            log.info("default key store used.");
            return SSLContexts.createSystemDefault();
        }
    }

}
