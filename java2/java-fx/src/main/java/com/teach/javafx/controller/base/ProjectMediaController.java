package com.teach.javafx.controller.base;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.net.URL;
import java.util.List;

import static javafx.scene.media.MediaPlayer.Status.PLAYING;

public class ProjectMediaController {
    @FXML
    private MediaView mediaView;
    @FXML
    private Button playBtn;
    @FXML
    private Button stopBtn;

    @FXML
    public void initialize() {
        URL mediaUrl = getClass().getClassLoader().getResource("project.mp4");
        String mediaStringUrl = mediaUrl.toExternalForm();
        Media media = new Media(mediaStringUrl);
        MediaPlayer player = new MediaPlayer(media);
        player.setAutoPlay(true);
        mediaView.setMediaPlayer(player);
        playBtn.setOnAction(e -> {
            if (player.getStatus() == PLAYING) {
                player.stop();
                player.play();
            } else {
                player.play();
            }
        });
        stopBtn.setOnAction(e -> player.stop());
        player.setOnError(() -> System.out.println(player.getError().getMessage()));
    }

}
