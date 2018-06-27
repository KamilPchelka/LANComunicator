package com.codecool.noname.lancomunicator.server;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class ServerImpl implements Server {
    private final int port;
    private DatagramSocket serverSocket;
    Socket server;

    public ServerImpl(int port) {
        this.port = port;
    }


    public static AudioFormat getAudioFormat() {
        float sampleRate = 8000;
        int sampleSizeBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = true;

        return new AudioFormat(sampleRate, sampleSizeBits, channels, signed, bigEndian);
    }

    public void startBroadcasting() throws IOException {
        serverSocket = new DatagramSocket(port);
        try {
            new broadcastHandler().runBroadcast();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }


    private class broadcastHandler {
        AudioFormat format = ServerImpl.getAudioFormat();
        DataLine.Info micInfo = new DataLine.Info(TargetDataLine.class, format);
        private boolean outVoice = true;
        InetAddress addr;
        DatagramPacket dgp;

        public void runBroadcast() throws LineUnavailableException {
            TargetDataLine line;
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.drain();
            try {
                line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format);
                line.start();

                int numBytesRead;
                byte[] data = new byte[4096];

                addr = InetAddress.getByName("localhost");
                DatagramSocket socket = new DatagramSocket();
                while (true) {
                    System.out.println("xd");
                    // Read the next chunk of data from the TargetDataLine.
                    numBytesRead = line.read(data, 0, data.length);
                    // Save this chunk of data.
                    dgp = new DatagramPacket(data, data.length, addr, 9001);

                    socket.send(dgp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
