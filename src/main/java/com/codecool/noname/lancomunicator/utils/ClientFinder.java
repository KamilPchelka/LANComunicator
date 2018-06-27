package com.codecool.noname.lancomunicator.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ClientFinder {

    public static List<InetAddress> getAllAdressOverLocalNetwork() throws UnknownHostException {
        final List<InetAddress> addresses = new ArrayList<>();
        InetAddress localhost = InetAddress.getByName("192.168.0.00");
        byte[] ip = localhost.getAddress();
        for (int i = 1; i <= 254; i++) {
            ip[3] = (byte) i;
            InetAddress address = InetAddress.getByAddress(ip);
            new Thread(() -> {
                try {
                    if (address.isReachable(2000)) addresses.add(address);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        return addresses;
    }
}
