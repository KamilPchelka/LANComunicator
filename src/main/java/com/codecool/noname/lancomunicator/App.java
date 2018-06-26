package com.codecool.noname.lancomunicator;

import com.codecool.noname.lancomunicator.client.ClientImpl;
import com.codecool.noname.lancomunicator.server.ServerImpl;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

public class App {


    public static void main(String[] args) throws IOException, LineUnavailableException {


        try {
            String mode = args[1];

            if (mode.equalsIgnoreCase("server")) {
                int port = Integer.valueOf(args[2]);
                new ServerImpl(port).startBroadcasting();
            } else if (mode.equalsIgnoreCase("client")) {
                String hostname = args[2];
                int port = Integer.valueOf(args[3]);
                new ClientImpl(hostname, port).startListening();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Please provide correct arguments !");
        }


    }
}
