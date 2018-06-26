package com.codecool.noname.lancomunicator.client;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientImpl implements Client {


    private final AudioFormat format;
    private final SourceDataLine speaker;
    private Socket socket;
    private boolean listening = true;


    public ClientImpl(String hostname, int port) throws IOException, LineUnavailableException {
        this.socket = new Socket(hostname, port);
        this.format = getAudioFormat();
        DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class, format);
        this.speaker = (SourceDataLine) AudioSystem.getLine(speakerInfo);


    }

    @Override
    public void startListening() throws LineUnavailableException, IOException {

        InputStream inputStream = socket.getInputStream();
        speaker.open(format);
        speaker.start();

        while (listening) {
            byte[] data = new byte[1024];
            inputStream.read(data);
            try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
                 AudioInputStream ais = new AudioInputStream(bais, format, data.length)) {
                int bytesRead = 0;
                if ((bytesRead = ais.read(data)) != -1) {
                    speaker.write(data, 0, bytesRead);
                }
            }
        }
    }


    private AudioFormat getAudioFormat() {
        float sampleRate = 16000.0F;
        int sampleSizeBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = true;

        return new AudioFormat(sampleRate, sampleSizeBits, channels, signed, bigEndian);
    }


}
