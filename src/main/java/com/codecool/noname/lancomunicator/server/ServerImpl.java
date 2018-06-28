package com.codecool.noname.lancomunicator.server;

import com.codecool.noname.lancomunicator.utils.ClientFinder;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerImpl implements Server {
    private final DatagramSocket audioServerSocket;
    private final List<InetAddress> clients = new CopyOnWriteArrayList<>();

    public ServerImpl() throws SocketException, UnknownHostException {
        this.audioServerSocket = new DatagramSocket(0);
        clients.addAll(ClientFinder.getAllAdressOverLocalNetwork());
    }


    public void startBroadcasting() throws SocketException {
        new AudioBroadcastHandler(audioServerSocket, clients).runBroadcast();
        new VideoBroadcastHandler(clients).runBroadcast();
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
