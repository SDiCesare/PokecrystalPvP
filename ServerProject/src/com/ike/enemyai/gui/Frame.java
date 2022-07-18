package com.ike.enemyai.gui;

import com.ike.enemyai.connection.BgbClient;
import com.ike.enemyai.connection.DataListener;
import com.ike.enemyai.pokemon.Battle;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Frame extends JFrame implements DataListener {

    private BgbClient client;
    private boolean connected;
    private final BattlePanel battlePanel;

    public Frame() {
        super("Pokemon Crystal AI Controller");
        this.setSize(750, 500);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.battlePanel = new BattlePanel();
        this.add(battlePanel, BorderLayout.CENTER);
        this.battleStatus = BattleStatus.NO_BATTLE;
        new Thread(this::waitConnection).start();
    }

    public void waitConnection() {
        try (ServerSocket socket = new ServerSocket(8765)) {
            this.battlePanel.disconnect();
            this.connected = false;
            System.out.println("Waiting connection on port " + socket.getLocalPort());
            Socket acceptedSocked = socket.accept();
            this.client = new BgbClient(acceptedSocked);
            if (!this.connected) {
                this.client.sendHeader();
                System.out.println(acceptedSocked.getInetAddress() + " Connected");
                this.client.setListener(this);
                this.battlePanel.connect();
                this.connected = true;
                new Thread(() -> this.client.runClientListener()).start();
            } else {
                System.out.println("A Client is Already Connected!");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            this.connected = false;
            waitConnection();
        }
    }

    public enum BattleStatus {
        NO_BATTLE, READING_BATTLE, BATTLE
    }

    private BattleStatus battleStatus;
    private Battle battle;

    @Override
    public int listen(byte[] data) {
        byte code = data[1];
        switch (battleStatus) {
            case NO_BATTLE:
                if (code == 0x01) {
                    battleStatus = BattleStatus.READING_BATTLE;
                    battle = new Battle();
                    return 1;
                } else {
                    System.out.println("Invalid Code for Starting a Battle");
                    return 0xff;
                }
            case READING_BATTLE:
                battle.append(code);
                if (battle.doneLoading()) {
                    System.out.println("Battle Loaded");
                    battleStatus = BattleStatus.BATTLE;
                    battlePanel.startBattle(battle);
                    System.out.println(battle);
                }
                return battle.getMode().ordinal();
            case BATTLE:
                if (this.battle.isUpdating()) { // Receive Battle Update
                    this.battle.updateBattle(code);
                    if (!this.battle.isUpdating()) {
                        System.out.println("Battle Updated");
                        System.out.println(battle);
                        this.battlePanel.updateBattle();
                    }
                    return 1;
                }
                if (code == 0x08) { // Setup only switch
                    return battlePanel.getForcedSwitch();
                }
                if (code == 0x04) { // Waiting an Action
                    int selectedMove = battlePanel.getSelectedMove();
                    if (selectedMove == -1) {
                        return -128;
                    }
                    System.out.println("Sending Action: " + selectedMove);
                    return selectedMove; // Always Flee
                }
                if (code == 0x02) {
                    this.battle.setUpdating(true);
                    System.out.println("Updating Data");
                    return 1;
                }
                if (code == -128) { // Reset Battle
                    System.out.println("Battle Ended");
                    battle = null;
                    battleStatus = BattleStatus.NO_BATTLE;
                    battlePanel.resetBattle();
                    return 1;
                }
        }
        System.out.println("Invalid Byte " + code);
        return 0xff;
    }

    @Override
    public void onClose() {
        this.connected = false;
        this.client = null;
        this.waitConnection();
    }
}
