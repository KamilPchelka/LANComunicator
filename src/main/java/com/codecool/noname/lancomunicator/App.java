package com.codecool.noname.lancomunicator;

import com.codecool.noname.lancomunicator.client.ClientImpl;
import com.codecool.noname.lancomunicator.server.ServerImpl;

import java.io.IOException;

public class App {


    public static void main(String[] args) throws IOException {


        try {
            String mode = args[0];

            if (mode.equalsIgnoreCase("server")) {
                new ServerImpl().startBroadcasting();
            } else if (mode.equalsIgnoreCase("client")) {
                int port = Integer.valueOf(args[1]);
                new ClientImpl(port).startListening();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Please provide correct arguments !");
        }


    }
}
