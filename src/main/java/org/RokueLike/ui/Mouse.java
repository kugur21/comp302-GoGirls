package org.RokueLike.ui;

import org.RokueLike.domain.GameManager;

import java.awt.event.MouseListener;

public class Mouse implements MouseListener {
    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {}

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {}

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {}

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {}

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        GameManager.handleLeftClick(e.getX(), e.getY());
    }

}
