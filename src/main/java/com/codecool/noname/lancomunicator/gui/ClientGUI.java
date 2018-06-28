package com.codecool.noname.lancomunicator.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.concurrent.atomic.AtomicReference;

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

        imageView.imageProperty().bind(imageProperty);

        startWebCamStream();
    }


    private void startWebCamStream() {

        boolean stopCamera = false;
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {

                final AtomicReference<WritableImage> ref = new AtomicReference<>();
                byte[] buffer = new byte[50000];
                MulticastSocket multicastSocket = new MulticastSocket(9002);
                while (!stopCamera) {
                    try {
                        DatagramPacket dp = new DatagramPacket(buffer, buffer.length);

                        multicastSocket.receive(dp);
                        InputStream in = new ByteArrayInputStream(buffer);
                        BufferedImage bufferedImage = ImageIO.read(in);

                        DataBuffer dataBuffer = bufferedImage.getData().getDataBuffer();

                        long sizeBytes = ((long) dataBuffer.getSize()) * 4l;

                        ref.set(SwingFXUtils.toFXImage(bufferedImage, ref.get()));
                        bufferedImage.flush();


                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {
                                imageProperty.set(ref.get());
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                return null;
            }
        };

        Thread th = new Thread(task);
        th.start();
        imageView.imageProperty().bind(imageProperty);

    }
}
