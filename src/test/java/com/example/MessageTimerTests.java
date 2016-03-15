package com.example;

import org.junit.Test;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class MessageTimerTests {

    @Test
    public void testShouldSetMessageVisibilityToTrue(){

        String messageContent = "test-visibility" ;
        String receiptId = "receiptId";

        // Message with visibility false
        prepareMessageCache(messageContent, receiptId);
        try {
            Message messageBeforeTimer = (Message) MessageCache.getInstance().get(receiptId);
            assertEquals(messageBeforeTimer.getMessage(), messageContent);
            assertFalse(messageBeforeTimer.getVisible());

            MessageTimer timer = new MessageTimer(receiptId);
            timer.setMessageVisibilityToTrue();

            Message messageAfterTimer = (Message) MessageCache.getInstance().get(receiptId);
            assertEquals(messageAfterTimer.getMessage(),messageContent);
            assertTrue(messageAfterTimer.getVisible());

        } catch (Exception e) {
            System.out.print("Should not come here!");
        }
    }

    @Test
    public void testShouldCallScheduledExecutorService() throws InterruptedException {

        final ScheduledExecutorService fakeScheduledExecutorService = mock(ScheduledExecutorService.class);
        MessageTimer messageTimer = new MessageTimer("fake-receipt-id"){

            @Override
            protected ScheduledExecutorService newScheduledExecutorService() {
                return fakeScheduledExecutorService;
            }
        };
        messageTimer.start();
        // This test is verifying that when start is called from MessageTimer ScheduledExecutorService's schedule is
        // been called.
        verify(fakeScheduledExecutorService).schedule(
                any(MessageTimer.TimeOutService.class),
                eq(5L),
                eq(TimeUnit.SECONDS)
        );
    }

    private void prepareMessageCache(String messageContent, String receiptId){

        // Make message visibility false
        Message message = new Message();
        message.setMessage(messageContent);
        message.setVisible(false);
        try {
            MessageCache.getInstance().put(receiptId, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
