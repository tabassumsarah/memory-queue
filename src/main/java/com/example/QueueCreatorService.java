package com.example;

import com.google.common.collect.Queues;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

class QueueCreatorService implements QueueCreator{

    public ArrayBlockingQueue createQueue(String queueName) {

        ArrayBlockingQueue<String> queue = null;
        QueueCache queueCache = null;
        try {
            queueCache = QueueCache.getInstance();
        } catch (IOException e) {
            // todo: Proper Exception Handling
        }

        if (queueCache.get(queueName) == null) {
            queue = Queues.newArrayBlockingQueue(10);
            queueCache.put(queueName, queue);
            //todo: proper logging
        } else {
            //todo: proper logging
            return (ArrayBlockingQueue) queueCache.get(queueName);
        }
        return queue;
    }
}

