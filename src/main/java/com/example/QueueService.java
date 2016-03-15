package com.example;

public interface QueueService {
    //todo: design a method to create queue, delete queue, list of available queues

    // push method has void type. Improved version should have a error handling system and return error type upon push.
    void push(String message, String queueName);
    // This mehtod returns a receipt id for make a delete call
    String pull(String queueName);
    //Use the reciept is that was given upon a pull call.
    void delete(String messageId, String queueName);

}
