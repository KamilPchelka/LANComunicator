package com.codecool.noname.lancomunicator.server;

import com.codecool.noname.lancomunicator.client.AudioPlayerImpl;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

public class AudioBroadcastHandler implements Broadcaster {
    private final AudioFormat format = AudioPlayerImpl.getAudioFormat();
    private final DatagramSocket audioServerSocket;
    private final List<InetAddress> clients;

    public AudioBroadcastHandler(DatagramSocket audioServerSocket, List<InetAddress> clients) {
        this.audioServerSocket = audioServerSocket;
        this.clients = clients;
    }


    public void runBroadcast() {
        new Thread(() -> {
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
        }).start();
    }
}