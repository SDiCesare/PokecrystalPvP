package com.ike.enemyai.gui;

import com.ike.enemyai.pokemon.EnemyMon;
import com.ike.enemyai.pokemon.EnemyTrainer;
import com.ike.enemyai.util.ResourceHandler;

import javax.swing.*;
import java.awt.*;

public class SwitchDialog extends JDialog {

    private EnemyTrainer trainer;
    private boolean shouldSwitch;
    private JComboBox<String> box;

    public SwitchDialog(EnemyTrainer trainer, JFrame frame) {
        super(frame, "Which Pokemon Enter?");
        this.trainer = trainer;
        this.setModal(true);
        String[] names = new String[trainer.getPartySize()];
        for (int i = 0; i < names.length; i++) {
            EnemyMon mon = trainer.getMon(i);
            String health = mon.getHP() + "/" + mon.getMaxHP();
            names[i] = (i + 1) + ") " + ResourceHandler.getMonSpecies(mon.getSpecies()) + ": " + health;
        }
        this.setLayout(null);
        this.setSize(250, 150);
        box = new JComboBox<>(names);
        box.addActionListener((e) -> {
            System.out.println(box.getSelectedIndex());
        });
        box.setBounds(10, 10, 190, 30);
        this.add(box);
        JButton saveButton = new JButton("Switch");
        saveButton.addActionListener((e) -> {
            this.shouldSwitch = true;
            this.setVisible(false);
            this.dispose();
        });
        saveButton.setBounds(box.getX(), box.getY() + box.getHeight() + 10, 100, 30);
        JButton deleteButton = new JButton("Cancel");
        deleteButton.addActionListener((e) -> {
            this.setVisible(false);
            this.dispose();
        });
        deleteButton.setBounds(saveButton.getX() + saveButton.getWidth() + 10, saveButton.getY(), saveButton.getWidth(), saveButton.getHeight());
        this.add(saveButton);
        this.add(deleteButton);
        this.setResizable(false);
        this.setLocationRelativeTo(frame);
    }

    public int showDialog() {
        this.setVisible(true);
        int selectedIndex = box.getSelectedIndex();
        if (selectedIndex == trainer.getActiveMonIndex()) {
            System.out.println("Can't Switch With Active Pokemon");
            return -1;
        }
        short hp = trainer.getMon(selectedIndex).getHP();
        if (hp <= 0) {
            System.out.println("Can't Switch With a dead Pokemon");
            return -1;
        }
        return shouldSwitch ? selectedIndex : -1;
    }

}
