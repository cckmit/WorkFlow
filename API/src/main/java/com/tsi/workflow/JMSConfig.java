/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import com.tsi.workflow.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

/**
 *
 * @author USER
 */
@Configuration
public class JMSConfig {

    @Autowired
    SingleConnectionFactory singleConnectionFactory;

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
	DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
	factory.setConnectionFactory(singleConnectionFactory);
	factory.setConcurrency("1-1");
	factory.setPubSubDomain(true);
	return factory;
    }

    @Bean
    public JmsTemplate AppToTOSQueue() {
	JmsTemplate template = new JmsTemplate();
	template.setConnectionFactory(singleConnectionFactory);
	template.setDefaultDestinationName(Constants.TOS_QUEUE_APP_TOS);
	return template;
    }

    @Bean
    public JmsTemplate AppToTOSIPQueue() {
	JmsTemplate template = new JmsTemplate();
	template.setConnectionFactory(singleConnectionFactory);
	template.setDefaultDestinationName(Constants.TOS_QUEUE_IP_APP_TOS);
	return template;
    }
}
