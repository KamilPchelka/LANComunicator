package com.codecool.noname.lancomunicator;

public class App {


    public static void main(String[] args) {


        try {
            String mode = args[1];

            if (mode.equalsIgnoreCase("server")) {
                int port = Integer.valueOf(args[2]);
                new Server(port).startBroadcasting();
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
