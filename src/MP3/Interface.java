package MP3;

import javafx.application.Application;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

import static com.sun.javafx.application.PlatformImpl.runLater;
import static java.lang.Math.floor;
import static java.lang.String.format;

/**
 * Created by Feroli on 03/04/15.
 */
public class Interface extends Application {

    //Stage
    Stage mp3Inter;
    BorderPane root;


    //controls
    MediaPlayer mediaPlayer;
    ProgressBar progress;
    Label time;
    Slider slide;
    Slider progressSlider;



    //Other variables
    Duration duration;
    String artistName;
    String titletName;
    String durtName;
    private ChangeListener<Duration> progressChangeListener;





    private final ObservableList<musicItems> meta =
            FXCollections.observableArrayList();
    private TableView<musicItems> musicTable = new TableView<>(meta);

    public static void main(String[] args) {
        Application.launch(args);
    }


    @Override
    public void start(Stage mp3Inter) throws Exception {

     //  this.mp3Inter = mp3Inter;
        root = new BorderPane();

        mp3Inter.setScene(new Scene(root));

        mediaPlayer();

        root.setCenter(playBack());
        root.setTop(titleSection());
        root.setBottom(musicTable());



        mp3Inter.getScene().getStylesheets().add("MP3/style.css");
        mp3Inter.setTitle("MP3 Player");
        mp3Inter.sizeToScene();
        mp3Inter.centerOnScreen();
        mp3Inter.show();

//to print the current height and width of the stage
       // mp3Inter.setX(mp3Inter.getScene().getX() + mp3Inter.getScene().getWidth() / 2 - mp3Inter.getWidth() / 2); //dialog.getWidth() = not NaN
      //  mp3Inter.setY(mp3Inter.getScene().getY() + mp3Inter.getScene().getHeight() / 2 - mp3Inter.getHeight() / 2); //dialog.getHeight() = not NaN

       // System.out.println(mp3Inter.getHeight());
      //  System.out.println(mp3Inter.getWidth());



    }

    /**
     * A method for a the top area of the mp3 player. This includes a two buttons and currently a label
     * @return BorderPane with all the items inside
     */
    public BorderPane titleSection(){

        BorderPane north = new BorderPane();
        north.setPrefWidth(290);

        north.setId("north");

        Insets northPads = new Insets(10,10,10,10);

        Button repeat = new Button();
        repeat.setPadding(northPads);
        repeat.setId("repeat");
        repeat.setOnAction((ActionEvent e) -> {
            mediaPlayer.seek(mediaPlayer.getStartTime());
        });
        north.setLeft(repeat);
        north.setCenter(time());
        time.setPadding(new Insets(10, 0, 25, 0));

        StackPane pane = new StackPane();

        pane.getChildren().addAll(progress(), progressSlider());

        north.setBottom(pane);



        Image shuffling = new Image(getClass().getResourceAsStream("/mp3Images/knob_shuffle_on.png"));

        Button shuffle = new Button();
        shuffle.setGraphic(new ImageView(shuffling));
        shuffle.setId("shuffle");
        shuffle.setPadding(northPads);
        north.setRight(shuffle);

        return north;
    }

    /**
     * method for the middle area of the player. This includes the three buttons (back, play and rewind) and a slider for the volume.
     * @return BorderPane with all the items inside
     */
    public BorderPane playBack(){

        BorderPane center = new BorderPane();
        center.setId("center");

        HBox play = new HBox(20);
        play.setId("horBox");

        Button back = new Button();
        back.setId("back");
        back.setOnAction((ActionEvent e) -> {
            mediaPlayer.seek(mediaPlayer.getCurrentTime().divide(1.5));
        });

        Button forward = new Button();
        forward.setId("forward");
        forward.setOnAction((ActionEvent e) -> {
            mediaPlayer.seek(mediaPlayer.getCurrentTime().multiply(1.5));
        });

        Button playIcon = new Button();
        playIcon.setId("play");
        playIcon.setOnMouseClicked(e -> {


            //  boolean playing = mediaPlayer().getStatus().equals(MediaPlayer.Status.PLAYING);


            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
                System.out.println("The player was " + mediaPlayer.getStatus());

            } else {
                mediaPlayer.play();
                System.out.println("The player was " + mediaPlayer.getStatus());


                artistName = (String) mediaPlayer.getMedia().getMetadata().get("artist");
                titletName = (String) mediaPlayer.getMedia().getMetadata().get("title");
                durtName = String.valueOf(getTime(mediaPlayer.getMedia().getDuration().toSeconds()));

                String endTime = time.getText().toString();

                String[] splitEndTime = endTime.split("\\/");
                String part2 = splitEndTime[1];
                String cutEndTime = part2.substring(0, 5);

                System.out.println(musicTable.getItems());


                if (musicTable.getItems().size() != 0) {


                    if (musicTable.getItems().get(0).getArtist().equals(artistName)) {
                        System.out.println("freedom");
                    } else {

                        System.out.println(musicTable.getItems().equals(artistName));
                        System.out.println(musicTable.getItems().get(0).getArtist());
                        System.out.println(musicTable.getItems().get(0).getTitle());

                    }

                } else meta.addAll(new musicItems(artistName, titletName, cutEndTime));


            }


        });


        play.getChildren().addAll(back, playIcon, forward);
        play.setAlignment(Pos.CENTER);

        center.setTop(play);

        slide = new Slider(0,1,0.5);

        slide.valueProperty().addListener(e -> {
            mediaPlayer.setVolume(slide.getValue());
            System.out.println(slide.getValue());
        });

        HBox sliderBox = new HBox();
        sliderBox.getChildren().add(slide);
        sliderBox.setAlignment(Pos.CENTER);

        center.setCenter(sliderBox);





        return center;
    }

    /**
     * method contains a placeholder for the bottom area of the table
     * @return GridPane with all the items inside
     */
    public static GridPane playList(){

        GridPane playList = new GridPane();
        playList.setId("playList");
        Label num = new Label("01.");
        num.setPadding(new Insets(0, 20, 0, 0));
        playList.add(num, 0, 0);

        Label song = new Label("I am with you - Cameron Diaz");
        song.setPadding(new Insets(0, 10, 0, 10));

        playList.add(song, 1, 0);

        Label time = new Label("00:00");
        time.setPadding(new Insets(0, 0, 0, 20));

        playList.add(time, 2, 0);


        playList.setHgrow(num, Priority.ALWAYS);
        playList.setHgrow(song, Priority.ALWAYS);
        playList.setHgrow(time, Priority.ALWAYS);
        playList.setVgrow(num, Priority.ALWAYS);
        playList.setVgrow(song, Priority.ALWAYS);
        playList.setVgrow(time, Priority.ALWAYS);
        

        playList.setGridLinesVisible(true);


        return playList;
    }

    /**
     * A method which creates a mediaPlayer to allow further control of a music file
     * @return mediaPlayer
     */
    public MediaPlayer mediaPlayer(){

        File filestring = new File("src/music/remember_the_name.mp3");
        Media hit = new Media(filestring.toURI().toString());
        mediaPlayer = new MediaPlayer(hit);

        mediaPlayer.setOnReady(() -> {


            System.out.println(mediaPlayer.getStatus());
            try {
                musicTable();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        return mediaPlayer;
    }

    /**
     * method which creates a table holding the metadata of all songs imported
     * @return TableView musicTable
     * @throws IOException
     */

    public TableView musicTable() throws IOException {


        musicTable = new TableView<>(meta);
        musicTable.setPrefHeight(200);

        musicTable.setId("music-table");

        TableColumn<musicItems,String> artistCol = new TableColumn("Artist");
        artistCol.setCellValueFactory(
                new PropertyValueFactory("artist"));

        TableColumn<musicItems,String> titleCol = new TableColumn("Title");
        titleCol.setCellValueFactory(
                new PropertyValueFactory("title")
        );

        TableColumn<musicItems,String> durCol = new TableColumn("Duration");
        durCol.setCellValueFactory(
                new PropertyValueFactory("duration")
        );




        musicTable.setColumnResizePolicy(musicTable.CONSTRAINED_RESIZE_POLICY);
        musicTable.setItems(meta);
        musicTable.getColumns().addAll(artistCol, titleCol, durCol);
        musicTable.setPlaceholder(new Label("Click play to initialise the player"));



        return musicTable;

    }

    /**
     * method which converts the duration of a song in to a friendlier version
     * @param time the duration of the song
     * @return Time a string which represents the duration of a song
     */
    public String getTime(double time){

        //System.out.println(Math.round(time));

        double seconds = time;
        double modulo = seconds % 60;
        double hours = (seconds - modulo) / 3600;
        double minutes = seconds / 60;

        String[] secondMin = String.valueOf(minutes).split("\\.");
        String part2 = secondMin[1];
        String cut = part2.substring(0,2);

        String Time = "";


        if (hours > 0.5)
        {
            Time = Math.round(hours) + " : "+ Math.round(minutes) + " : "
                    +   Math.round(seconds);
        }
        else if (minutes<10)
        {
            Time = "0"+Math.round(minutes) + " : " + cut;
        }
        return Time;
    }

    /**
     * Constructor for the label which displays the time elapsed
     * @return
     */
    public Label time() {

        time = new Label();
        time.setTextFill(Color.WHITESMOKE);
        time.setPrefWidth(80);

        mediaPlayer.currentTimeProperty().addListener((Observable ov) -> {
            updateValues();
        });

        mediaPlayer.setOnReady(() -> {
            duration = mediaPlayer.getMedia().getDuration();
            updateValues();
        });


        return time;

    }

    /**
     * Method which updates the time elapsed displayed
     */
    protected void updateValues() {
        if (time != null) {
            runLater(() -> {
                Duration currentTime = mediaPlayer.getCurrentTime();
                time.setText(formatTime(currentTime, duration));

            });
        }
    }

    /**
     * Method which changes how the time is displayed on the mp3 player
     * @param elapsed
     * @param duration
     * @return
     */
    private static String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int) floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
                - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int) floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60
                    - durationMinutes * 60;
            if (durationHours > 0) {
                return format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds, durationMinutes,
                        durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return format("%d:%02d:%02d", elapsedHours,
                        elapsedMinutes, elapsedSeconds);
            } else {
                return format("%02d:%02d", elapsedMinutes,
                        elapsedSeconds);
            }
        }
    }

    public ProgressBar progress(){

        progress = new ProgressBar();

        progress.setPrefWidth(300);
        progress.setPadding(new Insets(0,15,0,15));

        progress.setProgress(0);


        progressChangeListener = (observableValue, oldValue, newValue) ->
                progress.setProgress(1.0 * mediaPlayer.getCurrentTime().toMillis() / mediaPlayer.getTotalDuration().toMillis());

       mediaPlayer.currentTimeProperty().addListener(progressChangeListener);

        return  progress;

    }

    //Extremly glitchy at the moment
    public Slider progressSlider(){

        progressSlider = new Slider();
        progressSlider.setId("pro-slider");
        progressSlider.setPrefWidth(300);

        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                progressSlider.setValue(newValue.toSeconds() / 2.6);
            }
        });

        progressSlider.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mediaPlayer.seek(Duration.seconds(progressSlider.getValue()));
            }
        });

        return progressSlider;

    }


}



