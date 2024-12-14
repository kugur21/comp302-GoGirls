package org.RokueLike.domain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameLoop implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent arg0) {
        try {
            // TODO Auto-generated method stub
        } catch (Exception e) {
            System.out.println("Error in GameLoop");
            e.printStackTrace();
            System.exit(-1);
        }

    }
}
