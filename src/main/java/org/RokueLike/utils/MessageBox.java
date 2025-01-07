package org.RokueLike.utils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

public class MessageBox implements Serializable {

    private static class Message implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L; // Serialization identifier

        private final String content;
        private int timeRemaining;

        public Message(String content, int seconds) {
            this.content = content;
            this.timeRemaining = seconds * 1000;
        }

        public String getContent() {
            return content;
        }

        public int getTimeRemaining() {
            return timeRemaining;
        }

        public void decrementTime() {
            this.timeRemaining--;
        }
    }

    @Serial
    private static final long serialVersionUID = 1L; // Serialization identifier

    private final Deque<Message> messageQueue;

    public MessageBox() {
        this.messageQueue = new LinkedList<>();
    }

    public void addMessage(String message, int time) {
        if (message == null || time <= 0) {
            return;
        }
        // Clear the current message if there is one
        if (!messageQueue.isEmpty()) {
            messageQueue.clear(); // Discard the current message and all waiting messages
        }
        this.messageQueue.addLast(new Message(message, time));
    }

    public String getMessage() {
        if (messageQueue.isEmpty()) {
            return null; // No messages
        }

        Message currentMessage = messageQueue.peekFirst();
        currentMessage.decrementTime();

        if (currentMessage.getTimeRemaining() <= 0) {
            messageQueue.pollFirst(); // Remove the message when its time expires
        }

        return currentMessage.getContent();
    }

    public Integer getTime() {
        if (messageQueue.isEmpty()) {
            return null; // No messages
        }
        return messageQueue.peekFirst().getTimeRemaining();
    }

}