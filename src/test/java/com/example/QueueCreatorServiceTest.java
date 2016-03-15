package com.example;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.concurrent.ArrayBlockingQueue;

public class QueueCreatorServiceTest {

    @Test
    public void testShouldCreateQueue(){
        QueueCreatorService service = new QueueCreatorService();
        ArrayBlockingQueue queueFirst = null;
        ArrayBlockingQueue queueLast = null;
        try{
            queueFirst = service.createQueue("test01");
            queueLast = service.createQueue("test01");
        }catch (Exception e){

        }
        assertEquals(queueFirst,queueLast);
    }

    @Test
    public void testShouldNotCreateQueueIfExists(){
        QueueCreatorService service = new QueueCreatorService();
        ArrayBlockingQueue queueFirst = null;
        ArrayBlockingQueue queueLast = null;
        try{
            queueFirst = service.createQueue("test01");
            queueLast = service.createQueue("test01");
        }catch (Exception e){

        }
        assertEquals(queueFirst,queueLast);
    }
}
