package com.company;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Controller {
    private static final int FIELD_WIDTH = 16;
    private static final int FIELD_HEIGHT = 12;
    private static final int SQUARE_SIZE = 50;
    private static final int SCREEN_WIDTH = SQUARE_SIZE * FIELD_WIDTH;
    private static final int SCREEN_HEIGHT = SQUARE_SIZE * FIELD_HEIGHT;

    private static final String CONFIG_NAME = "rev-the-game.cfg";

    private static final int DELAY = 350;
    private static final int DEFAULT_FPS = 25;
    private static final int DEFAULT_RENDER_DELAY = 100000000 / DEFAULT_FPS;

    private static final Random RANDOM = new Random();

    private static final Cell[][] FIELD = new Cell[FIELD_WIDTH][FIELD_HEIGHT];

    private static final Color CELL_COLOR = Color.GRAY;
    private static final Color ALTERNATIVE_CELL_COLOR = Color.DARK_GRAY;

    private static final Color WHITE_COLOR = new Color(1f, 1f, 1f, 0.75f);
    private static final Color BLACK_COLOR = new Color(0f, 0f, 0f, 0.75f);

    private static final Color SELECTED_COLOR = new Color(0f, 0f, 0f, 0.3f);

    private Point selectedSquare = null;
    private boolean isPlayerTurn = true;
    private boolean isRunning = true;

    private View view;
    private Graphics graphics;
    private int fps;
    private int renderDelay;

    public void setView(View view) {
        this.view = view;
    }

    public void start() {
        Properties config = new Properties();
        File configFile = new File(CONFIG_NAME);

        if (!configFile.exists()) {
            try {
                Path configPath = Path.of(CONFIG_NAME);

                Files.createFile(configPath);
                Files.write(configPath, ("fps=" + DEFAULT_FPS).getBytes());
            } catch (IOException e) {
                return;
            }

            fps = DEFAULT_FPS;
            renderDelay = DEFAULT_RENDER_DELAY;
        } else {
            try (FileInputStream fileInputStream = new FileInputStream(CONFIG_NAME)) {
                config.load(fileInputStream);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            try {
                fps = Integer.parseInt(config.getProperty("fps"));
                renderDelay = 100000000 / fps;
            } catch (RuntimeException e) {
                e.printStackTrace();
                return;
            }
        }
        System.out.println("Started with " + fps + " " + renderDelay);

        view.create(SCREEN_WIDTH, SCREEN_HEIGHT);
        initField();

        new Thread(() -> {
            while (isRunning) {
                render();

                try {
                    TimeUnit.NANOSECONDS.sleep(renderDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initField() {
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_HEIGHT; j++) {
                FIELD[i][j] = Cell.EMPTY;
            }
        }
    }

    private void render() {
        var image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        graphics = image.getGraphics();

        drawBackground();
        drawField();

        if (selectedSquare != null) {
            drawSelected();
        }

        view.setImage(image);
    }

    private void drawField() {
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_HEIGHT; j++) {
                Cell cell = FIELD[i][j];
                if (cell != Cell.EMPTY) {
                    graphics.setColor(cell == Cell.BLACK ? BLACK_COLOR : WHITE_COLOR);
                    graphics.fillOval(vtr(i), vtr(j), SQUARE_SIZE, SQUARE_SIZE);
                }
            }
        }
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
        }
    }

    private void computerTurn() {
        int posX = RANDOM.nextInt(FIELD_WIDTH);
        int posY = RANDOM.nextInt(FIELD_HEIGHT);

        try {
            TimeUnit.MILLISECONDS.sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        FIELD[posX][posY] = Cell.WHITE;
        isPlayerTurn = true;
    }

    public void onMousePress(int button, int mouseX, int mouseY) {
        int posX = rtv(mouseX);
        int posY = rtv(mouseY);

        if (button != 1 || !isPlayerTurn || FIELD[posX][posY] != Cell.EMPTY) {
            return;
        }

        FIELD[posX][posY] = Cell.BLACK;
        isPlayerTurn = false;
        computerTurn();
    }

    public void onKeyPress(int keycode) {
        if (keycode == KeyEvent.VK_ESCAPE) {
            isRunning = false;
            view.close();
        }
    }

    private int vtr(int v) {
        return v * SQUARE_SIZE;
    }

    private int rtv(int r) {
        return r / SQUARE_SIZE;
    }
}
