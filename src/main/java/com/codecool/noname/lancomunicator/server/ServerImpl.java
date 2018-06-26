package com.codecool.noname.lancomunicator.server;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerImpl implements Server {
    private final int port;
    private ServerSocket serverSocket;
    Socket server;

    public ServerImpl(int port) {
        this.port = port;
    }

    private static AudioFormat getAudioFormat() {
        float sampleRate = 16000.0F;
        int sampleSizeBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = true;

        return new AudioFormat(sampleRate, sampleSizeBits, channels, signed, bigEndian);
    }

    public void startBroadcasting() throws IOException {
        serverSocket = new ServerSocket(port);
        new broadcastHandler().runBroadcast();

    }


    private class broadcastHandler {
        AudioFormat format = ServerImpl.getAudioFormat();
        DataLine.Info micInfo = new DataLine.Info(TargetDataLine.class, format);
        private boolean outVoice = true;

        public void runBroadcast() {
            try (TargetDataLine mic = (TargetDataLine) AudioSystem.getLine(micInfo)) {
                server = serverSocket.accept();
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                mic.open(format);
                System.out.println("Mic open.");
                byte tmpBuff[] = new byte[mic.getBufferSize() / 5];
                mic.start();
                while (outVoice) {
                    System.out.println("Reading from mic.");
                    int count = mic.read(tmpBuff, 0, tmpBuff.length);
                    if (count > 0) {
                        System.out.println("Writing buffer to server.");
                        out.write(tmpBuff, 0, count);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
