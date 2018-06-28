package com.codecool.noname.lancomunicator.server;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerImpl implements Server {
    private final DatagramSocket audioServerSocket;
    private final List<InetAddress> clients = new CopyOnWriteArrayList<>();

    public ServerImpl() throws IOException {
        this.audioServerSocket = new DatagramSocket(0);
        startBroadcasting();
        new ConnectionRequestHanlder().startListening();
    }


    public void startBroadcasting() throws SocketException {
        new AudioBroadcastHandler(audioServerSocket, clients).runBroadcast();
        new VideoBroadcastHandler(clients).runBroadcast();

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
                clients.add(accept.getInetAddress());
                System.out.printf("Dodano nowego clienta %s:%d \n", accept.getInetAddress(), accept.getPort());
            }).start();
        }
    }






}
