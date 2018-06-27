package com.codecool.noname.lancomunicator.server;

import com.codecool.noname.lancomunicator.utils.ClientFinder;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerImpl implements Server {
    private final DatagramSocket serverSocket;
    private final List<InetAddress> clients = new CopyOnWriteArrayList<>();

    public ServerImpl(int port) throws SocketException {
        this.serverSocket = new DatagramSocket(port);
        clientListUpdater();
    }


    public static AudioFormat getAudioFormat() {

        return new AudioFormat(8000, 16, 1, true, true);
    }

    public void startBroadcasting() {
            new broadcastHandler().runBroadcast();
    }

    public void clientListUpdater() {
        new Thread(() -> {
            while (true) {
                try {
                    clients.clear();
                    clients.addAll(ClientFinder.getAllAdressOverLocalNetwork());
                    Thread.sleep(5000);
                } catch (InterruptedException | UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private class broadcastHandler {
        AudioFormat format = ServerImpl.getAudioFormat();

        public void runBroadcast() {
            try {

                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format);
                line.start();

                final byte[] data = new byte[4096];
                while (true) {
                    line.read(data, 0, data.length);
                    System.out.println(clients);
                    clients.forEach(address -> {
                        try {
                            serverSocket.send(new DatagramPacket(data, data.length, address, 9001));

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

}
