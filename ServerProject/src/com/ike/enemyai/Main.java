package com.ike.enemyai;

import com.ike.enemyai.gui.Frame;
import com.ike.enemyai.pokemon.Battle;

import javax.swing.*;
import java.io.PrintStream;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Frame frame = new Frame();
            frame.setVisible(true);
        });
    }

}
