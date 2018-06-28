package com.codecool.noname.lancomunicator.server;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerImpl implements Server {
    private final DatagramSocket audioServerSocket;
    private final List<InetAddress> clients = new CopyOnWriteArrayList<>();

    public ServerImpl() throws SocketException {
        this.audioServerSocket = new DatagramSocket(0);
    }


    public void startBroadcasting() throws SocketException {
        new AudioBroadcastHandler(audioServerSocket, clients).runBroadcast();
        new VideoBroadcastHandler(clients).runBroadcast();
    }




}
