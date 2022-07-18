package com.ike.enemyai.gui;

import com.ike.enemyai.pokemon.Battle;
import com.ike.enemyai.util.ResourceHandler;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("ALL")
public class BattlePanel extends JPanel {

    static {
        UIManager.put("ProgressBar.background", Color.WHITE);
        UIManager.put("ProgressBar.foreground", Color.GREEN);
        UIManager.put("ProgressBar.selectionBackground", Color.BLACK);
        UIManager.put("ProgressBar.selectionForeground", Color.BLACK);
    }

    private JLabel battleStatusLabel;
    private JLabel enemyMonLabel;
    private JLabel trainerMonLabel;
    private JButton[] moveButtons;
    private JButton fleeButton;
    private JButton switchButton;
    private JButton itemButton;
    private JLabel[] movePPButtons;
    private JProgressBar enemyHPBar;
    private JProgressBar trainerHPBar;
    private boolean readyMove;
    private int selectedMove;
    private Battle battle;

    public BattlePanel() {
        super();
        this.setLayout(null);
        this.battleStatusLabel = new JLabel("Waiting Game to Connect");
        this.battleStatusLabel.setFont(this.battleStatusLabel.getFont().deriveFont(20.0f));
        this.battleStatusLabel.setBounds(250, 0, 250, 30);
        this.moveButtons = new JButton[4];
        for (int i = 0; i < this.moveButtons.length; i++) {
            JButton button = new JButton("");
            button.setBackground(Color.LIGHT_GRAY);
            button.setForeground(Color.BLACK);
            button.setEnabled(false);
            int finalI = i;
            button.addActionListener((e) -> {
                if (!readyMove) {
                    selectedMove = finalI;
                    readyMove = true;
                }
            });
            button.setBounds(100, 150 + (50 * i), 200, 30);
            moveButtons[i] = button;
            this.add(moveButtons[i]);
        }
        this.movePPButtons = new JLabel[this.moveButtons.length];
        for (int i = 0; i < this.movePPButtons.length; i++) {
            JLabel label = new JLabel("0");
            label.setBounds(310, 150 + (50 * i), 50, 30);
            label.setForeground(Color.BLACK);
            movePPButtons[i] = label;
            this.add(movePPButtons[i]);
        }
        this.fleeButton = new JButton("Flee");
        this.fleeButton.setBounds(160, 350, 100, 30);
        this.fleeButton.addActionListener((e) -> {
            if (!readyMove) {
                selectedMove = 4;
                readyMove = true;
            }
        });
        this.switchButton = new JButton("Switch");
        this.switchButton.setBounds(50, 350, 100, 30);
        this.switchButton.addActionListener((e) -> {
            SwitchDialog switchDialog = new SwitchDialog(battle.getEnemyTrainer(), ((JFrame) SwingUtilities.getRoot(((Component) e.getSource()))));
            int i = switchDialog.showDialog();
            if (!readyMove && i != -1) {
                selectedMove = 5 + i; // Number from 5 to 10
                this.battle.getEnemyTrainer().setActiveMon(i);
                readyMove = true;
                this.updateBattle();
            }
        });
        this.itemButton = new JButton("Item");
        this.itemButton.setBounds(270, 350, 100, 30);
        this.itemButton.addActionListener((e) -> {
            ItemDialog itemDialog = new ItemDialog(battle.getEnemyTrainer(), ((JFrame) SwingUtilities.getRoot(((Component) e.getSource()))));
            int i = itemDialog.showDialog();
            if (!readyMove && i != -1) {
                selectedMove = i;
                readyMove = true;
                this.updateBattle();
            }
        });
        this.fleeButton.setEnabled(false);
        this.itemButton.setEnabled(false);
        this.switchButton.setEnabled(false);
        this.enemyMonLabel = new JLabel("", SwingConstants.CENTER);
        this.enemyMonLabel.setBounds(100, 50, 200, 30);
        this.enemyMonLabel.setFont(this.battleStatusLabel.getFont());
        this.trainerMonLabel = new JLabel("", SwingConstants.CENTER);
        this.trainerMonLabel.setBounds(450, 50, 200, 30);
        this.trainerMonLabel.setFont(this.battleStatusLabel.getFont());
        this.enemyHPBar = new JProgressBar();
        this.enemyHPBar.setBounds(100, 100, 200, 30);
        this.enemyHPBar.setMinimum(0);
        this.enemyHPBar.setStringPainted(true);
        this.trainerHPBar = new JProgressBar();
        this.trainerHPBar.setBounds(450, 100, 200, 30);
        this.trainerHPBar.setMinimum(0);
        this.trainerHPBar.setStringPainted(true);
        this.add(this.battleStatusLabel);
        this.add(this.enemyMonLabel);
        this.add(this.trainerMonLabel);
        this.add(this.itemButton);
        this.add(this.fleeButton);
        this.add(this.switchButton);
        this.add(this.enemyHPBar);
        this.add(this.trainerHPBar);
    }

    public int getForcedSwitch() {
        ForcedSwitchDialog switchDialog = new ForcedSwitchDialog(battle.getEnemyTrainer(), ((JFrame) SwingUtilities.getRoot(this)));
        return switchDialog.showDialog();
    }

    public void connect() {
        this.battleStatusLabel.setText("Waiting for a Battle");
        this.repaint();
    }

    public void disconnect() {
        this.battleStatusLabel.setText("Waiting Game to Connect");
        this.repaint();
    }

    public void startBattle(Battle battle) {
        this.battle = battle;
        this.battleStatusLabel.setText("In Battle");
        this.updateBattle();
    }

    public void updateBattle() {
        updateHPBars();
        this.switchButton.setEnabled(this.battle.getMode() == Battle.BattleMode.TRAINER_BATTLE);
        this.itemButton.setEnabled(this.battle.getMode() == Battle.BattleMode.TRAINER_BATTLE);
        this.fleeButton.setEnabled(this.battle.getMode() == Battle.BattleMode.WILD_BATTLE);
        this.enemyMonLabel.setText(ResourceHandler.getMonSpecies(battle.getEnemyMonSpecies()));
        this.trainerMonLabel.setText(ResourceHandler.getMonSpecies(battle.getTrainer().getSpecies()));
        for (int i = 0; i < this.moveButtons.length; i++) {
            byte move = battle.getEnemyMonMove(i);
            if (move == 0) {
                this.moveButtons[i].setText("");
                this.moveButtons[i].setEnabled(false);
            } else {
                String moveName = ResourceHandler.getMoveName(move);
                this.moveButtons[i].setText(moveName);
                this.moveButtons[i].setEnabled(true);
            }
        }
        for (int i = 0; i < this.movePPButtons.length; i++) {
            byte movePP = battle.getEnemyMonMovePP(i);
            this.movePPButtons[i].setText(String.valueOf(movePP));
        }
        this.repaint();
    }

    private void updateHPBars() {
        short enemyMonMaxHP = this.battle.getEnemyMonMaxHP();
        this.enemyHPBar.setMaximum(enemyMonMaxHP);
        short enemyMonHP = this.battle.getEnemyMonHP();
        this.enemyHPBar.setValue(enemyMonHP);
        this.enemyHPBar.setString(enemyMonHP + "/" + enemyMonMaxHP);
        short trainerMonMaxHP = this.battle.getTrainerMonMaxHP();
        this.trainerHPBar.setMaximum(trainerMonMaxHP);
        short traineryMonHP = this.battle.getTrainerMonHP();
        this.trainerHPBar.setValue(traineryMonHP);
        this.trainerHPBar.setString(traineryMonHP + "/" + trainerMonMaxHP);
    }

    public void resetBattle() {
        this.battleStatusLabel.setText("Waiting for a Battle");
        this.trainerHPBar.setValue(0);
        this.enemyHPBar.setValue(0);
        this.trainerHPBar.setMaximum(0);
        this.enemyHPBar.setMaximum(0);
        this.trainerHPBar.setString("");
        this.enemyHPBar.setString("");
        this.trainerMonLabel.setText("");
        this.enemyMonLabel.setText("");
        for (JButton move : this.moveButtons) {
            move.setText("");
            move.setEnabled(false);
        }
        for (JLabel movePP : this.movePPButtons) {
            movePP.setText("0");
        }
        this.battle = null;
        this.fleeButton.setEnabled(false);
        this.switchButton.setEnabled(false);
        this.repaint();
    }

    public int getSelectedMove() {
        if (readyMove) {
            readyMove = false;
            return selectedMove;
        }
        return -1;
    }

}
