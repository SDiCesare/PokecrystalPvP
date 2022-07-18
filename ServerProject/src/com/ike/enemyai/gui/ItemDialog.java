package com.ike.enemyai.gui;

import com.ike.enemyai.pokemon.EnemyMon;
import com.ike.enemyai.pokemon.EnemyTrainer;
import com.ike.enemyai.util.ResourceHandler;

import javax.swing.*;

public class ItemDialog extends JDialog {

    private EnemyTrainer trainer;
    private boolean shouldSwitch;
    private JComboBox<String> box;

    public ItemDialog(EnemyTrainer trainer, JFrame frame) {
        super(frame, "Which Item use?");
        this.trainer = trainer;
        this.setModal(true);
        String[] items = new String[2];
        items[0] = "1) " + ResourceHandler.getItemName(trainer.getItem1());
        items[1] = "2) " + ResourceHandler.getItemName(trainer.getItem2());
        this.setLayout(null);
        this.setSize(250, 150);
        box = new JComboBox<>(items);
        box.addActionListener((e) -> {
            System.out.println(box.getSelectedIndex());
        });
        box.setBounds(10, 10, 190, 30);
        this.add(box);
        JButton saveButton = new JButton("Use Item");
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
        int item = selectedIndex == 0 ? trainer.getItem1() : trainer.getItem2();
        if (item == 0) {
            System.out.println("Can't Use an invalid item");
            return -1;
        }
        return shouldSwitch ? (11 + selectedIndex) : -1;
    }

}
