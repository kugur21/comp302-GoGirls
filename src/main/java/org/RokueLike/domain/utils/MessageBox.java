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
        this.times.add(time*100);
    }

    public String getMessage() {
        try {
            this.times.set(0, this.times.get(0)-1);

            if(this.times.get(0) <= 0) {
                this.times.removeFirst();
                this.messageQueue.removeFirst();
            }

            return this.messageQueue.get(0);
        } catch(IndexOutOfBoundsException e) {
            return null;
        }
    }

    public int getTime() {
        return this.times.getFirst();
    }

}
