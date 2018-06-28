package com.codecool.noname.lancomunicator.client;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.Socket;

public class ClientImpl implements Client {


    private final AudioFormat format = AudioPlayerImpl.getAudioFormat();
    private final AudioPlayer audioPlayer = new AudioPlayerImpl();
    private MulticastSocket socket;
    private Socket socketTCP;
    private boolean listening = true;


    public ClientImpl(String hostname) throws IOException, InterruptedException {
        this.socket = new MulticastSocket(9001);
        connectToServer(hostname);
    }

    @Override
    public void startListening() {


        new Thread(() -> {
            byte[] data = new byte[4096];

            while (listening) {
                DatagramPacket receivePacket = new DatagramPacket(data, data.length);
                try {
                    socket.receive(receivePacket);
                    try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
                         AudioInputStream ais = new AudioInputStream(bais, format, data.length)) {
                        int bytesRead = ais.read(data);
                        if (bytesRead != -1) {
                            new Thread(() -> audioPlayer.play(data)).start();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void connectToServer(String hostname) throws IOException, InterruptedException {
        boolean connected = false;
        int tries = 0;
        while (!connected) {
            try {
                tries++;
                socketTCP = new Socket(hostname, 9003);
                DataOutputStream out = new DataOutputStream(socketTCP.getOutputStream());
                out.write(1);
                connected = true;
            } catch (ConnectException e) {
                System.err.println("(" + tries + ") Please run the server, next try within 2 seconds");
                Thread.sleep(2000);
            }
        }
    }
}
