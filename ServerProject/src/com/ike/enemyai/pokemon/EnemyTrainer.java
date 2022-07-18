package com.ike.enemyai.pokemon;

public class EnemyTrainer extends DataHandler {

    public static final int INITIAL_DATA = 5;

    private final int partySize;
    private final EnemyMon[] party;

    public EnemyTrainer(int partySize) {
        super(partySize * 17 + INITIAL_DATA);
        System.out.println("Init Enemy Trainer with a party of " + (partySize));
        this.partySize = partySize;
        this.party = new EnemyMon[partySize];
        for (int i = 0; i < partySize; i++) {
            this.party[i] = new EnemyMon();
        }
    }

    @Override
    public void append(byte b) {
        super.append(b);
        if (this.getDataIndex() - 1 >= INITIAL_DATA) {
            for (EnemyMon enemyMon : party) {
                if (enemyMon.doneLoading()) {
                    continue;
                }
                enemyMon.append(b);
                // System.out.println(enemyMon.getSpecies() + ") -> " + b);
                break;
            }
        }
    }

    public byte getItem1() {
        return this.data[3];
    }

    public byte getItem2() {
        return this.data[4];
    }

    public EnemyMon getMon(int index) {
        return this.party[index];
    }

    public EnemyMon getActiveMon() {
        EnemyMon enemyMon = this.party[this.getActiveMonIndex()];
        enemyMon.setHP(this.getShort(this.data[2], this.data[1]));
        return enemyMon;
    }

    public int getActiveMonIndex() {
        return this.data[0];
    }

    public void setActiveMon(int activeMon) {
        this.data[0] = (byte) activeMon;
    }

    public int getPartySize() {
        return partySize;
    }

}
