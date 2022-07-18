package com.ike.enemyai.connection;

public interface DataListener {

    public int listen(byte[] data);

    public void onClose();
}
