package com.codecool.noname.lancomunicator.server;

import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;

public class VideoBroadcastHandler {

    private final Webcam webcam = Webcam.getDefault();
    private final DatagramSocket socket;
    private final List<InetAddress> clients;

    public VideoBroadcastHandler(List<InetAddress> clients) throws SocketException {
        this.socket = new DatagramSocket(0);
        this.clients = clients;
    }

    public void runBroadcast() {
        webcam.open();

        new Thread(() -> {
            while (true) {
                BufferedImage img = null;
                if ((img = webcam.getImage()) != null) {

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    try {
                        ImageIO.write(img, "png", baos);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    final byte[] imageInByte = baos.toByteArray();
                    int length = imageInByte.length;

                    clients.forEach(address -> {
                        try {
                            socket.send(new DatagramPacket(imageInByte, imageInByte.length, address, 9002));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        }).start();


    }
}
