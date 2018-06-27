package com.codecool.noname.lancomunicator.server;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

public class AudioBroadcastHandler {
    private final AudioFormat format = ServerImpl.getAudioFormat();
    private final DatagramSocket audioServerSocket;
    private final List<InetAddress> clients;

    public AudioBroadcastHandler(DatagramSocket audioServerSocket, List<InetAddress> clients) {
        this.audioServerSocket = audioServerSocket;
        this.clients = clients;
    }


    public void runBroadcast() {
        try {

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            final byte[] data = new byte[4096];
            while (true) {
                line.read(data, 0, data.length);
                clients.forEach(address -> {
                    try {
                        audioServerSocket.send(new DatagramPacket(data, data.length, address, 9001));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}