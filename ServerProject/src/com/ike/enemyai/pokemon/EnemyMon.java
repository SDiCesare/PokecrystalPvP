package com.ike.enemyai.pokemon;

public class EnemyMon extends DataHandler {

    public EnemyMon() {
        super(17);
    }

    public byte getSpecies() {
        return this.data[0];
    }

    public void setSpecies(byte species) {
        this.data[0] = species;
    }

    public byte getItem() {
        return this.data[1];
    }

    public void setItem(byte item) {
        this.data[1] = item;
    }

    public byte getMove(int index) {
        return this.data[2 + index];
    }

    public void setMove(int index, byte move) {
        this.data[2 + index] = move;
    }

    public byte getMovePP(int index) {
        return this.data[6 + index];
    }

    public void setMovePP(int index, byte PP) {
        this.data[6 + index] = PP;
    }

    public byte getLevel() {
        return this.data[10];
    }

    public void setLevel(byte level) {
        this.data[0] = level;
    }

    public short getStatus() {
        return this.getShort(this.data[12], this.data[11]);
    }

    public void setStatus(short status) {
        this.setShort(status, 11);
    }

    public short getHP() {
        return this.getShort(this.data[14], this.data[13]);
    }

    public void setHP(short hp) {
        this.setShort(hp, 13);
    }

    public short getMaxHP() {
        return this.getShort(this.data[16], this.data[15]);
    }

    public void setMaxHP(short maxHP) {
        this.setShort(maxHP, 15);
    }

}
