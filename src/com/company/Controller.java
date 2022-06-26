package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Controller {
    private static final int FIELD_WIDTH = 16;
    private static final int FIELD_HEIGHT = 12;
    private static final int SQUARE_SIZE = 50;
    private static final int SCREEN_WIDTH = SQUARE_SIZE * FIELD_WIDTH;
    private static final int SCREEN_HEIGHT = SQUARE_SIZE * FIELD_HEIGHT;

    private static final Color CELL_COLOR = Color.GRAY;
    private static final Color ALTERNATIVE_CELL_COLOR = Color.DARK_GRAY;

    private static final Color SELECTED_COLOR = new Color(0, 0, 0, 0.3f);

    private Point selectedSquare = null;

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
        var image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        graphics = image.getGraphics();

        drawBackground();

        if (selectedSquare != null) {
            drawSelected();
        }

        view.setImage(image);
    }

    private void drawSelected() {
        graphics.setColor(SELECTED_COLOR);
        graphics.fillRect(vtr(selectedSquare.x), vtr(selectedSquare.y), SQUARE_SIZE, SQUARE_SIZE);
    }

    private void drawBackground() {
        boolean currentColor = true;

        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_HEIGHT; j++) {
                graphics.setColor(currentColor ? ALTERNATIVE_CELL_COLOR : CELL_COLOR);
                graphics.fillRect(vtr(i), vtr(j), SQUARE_SIZE, SQUARE_SIZE);
                currentColor = !currentColor;
            }
            currentColor = !currentColor;
        }
    }

    public void onMouseMotion(int mouseX, int mouseY) {
        Point currentLocation = new Point(rtv(mouseX), rtv(mouseY));

        if (!currentLocation.equals(selectedSquare)) {
            selectedSquare = currentLocation;
            render();
        }
    }

    private int vtr(int v) {
        return v * SQUARE_SIZE;
    }

    private int rtv(int r) {
        return r / SQUARE_SIZE;
    }
}
