package com.idugalic.configuration.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.data.mongodb.ssl")
public class MongoSslProperties {
    /**
    key-store-path: classpath:mongo-tm-trust-store.jks
    key-store-password: 5tep0ver
    key-password: 5tep0ver
     */
    private String keyStorePath;
    private String keyStorePassword;
    private String keyPassword;

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getKeyPassword() {
        return keyPassword;
    }

    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
    }
}
