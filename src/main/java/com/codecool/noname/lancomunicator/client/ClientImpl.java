package com.codecool.noname.lancomunicator.client;

import com.codecool.noname.lancomunicator.server.ServerImpl;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientImpl implements Client {


    private final AudioFormat format;
    private final SourceDataLine speaker;
    private final InetAddress inetAddress;
    DatagramSocket socket;
    private boolean listening = true;


    public ClientImpl(String hostname, int port) throws IOException, LineUnavailableException {
        this.socket = new DatagramSocket(port);
        this.inetAddress = InetAddress.getByName(hostname);
        this.format = ServerImpl.getAudioFormat();
        DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class, format);
        this.speaker = (SourceDataLine) AudioSystem.getLine(speakerInfo);


    }

    @Override
    public void startListening() throws LineUnavailableException, IOException {

        speaker.open(format);
        speaker.start();

        while (listening) {
            byte[] data = new byte[12000];
            DatagramPacket receivePacket = new DatagramPacket(data, data.length);
            socket.receive(receivePacket);
            try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
                 AudioInputStream ais = new AudioInputStream(bais, format, data.length)) {
                int bytesRead = 0;
                if ((bytesRead = ais.read(data)) != -1) {
                    speaker.write(data, 0, bytesRead);
                }
            }
        }
    }

}
