/*
*   This is a simple snake game learned from zetcode.com java tutorials
*   I've made some improvements in flow and apple spawning.
*   Also I've added that the snake head changes direction, based on
*   direction of movement. Picture of apple taken (and scaled down) from -
*   https://www.logodesignlove.com/images/classic/apple-logo-rob-janoff-01.jpg
*/

package com.company;
import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public Main() {

        // Initializing frame and adding panel (Board) to it.
        add(new Board());

        setResizable(false);
        pack();

        setTitle("Snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
    public static void main(String[] args) {
        // Executing the game aka. creating new JFrame object
        EventQueue.invokeLater(() -> {
            JFrame ex = new Main();
            ex.setVisible(true);
        });


    }
}
