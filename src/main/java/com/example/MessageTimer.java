package com.example;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.ScheduledExecutorService;

/* This class is responsible for starting thread containing action of making a message visible again
   after a certain period of time.
* */
public class MessageTimer {

    static String messageReciept;
    ScheduledExecutorService scheduledExecutorService ;

    public MessageTimer(String recieptId) throws InterruptedException {

        this.messageReciept = recieptId;
        this.scheduledExecutorService = newScheduledExecutorService();
    }

    static void setMessageVisibilityToTrue(){

        Message messageInCache = null;
        try {
            messageInCache = (Message) (MessageCache.getInstance().get(messageReciept));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // If its already removed then don't bother!
        if (messageInCache != null) {
            messageInCache.setVisible(Boolean.TRUE);
        }
    }

    protected static class TimeOutService implements  Runnable {

        public void run()  {
            setMessageVisibilityToTrue();
        }
    }

    public void start() {
        // todo: need to make the time configurable from properties files.
        scheduledExecutorService.schedule(new TimeOutService(), 5, TimeUnit.SECONDS);
        scheduledExecutorService.shutdown();
        //todo: i would do some proper logging here
    }

    //todo: need to make this configurable thread counts
    protected ScheduledExecutorService newScheduledExecutorService() {
        return  Executors.newScheduledThreadPool(5);
    }
}