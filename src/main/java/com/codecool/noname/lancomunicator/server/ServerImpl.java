package com.codecool.noname.lancomunicator.server;

import javax.sound.sampled.AudioFormat;
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
}
