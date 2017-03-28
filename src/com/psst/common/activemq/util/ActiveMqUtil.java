package com.psst.common.activemq.util;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.psst.common.util.PropertiesLoader;

public class ActiveMqUtil {
    private static ConnectionFactory connectionFactory = null;
    private static Connection connection = null;
    private static Session session = null;
    private static String ADDR = null;
    private static String USER = null;
    private static String PASSWORD = null;
    static {
        PropertiesLoader propertiesLoader =  new PropertiesLoader("system.properties");
        ADDR = propertiesLoader.getProperty("activeMq_url");
//        USER = propertiesLoader.getProperty("activeMq_username");
//        PASSWORD = propertiesLoader.getProperty("activeMq_password");
        //connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, ADDR);
        connectionFactory = new ActiveMQConnectionFactory(USER, PASSWORD, ADDR);
    }
    /**
     * 获得连接
     *   连接一创建则开启
     * @return
     * @throws JMSException
     */
    public static Connection getConnection() throws JMSException {
        if(null == connection) {
            connection = connectionFactory.createConnection();
            connection.start();
        }
        return connection;
    }
    /**
     * 关闭连接
     * @throws JMSException
     */
    public static void closeConnection () throws JMSException {
        if(null != connection) {
            connection.close();
        }
    }
    /**
     * 获得一个会话
     * @param flag
     *   true  启用事务   false 不启用事务
     * @return
     * @throws JMSException
     */
    public static Session getSession(Boolean flag) throws JMSException {
        /**
         * 其中transacted为使用事务标识，acknowledgeMode为签收模式。
         */
        Session session = getConnection().createSession(flag, Session.AUTO_ACKNOWLEDGE);
        return session;
    }
    /**
     * 获得一个会话
     *    默认开始事务
     * @return
     * @throws JMSException
     */
    public static Session getSession() throws JMSException {
        return getSession(true);
    }
    /**
     * 关闭一个MQ会话
     * @param session
     * @throws JMSException
     */
    public static void closeSession(Session session) throws JMSException {
        if(null != session) {
            session.close();
        }
    }
    /**
     * 提交事务
     * @param session
     * @throws JMSException
     */
    public static void commit(Session session) throws JMSException {
        if(null != session) {
            session.commit();
        }
    }
    /**
     * 获得消费者会话  此会话一直保存
     * @return
     * @throws JMSException
     */
    public static Session getRevSession() throws JMSException {
        if(null == session) {
            session = getConnection().createSession(false, Session.AUTO_ACKNOWLEDGE);
        }
        return session;
    }
    /**
     * 重新或者连接
     * @throws JMSException
     */
    public static void redestoryConnection() throws JMSException {
        closeConnection();
        getConnection();
    }
    
    
    /**
     * 获得一个队列
     * @param session
     *   MQ会话
     * @param queue
     *   队列名称
     * @return
     * @throws JMSException
     */
    public static Destination getQueue(Session session, String queue) throws JMSException {
        Queue queueT = session.createQueue(queue);
        return queueT;
    }
    /**
     * 获得一个主题
     * @param session
     *   MQ会话
     * @param topic
     *   主题名称
     * @return
     * @throws JMSException
     */
    public static Destination getTopic(Session session, String topic) throws JMSException {
        Topic topicT = session.createTopic(topic);
        return topicT;
    }
    
    
    /**
     * 获得一个点对点模式生产者
     * @param session
     *   MQ会话
     * @param queue
     *   队列名称
     * @return
     * @throws JMSException
     */
    public static MessageProducer getProducerQueue(Session session,String queue) throws JMSException {
        MessageProducer producer = session.createProducer(getQueue(session, queue));
        return producer;
    }
    
    /**
     * 获得一个发布模式生产者
     * @param session
     *   MQ会话
     * @param topic
     *   主题名称
     * @return
     * @throws JMSException
     */
    public static MessageProducer getProducerTopic(Session session,String topic) throws JMSException {
        MessageProducer producer = session.createProducer(getTopic(session, topic));
        return producer;
    }
    
    /**
     * 获得一个点对点模式的消费者
     * @param session
     *   MQ会话
     * @param queue
     *   队列名称
     * @return
     * @throws JMSException
     */
    public static MessageConsumer getConsumerQueue(Session session, String queue) throws JMSException {
        MessageConsumer consumer = session.createConsumer(getQueue(session, queue));
        return consumer;
    }
    
    /**
     * 获得一个订阅模式的消费者
     * @param session
     *   MQ会话
     * @param topic
     *   主题名称
     * @return
     * @throws JMSException
     */
    public static MessageConsumer getConsumerTopic(Session session, String topic) throws JMSException {
        MessageConsumer consumer = session.createConsumer(getTopic(session, topic));
        return consumer;
    }
    
    /**
     * 发布一条消息 点对点模式
     * @param messageQueue
     * 队列名称
     * @param message
     * @throws JMSException
     */
    public static void sendMessageQueue(String messageQueue, String message) throws JMSException {
        Session session = getSession();
        TextMessage textMessage = session.createTextMessage(message);
        getProducerQueue(session, messageQueue).send(textMessage);
        session.commit();
        closeSession(session);
    }
    /**
     * 发布一条消息 订阅模式
     * @param topic
     *   订阅主题
     * @param message
     * @throws JMSException
     */
    public static void sendMessageTopic(String topic, String message) throws JMSException {
        Session session = getSession();
        TextMessage textMessage = session.createTextMessage(message);
        getProducerTopic(session,  topic).send(textMessage);
        session.commit();
        closeSession(session);
    }
    /**
     * 获得一条点对点消息
     * @param messageQueue
     *   队列名称
     * @return
     * @throws JMSException
     */
    public static String receiveMessageQueue(String messageQueue) throws JMSException {
        Session session = getSession(false);
        MessageConsumer consumer = getConsumerQueue(session, messageQueue);
        TextMessage textMessage = (TextMessage)consumer.receive();
        return textMessage == null ? null : textMessage.getText();
    }
    /**
     * 获得一条点对点消息
     * @param messageQueue
     *   队列名称
     * @param listener
     *      消息监听，收到消息调用listener 的onMessage方法
     * @throws JMSException
     */
    public static void receiveMessageQueue(String messageQueue, MessageListener listener) throws JMSException {
        Session session = getRevSession();
        MessageConsumer consumer = getConsumerQueue(session, messageQueue);
        if(null != listener) {
            consumer.setMessageListener(listener);
        }
    }
    /**
     * 获得一条订阅消息
     * @param topic
     *    订阅主题
     * @return
     * @throws JMSException
     */
    public static String receiveMessageTopic(String topic) throws JMSException {
        Session session = getSession(false);
        MessageConsumer consumer = getConsumerTopic(session, topic);
        TextMessage textMessage = (TextMessage)consumer.receive();
        return textMessage == null ? null : textMessage.getText();
    }
    /**
     * 获得一条订阅消息
     * @param topic
     *      订阅主题
     * @param listener
     *      消息监听，收到消息调用listener 的onMessage方法
     * @throws JMSException
     */
    public static void receiveMessageTopic(String topic, MessageListener listener) throws JMSException {
        Session session = getRevSession();
        MessageConsumer consumer = getConsumerTopic(session, topic);
        if(null != listener) {
            consumer.setMessageListener(listener);
        }
    }
    
    public static void testQueue() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Long num = 1L;
                while(true) {
                    try {
                        String message = "发送消息:" + num ++;
                        System.out.println(message);
                        ActiveMqUtil.sendMessageQueue("FirstQueue", message);
                        //Thread.sleep(Math.round((Math.random() * 5000)));
                        Thread.sleep(4000L);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                   
                }
                
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        /*String message = ActiveMqUtil.receiveMessageQueue("FirstQueue");
                        if(null != message) {
                            System.out.println("1接收到消息:" + message);
                        } else {
                            System.out.println("没有消息..............");
                        }*/
                        ActiveMqUtil.receiveMessageQueue("FirstQueue", new MessageListener() {
                            
                            @Override
                            public void onMessage(Message message) {
                                try {
                                    System.out.println("1接收到消息:" + ((TextMessage)message).getText());
                                } catch (JMSException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        /*String message = ActiveMqUtil.receiveMessageQueue("FirstQueue");
                        if(null != message) {
                            System.out.println("2接收到消息:" + message);
                        } else {
                            System.out.println("没有消息..............");
                        }*/
                        ActiveMqUtil.receiveMessageQueue("FirstQueue", new MessageListener() {
                            @Override
                            public void onMessage(Message message) {
                                try {
                                    System.out.println("2接收到消息:" + ((TextMessage)message).getText());
                                } catch (JMSException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    
    public static void testTopic() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Long num = 1L;
                while(true) {
                    try {
                        String message = "发送消息:" + num ++;
                        System.out.println(message);
                        ActiveMqUtil.sendMessageTopic("FirstTopic", message);
                        //Thread.sleep(Math.round((Math.random() * 5000)));
                        Thread.sleep(4000L);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                   
                }
                
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        String message = ActiveMqUtil.receiveMessageTopic("FirstTopic");
                        if(null != message) {
                            System.out.println("1接收到消息:" + message);
                        } else {
                            System.out.println("没有消息..............");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        String message = ActiveMqUtil.receiveMessageTopic("FirstTopic");
                        if(null != message) {
                            System.out.println("2接收到消息:" + message);
                        } else {
                            System.out.println("没有消息..............");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    public static void main(String[] args) {
        //testTopic();
        testQueue();   
    }
}
