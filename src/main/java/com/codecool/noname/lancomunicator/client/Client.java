package com.codecool.noname.lancomunicator.client;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

public interface Client {

    void startListening() throws LineUnavailableException, IOException;

}
