package com.example;

import java.util.concurrent.ArrayBlockingQueue;

// todo: Should have a method which will only retrurn queue name after creation.
public interface QueueCreator {
     ArrayBlockingQueue createQueue(String queueName);
}
