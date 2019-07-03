package com.idugalic.commandside.project.configuration;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.messaging.interceptors.BeanValidationInterceptor;
import org.axonframework.monitoring.NoOpMessageMonitor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * A configuration for axonframework.
 * Axonframework is used to support eventsourcing and CQRS.
 * 
 * @author idugalic
 *
 */
@Configuration
public class AxonConfiguration {
    
    @Bean
    CommandBus commandBus(TransactionManager transactionManager) {
        SimpleCommandBus commandBus = SimpleCommandBus.builder().transactionManager(transactionManager).messageMonitor(NoOpMessageMonitor.INSTANCE).build();
        commandBus.registerDispatchInterceptor(new BeanValidationInterceptor());
        return commandBus;
    }
}
