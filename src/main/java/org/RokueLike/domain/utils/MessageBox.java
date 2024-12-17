package org.RokueLike.domain.utils;

import java.util.ArrayList;
import java.util.List;

public class MessageBox {

    private List<String> messageQueue;
    private List<Integer> times;

    public MessageBox() {
        this.messageQueue = new ArrayList<>();
        this.times = new ArrayList<>();
    }

    public void addMessage(String message, int time) {
        this.messageQueue.add(message);
        this.times.add(time*10);
    }

    public String getMessage() {
        try {
            this.times.set(0, this.times.getFirst()-1);

            if(this.times.getFirst() <= 0) {
                this.times.removeFirst();
                this.messageQueue.removeFirst();
            }

            return this.messageQueue.getFirst();

        } catch(IndexOutOfBoundsException e) {
            return null;
        }
    }

    public int getTime() {
        return this.times.getFirst();
    }

}
