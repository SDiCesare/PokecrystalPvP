package com.ike.enemyai.pokemon;

public class WildMon extends DataHandler {

    public WildMon() {
        super(20);
    }

    public byte getSpecies() {
        return data[0];
    }

    public byte getItem() {
        return data[1];
    }

    public short getDV() {
        return getShort(data[7], data[6]);
    }

    public byte getMove(int index) {
        return data[2 + index];
    }

    public byte getMovePP(int index) {
        return data[8 + index];
    }

    public byte getHappiness() {
        return data[12];
    }

    public byte getLevel() {
        return data[13];
    }

    public short getStatus() {
        return getShort(data[15], data[14]);
    }

    public short getHP() {
        return getShort(data[17], data[16]);
    }

    public short getMaxHP() {
        return getShort(data[19], data[18]);
    }


}
