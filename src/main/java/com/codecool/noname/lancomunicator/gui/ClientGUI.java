package com.codecool.noname.lancomunicator.gui;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class ClientGUI extends Application {
    ImageView imageView;
    private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();
        imageView = new ImageView();
        pane.getChildren().add(imageView);

        double height = 700;
        double width = 600;

        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
        imageView.prefHeight(height);
        imageView.prefWidth(width);
        imageView.setPreserveRatio(true);


        primaryStage.setScene(new Scene(pane));
        primaryStage.setHeight(700);
        primaryStage.setWidth(600);
        primaryStage.show();

        startWebCamStream();
    }


    private void startWebCamStream() {

        new Thread(() -> {
            try {
                byte[] buffer = new byte[60000];
                MulticastSocket multicastSocket = new MulticastSocket(9002);
                DatagramPacket dp;

                while (true) {
                    dp = new DatagramPacket(buffer, buffer.length);
                    multicastSocket.receive(dp);
                    InputStream in = new ByteArrayInputStream(buffer);
                    BufferedImage bufferedImage = ImageIO.read(in);
                    imageView.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }
}
