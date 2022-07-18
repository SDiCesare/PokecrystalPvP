package com.ike.enemyai.pokemon;

public class Trainer extends DataHandler {

    public Trainer() {
        super(5);
    }

    public byte getSpecies() {
        return this.data[0];
    }

    public short getHP() {
        return getShort(this.data[2], this.data[1]);
    }

    public short getMaxHP() {
        return getShort(this.data[4], this.data[3]);
    }

}
