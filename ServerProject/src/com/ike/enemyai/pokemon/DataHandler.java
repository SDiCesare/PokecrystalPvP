package com.ike.enemyai.pokemon;

import java.util.Arrays;

public class DataHandler {

    protected byte[] data;
    private int dataIndex;

    public DataHandler(int size) {
        this.data = new byte[size];
        this.dataIndex = 0;
    }

    public void append(byte b) {
        if (this.doneLoading()) {
            return;
        }
        this.data[this.dataIndex] = b;
        this.dataIndex++;
    }

    public boolean doneLoading() {
        return this.dataIndex >= this.data.length;
    }

    protected int getDataIndex() {
        return dataIndex;
    }

    public short getShort(byte b1, byte b2) {
        return (short) ((b2 & 0xFF) << 8 | b1 & 0xFF);
    }

    public void setShort(short s, int index) {
        this.data[index] = ((byte) ((s >> 8) & 0xff));
        this.data[index + 1] = ((byte) (s & 0xff));
    }

    @Override
    public String toString() {
        return "{doneLoading?=" + doneLoading() + " -> " + Arrays.toString(data) + "}";
    }
}
