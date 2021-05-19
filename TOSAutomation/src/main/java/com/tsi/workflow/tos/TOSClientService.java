package com.tsi.workflow.tos;

import com.tsi.workflow.tos.client.TOSClientImpl;
import com.tsi.workflow.utils.Constants;
import java.io.InputStream;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.PropertyConfigurator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author USER
 */
public class TOSClientService {

    public static void startClient(String[] args) throws Exception {
	TOSClientDaemon.start(args);
    }

    public static void stopClient(String[] args) throws Exception {
	InputStream lLogStream = null;
	Connection connection = null;
	try {
	    lLogStream = TOSClientImpl.class.getResourceAsStream("/log4j-tos.properties");
	    PropertyConfigurator.configure(lLogStream);
	    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
	    connection = connectionFactory.createConnection(TOSConfig.getInstance().getServiceUserID(), TOSConfig.getInstance().getServiceSecret());
	    connection.start();
	    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	    Destination destination = session.createQueue(Constants.TOS_QUEUE_EXIT);
	    MessageProducer producer = session.createProducer(destination);
	    producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
	    String text = "Exit";
	    TextMessage message = session.createTextMessage(text);
	    producer.send(message);
	    session.close();
	} finally {
	    if (connection != null) {
		connection.close();
	    }
	}
    }
}
