package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class Board extends JPanel implements ActionListener{

    // Size of the game screen.
    private final int BOARD_WIDTH = 400;
    private final int BOARD_HEIGHT = 400;
    // Size of each segment (body, head, apple).
    private final int DOT_SIZE = 10;
    // Number of possible segments to fill the whole map.
    private final int DOTS_POSSIBLE = 1600;
    // Constant needed for apple spawning (to make it possible
    // to spawn in every corner of a screen).
    private final int RANDOM_POSITION = 40;
    // Delay which indicates the speed of the game.
    private final int DELAY = 100;
    private final String MESSAGE = "Game Over!";

    // Two arrays, to represent coordinates of each snake segment.
    private final int x[] = new int[DOTS_POSSIBLE];
    private final int y[] = new int[DOTS_POSSIBLE];


    private int parts;
    private int appleX;
    private int appleY;

    private Timer timer;
    private Image headUp;
    private Image headRight;
    private Image headLeft;
    private Image headDown;
    private Image dot;
    private Image apple;

    private boolean isGame = true;
    // Booleans stating in which direction snake is moving.
    private boolean isLeft = false;
    private boolean isRight = true;
    private boolean isUp = false;
    private boolean isDown = false;


    public Board() {

            // Initializing new panel, with key listener
            // and all necessary items (images, initGame)
            addKeyListener(new TAdapter());
            setBackground(Color.black);
            setFocusable(true);

            setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
            loadImages();
            initGame();


        }



    private void loadImages() {

        // Creates objects for every image used in the program - that is body, apple, and for
        // directions of a snake head.
        ImageIcon iid = new ImageIcon("//Users/mateuszmeller/Desktop/programowanie/Snake/img/dot.png");
        dot = iid.getImage();
        ImageIcon iihu = new ImageIcon("/Users/mateuszmeller/Desktop/programowanie/Snake/img/head_up.png");
        headUp = iihu.getImage();
        ImageIcon iihr = new ImageIcon("/Users/mateuszmeller/Desktop/programowanie/Snake/img/head_right.png");
        headRight = iihr.getImage();
        ImageIcon iihl = new ImageIcon("/Users/mateuszmeller/Desktop/programowanie/Snake/img/head_left.png");
        headLeft = iihl.getImage();
        ImageIcon iihd = new ImageIcon("/Users/mateuszmeller/Desktop/programowanie/Snake/img/head_down.png");
        headDown = iihd.getImage();
        ImageIcon iia = new ImageIcon("/Users/mateuszmeller/Desktop/programowanie/Snake/img/apple.png");
        apple = iia.getImage();

    }


    private void initGame() {

        // The game starts with 3 parts of a body, where 1 part is a head.
        parts = 3;

        // Sets the position of every part of the snake.
        // The starting position is horizontal.
        for (int i = 0; i < parts; i++) {
            x[i] = 50 - i * 10;
            y[i] = 50;
        }

        appleSpawn();

        // creates new timer, which refreshes the screen based on DELAY constant.
        timer = new Timer(DELAY, this);
        timer.start();
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        draw(g);
    }

    private void draw(Graphics g) {

        // The draw method will paint the apple and snake, when called
        // given the game is running.
        if (isGame) {

            g.drawImage(apple, appleX, appleY, this);

            for (int i = 0; i < parts; i++) {

                if (i == 0) {

                    // Based on direction in which snake moves, different image of
                    // head is drawn - facing up when going up etc.
                    if (isUp) {
                        g.drawImage(headUp, x[i], y[i], this);
                    }
                    if (isRight) {
                        g.drawImage(headRight, x[i], y[i], this);
                    }
                    if (isLeft) {
                        g.drawImage(headLeft, x[i], y[i], this);
                    }
                    if (isDown) {
                        g.drawImage(headDown, x[i], y[i], this);
                    }
                } else {
                    // For every other part of the snake "dot" image is drawn.
                    g.drawImage(dot, x[i], y[i], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();
        } else {
            // if isGame == false, print game over message.
            gameOver(g);
        }
    }


    private void appleSpawn() {

        // creates random number between 0 and 1, and multiplies it by 40 (constant) to
        // ensure apple can spawn in any place of a game screen. Later on it multiplies
        // by the size of a dot to make it relevant in a game. Both for x and y coordinate.
        int r = (int) (Math.random() * RANDOM_POSITION);
        appleX = ((r * DOT_SIZE));

        r = (int) (Math.random() * RANDOM_POSITION);
        appleY = ((r * DOT_SIZE));

    }


    private void appleCollision() {

        // If head collides with apple add 1 to part (make snake bigger)
        // and spawn new apple.
        if (x[0] == appleX && y[0] == appleY) {
            parts++;
            appleSpawn();
        }

        // For every other part of the snake, if apple spawns "in" the snake,
        // it spawns again elsewhere.
        for (int i = 1; i < x.length - 1; i++) {
            if (x[i] == appleX && y[i] == appleY) {
                appleSpawn();
            }
        }
    }


    private void gameOver(Graphics g) {

        // When the game ends, a message pops up in a middle of a screen.
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(MESSAGE, (BOARD_WIDTH - metr.stringWidth(MESSAGE)) / 2, BOARD_HEIGHT / 2);

    }

    private void move() {

        // For every part of a snake, the one before replaces the position of a current.
        for (int i = parts; i > 0; i--) {

            x[i] = x[(i - 1)];
            y[i] = y[(i - 1)];
        }

        // "If" statements which change the position of the head (thus the whole snake)
        // based on boolean value of direction.
        if (isLeft) {

            x[0] -= DOT_SIZE;
        }
        if (isRight) {

            x[0] += DOT_SIZE;
        }
        if (isUp) {

            y[0] -= DOT_SIZE;
        }
        if (isDown) {

            y[0] += DOT_SIZE;
        }

    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            // Based on the key pressed the boolean value of direction is changed
            // First however, condition must be met that the snake isn't moving
            // in completely opposite direction.
            // Besides changing direction, each conditional makes sure the snake
            // will "move" only in one direction.
            if ((key == KeyEvent.VK_LEFT) && (!isRight)) {

                isLeft = true;
                isUp = false;
                isDown = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!isLeft)) {

                isRight = true;
                isUp = false;
                isDown = false;
            }

            if ((key == KeyEvent.VK_UP) && (!isDown)) {

                isUp = true;
                isLeft = false;
                isRight = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!isUp)) {

                isDown = true;
                isLeft = false;
                isRight = false;
            }
        }
    }


    private void isCollision() {

        // This conditional check if the snake's head didn't go out of the
        // game bounds. If so, the game is over.
        if (y[0] >= BOARD_HEIGHT) {

            isGame = false;
        }

        if (y[0] < 0) {

            isGame = false;
        }

        if (x[0] >= BOARD_WIDTH) {
            isGame = false;
        }

        if (x[0] < 0) {
            isGame = false;
        }

        // For every other part there is a collision check,
        // whether the head didn't "hit" the body of the snake.
        // Also it checks the condition after such event is possible
        // ie. snake has more than 4 parts.
        for (int i = parts; i > 0; i--) {

            if ((i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
                isGame = false;
            }
        }

        // If the game is lost, the timer stops refreshing the game.
        if (!isGame) {
            timer.stop();
        }
    }

    // Handles the events when the timer "refreshes". If the game is going
    // it calls the given methods and repaints the panel.
    @Override
    public void actionPerformed(ActionEvent e) {

        if (isGame) {

            appleCollision();
            isCollision();
            move();
        }

        repaint();
    }
}


