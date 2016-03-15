package com.example;

import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.*;

public class InMemoryQueueService implements QueueService {

    public void push(String message, String queueName) {

        // todo: Check For queue existence and handle proper exception
        QueueCache queueCache = null;
        try {
            queueCache = QueueCache.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayBlockingQueue queue = (ArrayBlockingQueue) queueCache.get(queueName);
        Message msg = new Message(message);
        msg.setVisible(Boolean.TRUE);

        try {
            queue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //todo: Should implement custom exception handling for a invalid queue
    public String pull (String queueName){

        ArrayBlockingQueue<Message> queue = getQueue(queueName);
        return getMessageFromQueue(queue);
    }

    public void delete(String messageId, String queueName)  {

        Message message = null;
        try {
            MessageCache cache = MessageCache.getInstance();
            message = (Message) cache.get(messageId);
            cache.delete(messageId, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        getQueue(queueName).remove(message);
    }

    /*
    * Get existing queue from QueueCache
    * todo: Should Throw a QueueNotFoundException
    * */
    private ArrayBlockingQueue<Message> getQueue(String queueName){

        QueueCache  queueCache = null;
        try {
            queueCache = QueueCache.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayBlockingQueue queue = (ArrayBlockingQueue) queueCache.get(queueName);
        return queue;
    }

    /**
     * This method returns message from queue based on it's visibility status.
     * If the message from queue head has a not visible status tru then the next message from the queue is checked
     * and so on.
     *
     * @param queue
     * @return Message Object
     * @see Message
     */
    private String getMessageFromQueue(ArrayBlockingQueue<Message> queue){

        Message message = queue.peek();

        if (message.getVisible()) {
            message = queue.peek();
        } else {
            message = getNextVisibleMessageFromQueue(queue);
        }

        String systemGeneratedId = generateUniqueIdForEachConsumer(message);
        processMessage(message, systemGeneratedId);
        // concat the generated id with the actual message specific for each pull call
        return  getMessageWithReceiptId(message.getMessage(), systemGeneratedId);
    }

    /**
     * This method populates the given queue and gets the next visible message.
     * @param queue
     * @return Visible message
     * @see Message
     */
    private Message getNextVisibleMessageFromQueue(ArrayBlockingQueue<Message> queue){

        Iterator iterator = queue.iterator();
        while(iterator.hasNext()) {
            Message message = (Message) iterator.next();
            if(message.getVisible()){
                return  message;
            }
        }
        //todo handle exception for not visible message
        return new Message();
    }

    private synchronized String generateUniqueIdForEachConsumer(Message m){
        return  UUID.randomUUID().toString();
    }

    /*
    * This method makes the requested message invisible for a certain period of time. Store it in the message cache.
    * starts the timer.
    * */
    private void processMessage(Message message, String id){

        message.setVisible(Boolean.FALSE);
        storeInvisibleMessageInMessageCache(message, id);
        startVisibilityTimeout(id);
    }

    private void storeInvisibleMessageInMessageCache(Message message, String id){

        try {
            MessageCache messageCache = MessageCache.getInstance();
            messageCache.put(id, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is responsible for keeping track of the time for a message to turn visible again.
     * @param messageReceiptId
     */
    protected void startVisibilityTimeout(String messageReceiptId){

        try {
            MessageTimer messageTimer = new MessageTimer(messageReceiptId);
            messageTimer.start();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //concatenates reciept id in the message so consumer can send the receipt id when make a delete call.
    private String getMessageWithReceiptId(String message, String id){

        return id.concat(":").concat(message);
    }
}