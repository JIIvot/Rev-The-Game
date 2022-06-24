package com.company;

import javax.swing.*;
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

        frame.setVisible(true);
    }

    public void setImage(BufferedImage image) {
        frame.setIconImage(image);
    }
}
