package com.company;

import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class View {
    private Controller controller;
    private JFrame frame;
    private JLabel label;

    public void close() {
        frame.dispose();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void create(int width, int height) {
        frame = new JFrame();
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);

        label = new JLabel();
        label.setBounds(0, 0, width, height);
        frame.add(label);

        frame.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                controller.onMouseMotion(e.getX(), e.getY());
            }
        });

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                controller.onKeyPress(e.getKeyCode());
            }
        });

        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new Thread(() -> controller.onMousePress(e.getButton(), e.getX(), e.getY())).start();
            }
        });

        frame.setVisible(true);
    }

    public void setImage(BufferedImage image) {
        label.setIcon(new ImageIcon(image));
    }
}
