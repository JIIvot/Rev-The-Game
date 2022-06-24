package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Controller {
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private View view;
    private Graphics graphics;

    public void setView(View view) {
        this.view = view;
    }

    public void start() {
        view.create(SCREEN_WIDTH, SCREEN_HEIGHT);

        render();
    }

    private void render() {
        BufferedImage image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        graphics = image.getGraphics();

        view.setImage(image);
    }
}
