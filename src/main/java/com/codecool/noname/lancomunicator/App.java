package com.codecool.noname.lancomunicator;

import com.codecool.noname.lancomunicator.client.ClientImpl;
import com.codecool.noname.lancomunicator.gui.ClientGUI;
import com.codecool.noname.lancomunicator.server.ServerImpl;
import javafx.application.Application;

public class App {


    public static void main(String[] args) throws Exception {


        try {
            String mode = args[0];

            if (mode.equalsIgnoreCase("server")) {
                new ServerImpl().startBroadcasting();
            } else if (mode.equalsIgnoreCase("client")) {
                new ClientImpl().startListening();
                Application.launch(ClientGUI.class);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Please provide correct arguments !");
        }


    }
}
