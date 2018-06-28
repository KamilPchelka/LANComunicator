package com.codecool.noname.lancomunicator.server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerImpl implements Server {
    private final DatagramSocket audioServerSocket;
    private final List<InetAddress> clients = new CopyOnWriteArrayList<>();

    public ServerImpl() throws IOException {
        this.audioServerSocket = new DatagramSocket(0);
        new ConnectionRequestHanlder().startListening();
        startBroadcasting();
    }


    public void startBroadcasting() {
        /*new AudioBroadcastHandler(audioServerSocket, clients).runBroadcast();
        new VideoBroadcastHandler(clients).runBroadcast();*/

    }

    private class ConnectionRequestHanlder {


        public void startListening() throws IOException {
            try (ServerSocket serverSocket = new ServerSocket(9003)) {

                while (true) {
                    handleRequest(serverSocket.accept());
                }

            }
        }

        private void handleRequest(Socket accept) {
            new Thread(() -> {
                InetAddress inetAddress = accept.getInetAddress();
                clients.add(accept.getInetAddress());
                System.out.printf("Dodano nowego clienta %s:%d \n", inetAddress, accept.getPort());
            });
        }
    }






}
