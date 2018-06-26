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

    private static AudioFormat getAudioFormat() {

        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        float rate = 44100.0f;
        int channels = 2;
        int sampleSize = 16;
        boolean bigEndian = true;

        return new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);
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
            try {
                line = (TargetDataLine) AudioSystem.getLine(info);
                int buffsize = 1024;
                line.open(format);
                line.start();

                int numBytesRead;
                byte[] data = new byte[buffsize];

                addr = InetAddress.getByName("192.168.11.120");
                DatagramSocket socket = new DatagramSocket();
                while (true) {
                    // Read the next chunk of data from the TargetDataLine.
                    numBytesRead = line.read(data, 0, data.length);
                    // Save this chunk of data.
                    dgp = new DatagramPacket(data, data.length, addr, 9000);

                    socket.send(dgp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
