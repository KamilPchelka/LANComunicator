package com.codecool.noname.lancomunicator.server;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerImpl implements Server {
    private final int port;
    Socket server;
    private ServerSocket serverSocket;

    public ServerImpl(int port) {
        this.port = port;
    }
}
