package com.example;

import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.concurrent.*;
import static org.junit.Assert.*;

public class InMemoryQueueServiceTests {
    
    QueueCreatorService service;
    String defaultQueueName;
    String defaultSingleMessage;

    @Before
    public void setUp() throws Exception {
        defaultQueueName = "default-queue";
        defaultSingleMessage = "default message";

        service = new QueueCreatorService();
        service.createQueue(defaultQueueName);

        InMemoryQueueService inMemoryQueueService = new InMemoryQueueService();
        inMemoryQueueService.push(defaultSingleMessage,defaultQueueName);
        inMemoryQueueService.push("02",defaultQueueName);
    }

    @Test
    public void testShouldCheckMessageIsPushedToASpecificQueue() {

        String queueName = "testPushQueue";
        String messageToSend = "This is message 01";
        createQueue(queueName);

        InMemoryQueueService service = new InMemoryQueueService();
        service.push(messageToSend, queueName);
        ArrayBlockingQueue<Message> queue = null;
        try {
            queue = (ArrayBlockingQueue<Message>) QueueCache.getInstance().get(queueName);
        } catch (IOException e) {
            System.out.print("Should not come here!");
        }
        Message message = queue.peek();
        assertEquals(messageToSend, message.getMessage());
    }

    @Test
    public void testShouldGetMessageWithUniqueIdWhenPullRequestIsMade() {

        InMemoryQueueService service = new InMemoryQueueService();
        try {
            String message = service.pull(defaultQueueName);
            assertEquals(defaultSingleMessage, extractMessageFromMessageWithId(message));
        } catch (Exception e) {
            System.out.print("Should not come here!");
        }
    }

    @Test
    public void testShouldDeleteMessageFromQueue() {

        String queueName = "testDeleteQueue";
        String messageToBeDeleted = "This is message 01";
        String messageToTest = "This is message 02";
        
        createQueue(queueName);
        InMemoryQueueService service = new InMemoryQueueService();
       
        try {
            //push two message
            service.push(messageToBeDeleted, queueName);
            service.push(messageToTest, queueName);

            //pull one
            String message = service.pull(queueName);
            assertEquals(messageToBeDeleted, extractMessageFromMessageWithId(message));

            //extracting message receipt Id to delete from queue
            String messageReciept = extractIdFromMessage(message);
            service.delete(messageReciept, queueName);

            // checking the queue
            ArrayBlockingQueue<Message> q = (ArrayBlockingQueue<Message>) QueueCache.getInstance().get(queueName);
            Message m = q.peek();

            assertEquals(messageToTest, m.getMessage());
        } catch (Exception e) {
            System.out.print("Should not come here!");
        }
    }

    private String extractIdFromMessage(String messageWithId){
        String id;
        int index = messageWithId.indexOf(':');
        id = messageWithId.substring(0,index);
        return id;
    }

    private String extractMessageFromMessageWithId(String messageWithId){
        String id;
        int index = messageWithId.indexOf(':');
        id = messageWithId.substring(index+1, messageWithId.length());
        return id;
    }

    private void createQueue(String queueName){
        QueueCreatorService service;
        service = new QueueCreatorService();
        service.createQueue(queueName);
    }
}
