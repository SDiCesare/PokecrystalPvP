package com.ike.enemyai.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class BgbClient {

    public static BgbClient waitBgbConnection() {
        try {
            ServerSocket socket = new ServerSocket(8765);
            System.out.println("Waiting Connection on " + socket.getInetAddress().toString());
            Socket accept = socket.accept();
            BgbClient bgbClient = new BgbClient(accept);
            bgbClient.sendHeader();
            System.out.println(bgbClient.client.getInetAddress() + " Connected");
            return bgbClient;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private Socket client;
    private DataListener listener;

    public BgbClient(Socket socket) {
        this.client = socket;
    }

    public BgbClient(String ip, int port) {
        try {
            this.client = new Socket(ip, port);
            sendHeader();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void runClientListener() {
        try {
            while (client.isConnected()) {
                if (hasData()) {
                    byte[] bytes = receiveData();
                    switch (bytes[0]) {
                        case 1:
                        case 108:
                            handleStatus(bytes);
                            break;
                        case 106:
                            syncTransfer(bytes);
                        case 101:
                            break;
                        case 104:
                            handleMasterData(bytes);
                            break;
                        default:
                            System.out.println(Arrays.toString(bytes));
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (listener != null) {
            listener.onClose();
        }
    }

    private void handleMasterData(byte[] data) throws IOException {
        if (listener != null) {
            int toSend = listener.listen(data);
            if (toSend == -1) {
                return;
            }
            sendData(new byte[]{105, (byte) toSend, (byte) 0x80, 0, data[4], data[5], data[6], data[7]});
        }
    }

    private void syncTransfer(byte[] data) throws IOException {
        sendData(new byte[]{106, data[1], data[2], data[3], data[4], data[5], data[6], data[7]});
    }

    private void handleStatus(byte[] bytes) throws IOException {
        boolean running = (bytes[1] & 0b001) == 1;
        boolean paused = (bytes[1] & 0b010) == 2;
        boolean supportReconnect = (bytes[1] & 0b100) == 4;
        //System.out.printf("Status: Running=%b,Paused=%b,CanReconnect=%b\n", running, paused, supportReconnect);
        sendData(new byte[]{108, 1, 0, 0, bytes[4], bytes[5], bytes[6], bytes[7]});
    }

    public void sendHeader() throws IOException {
        // byte[]{112, 11, 107, 101, 99, 114, 121, 115, 116, 97, 108}
        sendData(new byte[]{1, 1, 4, 0, 0, 0, 0, 0});
    }

    public boolean hasData() throws IOException {
        return client.getInputStream().available() > 0;
    }

    public byte[] receiveData() throws IOException {
        InputStream inputStream = client.getInputStream();
        byte[] data = new byte[inputStream.available()];
        inputStream.read(data);
        return data;
    }

    private void sendData(byte[] data) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(data.length);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        for (byte b : data) {
            buffer.put(b);
        }
        OutputStream outputStream = client.getOutputStream();
        outputStream.write(buffer.array());
        //outputStream.write(10);
        outputStream.flush();
    }

    public void setListener(DataListener listener) {
        this.listener = listener;
    }

    public DataListener getListener() {
        return listener;
    }

}
