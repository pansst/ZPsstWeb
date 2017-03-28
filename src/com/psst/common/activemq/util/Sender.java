package com.psst.common.activemq.util;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Sender {
    private static final int SEND_NUMBER = 5;
    public static void sendMessgae(Session session, MessageProducer producer) throws JMSException {
        for(int i = 0; i < SEND_NUMBER; i++) {
            TextMessage  message = session.createTextMessage("ActiveMq 发送的消息，消息序号为" + (i + 1));
            producer.send(message);
            System.out.println("ActiveMq 发送的消息，消息序号为" + (i + 1));
        }
    }
    public static void sendMessgae() {
        ConnectionFactory connectionFactory = null;
        Connection connection = null;
        Session session = null;
        Destination destination = null;
        MessageProducer producer = null;
        //connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");
        connectionFactory = new ActiveMQConnectionFactory(null, null, "tcp://localhost:61616");
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("FirstQueue");
            producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            sendMessgae(session, producer);
            session.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(null != session) {
                    session.close();
                }
                if(null != connection) {
                    connection.close();
                }
            } catch (Exception e2) {
            }
        }
    }
    public static void main(String[] args) {
        sendMessgae();
    }
}
