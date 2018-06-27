package com.codecool.noname.lancomunicator.server;

import com.codecool.noname.lancomunicator.utils.ClientFinder;

import javax.sound.sampled.AudioFormat;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerImpl implements Server {
    private final DatagramSocket audioServerSocket;
    private final List<InetAddress> clients = new CopyOnWriteArrayList<>();

    public ServerImpl(int port) throws SocketException {
        this.audioServerSocket = new DatagramSocket(port);
        clientListUpdater();
    }


    public static AudioFormat getAudioFormat() {

        return new AudioFormat(8000, 16, 1, true, true);
    }

    public void startBroadcasting() {
        new AudioBroadcastHandler(audioServerSocket, clients).runBroadcast();
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



}
