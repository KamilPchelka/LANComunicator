package com.codecool.noname.lancomunicator.client;

import com.codecool.noname.lancomunicator.server.ServerImpl;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ClientImpl implements Client {


    private final AudioFormat format;
    private final InetAddress inetAddress;
    private MulticastSocket socket;
    private boolean listening = true;


    public ClientImpl(String hostname, int port) throws IOException {
        this.socket = new MulticastSocket(port);
        this.inetAddress = InetAddress.getByName(hostname);
        this.format = ServerImpl.getAudioFormat();


    }

    @Override
    public void startListening() throws IOException {


        byte[] data = new byte[4096];

        while (listening) {
            DatagramPacket receivePacket = new DatagramPacket(data, data.length);
            socket.receive(receivePacket);
            try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
                 AudioInputStream ais = new AudioInputStream(bais, format, data.length)) {
                int bytesRead = 0;
                if ((bytesRead = ais.read(data)) != -1) {
                    new Thread(() -> toSpeaker(data)).start();
                }
            }
        }
    }

    public void toSpeaker(byte soundbytes[]) {
        try {

            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);

            sourceDataLine.open(format);

            sourceDataLine.start();

            System.out.println("format? :" + sourceDataLine.getFormat());

            sourceDataLine.write(soundbytes, 0, soundbytes.length);
            sourceDataLine.drain();
            sourceDataLine.close();
        } catch (Exception e) {
            System.out.println("Not working in speakers...");
            e.printStackTrace();
        }
    }

}
