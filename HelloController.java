package com.example.javafxbadmanplayer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.scene.control.Button;
import javafx.util.Duration;
import javafx.event.ActionEvent;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.sql.*;
import java.util.*;
import java.awt.*;
import java.util.stream.Collectors;
import javax.swing.*;

public class HelloController implements Initializable
{
    @FXML
    Text songName;
    MediaPlayer player;
    Media media;
    static JComboBox cb;
    String fileToStr;
    File file,file1;
    FileChooser chooser;
    HashSet<String> favouritePlayList;
    Connection con;
    @FXML
    private Slider timeSlider;
    @FXML
    private Slider volSlider;
    @FXML
    MediaView mediaView;
    @FXML
    Button playbtn,backwardBtn,forwardBtn,favBtn,volBtn;


    String fileNameFinder(String s)
    {
        String[] temp1=s.split("\\.");
        String[] temp2=temp1[0].split("/");
        int size=temp2.length;
        size-=1;

        return (temp2[size]);
    }


    @FXML
    void openSongMenu(ActionEvent event) throws MalformedURLException
    {
        try {
            chooser = new FileChooser();
            file = chooser.showOpenDialog(null);   //showOpenDialog return a object of file and it is a static function of class FileChooser that's why we are writing Filechooser obj.showOpenDialogBox
            //null value will display file choser in the center
            fileToStr = file.toURI().toURL().toString();
            String[] temp = fileToStr.split("\\.");
            String temp1 = temp[temp.length - 1];
            //check for audio song
            if ((temp1.equals("mp3")) || (temp1.equals("mp4")))
            {
                if(temp1.equals("mp3")) {}
                media = new Media(fileToStr);    //media take string as input convert file first to url then to string
                //if the song is already playing and you opened another one so first song shoudl shut down
                if (player != null)
                { player.dispose(); }
                player = new MediaPlayer(media);
                mediaView.setMediaPlayer(player);
                player.setOnReady(() -> {    //when mediaplayer will be just ready to play, this lambda function will be called
                    timeSlider.setMin(0); //set 0 as the lowest value
                    timeSlider.setMax(player.getMedia().getDuration().toMinutes());
                    //how to set the max value take the media check its duration and then add to minutes to convert to minutes
                    //if not it will be in millisecondss
                    //set the current value of the slider
                    timeSlider.setValue(0);
                    songName.setText("Playing "+this.fileNameFinder(fileToStr));
                    try {
                        playbtn.setGraphic(new ImageView(new Image(new FileInputStream("src/main/java/icons/play.png"))));
                        volBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/main/java/icons/volume.png"))));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });


                //to update the value of the slider on time change of the video set a listener on the mediaplayer
                //when you set the add Listener on te=he current count property and you enter new in the brackets
                // it will give you suggestion to decide which listener you want and you click that code will be autogenerated


                player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                    @Override
                    public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration t1) {
                        Duration d=player.getCurrentTime();
                        timeSlider.setValue(d.toMinutes());
                    }
                });


                timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        if (timeSlider.isPressed()) {
                            double v = timeSlider.getValue();        //take the slider change
                            player.seek(new Duration(v * 60 * 1000));
                            //set the video duration to that but multiply with 1000 to change minutes to seconds and then to milliseconds

                        }//because timeslider is changing on its own also we have to make when we change it
                    }
                });


                //set the volume slider

                volSlider.setValue(1.0);
                volSlider.valueProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        if(volSlider.isPressed())
                        {
                            double v=volSlider.getValue();
                            player.setVolume(v);
                        }
                    }
                });


            }
        else
        {
            System.out.println("bye");
        }

    }
        catch(Exception err)
        {
            err.printStackTrace();
        }

    }

    void play1()
    {
        try
        {
            if(this.player != null)
            {
                MediaPlayer.Status status =player.getStatus();
                //status is a static function of Mediaplayer class which return status
                if(status == MediaPlayer.Status.PLAYING)
                {
                    player.pause();
                    //playbtn.setText("Play");
                    playbtn.setGraphic(new ImageView(new Image(new FileInputStream("src/main/java/icons/play.png"))));
                }
                else
                {
                    player.play();
                    // playbtn.setText("Pause");
                    playbtn.setGraphic(new ImageView(new Image(new FileInputStream("src/main/java/icons/pause.png"))));
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }


    @FXML
    void play(ActionEvent event)
    {
       this.play1();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {


        //for this method intializable interface is implemented as it run firstmost
        //this will be used to set the icons on Buttons
        //setGraphic wants imageView as attribute image view wants image and image wants either text or fileinputStream
        try
        {
            volSlider.setValue(1.0);
            playbtn.setGraphic(new ImageView(new Image(new FileInputStream("src/main/java/icons/play.png"))));
            backwardBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/main/java/icons/back.png"))));
            forwardBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/main/java/icons/forward.png"))));
            favBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/main/java/icons/fav.png"))));
            volBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/main/java/icons/volume.png"))));


            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/media", "root", "system");
            String sql = "SELECT * FROM Fav";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            favouritePlayList = new HashSet<>();
            while (resultSet.next())
            {
                // Retrieve the values of the columns of the current row and add them to an object or a List
                String entry= (resultSet.getString("songs"));
                favouritePlayList.add(entry);
            }
            preparedStatement.close();
            con.close();
        }
        catch(IOException err)  //as fileinputstream is used so need to catch ioException such as file not found
        {System.out.println(err.getMessage());}
        catch(Exception err) {System.out.println(err.getStackTrace());}

    }

    public void volume(ActionEvent actionEvent)
    {
        try
        {
            if(player.isMute() == true)
            {
                player.setMute(false);
                volBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/main/java/icons/volume.png"))));

            }
            else if(player.isMute() == false)
            {
                player.setMute(true);
                volBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/main/java/icons/mute.png"))));
            }


        }
        catch(Exception err) {err.printStackTrace();}

    }

    public void forwardBtnClick(ActionEvent actionEvent)
    {
       // Duration d=player.getCurrentTime(); //take the current time property it will give in duration object
        double d =player.getCurrentTime().toSeconds(); //give the time in double
        d+=10;
        player.seek(new Duration(d*1000));
    }

    public void backwardBtnClick(ActionEvent actionEvent)
    {
        double d =player.getCurrentTime().toSeconds(); //give the time in double
        d-=10;
        player.seek(new Duration(d*1000));

    }

    public void exitSong(ActionEvent actionEvent)
    {
        if(player!=null)
        {
            player.dispose();
            mediaView.setMediaPlayer(null);
            try
            {
                playbtn.setGraphic(new ImageView(new Image(new FileInputStream("src/main/java/icons/play.png"))));
                volBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/main/java/icons/volume.png"))));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    void removeItemFromFavourites(String file1)
    {
        System.out.println("5");
        favouritePlayList.remove(file1);
        String temp=this.fileNameFinder(file1);
        //shortFavPlayList.remove(temp);
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/media", "root", "system");
            String sql2 = "DELETE FROM Fav WHERE songs = (?)";
            PreparedStatement preparedStatement2 = con2.prepareStatement(sql2);
            preparedStatement2.setString(1, file1);
            int rowsAffected = preparedStatement2.executeUpdate();
            System.out.println(rowsAffected + " rows deleted");
            preparedStatement2.close();
            con2.close();
        }
        catch (Exception err) {err.printStackTrace();}
    }
    void addItemToFavourites(String file1)
    {
        System.out.println("4");
        favouritePlayList.add(file1);
        String temp=this.fileNameFinder(file1);
       // shortFavPlayList.add(temp);
        try
        {
            System.out.println("your file is "+ file1);
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/media", "root", "system");
            String sql2 = "INSERT INTO Fav (songs) VALUES (?)";
            PreparedStatement preparedStatement2 = con.prepareStatement(sql2);
            preparedStatement2.setString(1, fileToStr);
            int rowsAffected = preparedStatement2.executeUpdate();
            System.out.println(rowsAffected + " rows inserted");
            preparedStatement2.close();
            con.close();
        }
        catch (Exception err){err.printStackTrace();}
    }


    public void favBtnClick(ActionEvent actionEvent)
    {
        int i=0;
        if(      (fileToStr != null ) || (!fileToStr.equals(null)) || (!fileToStr.equals(""))      )
        {
            System.out.println("1");
            for(String ts:favouritePlayList)
            {
                if(fileToStr.equals(ts))
                {
                    System.out.println("2");
                    i+=1;
                    //if already present remove it
                    this.removeItemFromFavourites(fileToStr);

                    break;
                }
            }
        }
        //check if not present alreday then add it
        if(i==0)
        {
            System.out.println("3");
            this.addItemToFavourites(fileToStr);
        }
        else {i=0;}
    }

    public void favPlayListMenuClicked(ActionEvent actionEvent)
    {
        try
        {
            if(favouritePlayList!=null)
            {
                int i=0;
                String[] temp=new String[favouritePlayList.size()];
                for(String sdf:favouritePlayList)
                {
                    temp[i]=sdf;
                    i+=1;
                }
                JFrame favMenu=new JFrame("Favourites");
                favMenu.setSize(300,260);
                favMenu.setResizable(false);
                favMenu.setLocationRelativeTo(null);
                favMenu.setLayout(null);
                cb=new JComboBox(temp);
                cb.setBounds(70, 50,150,20);



                JButton exit=new JButton("Exit");
                exit.setBounds(35,100,100,30);
                exit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        favMenu.dispose();
                    }
                });

                JButton delete=new JButton("Delete Song");
                delete.setBounds(160,100,100,30);
                delete.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent e)
                    {
                        String tempo=cb.getSelectedItem().toString();
                        try
                        {
                            Class.forName("com.mysql.cj.jdbc.Driver");
                            Connection con2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/media", "root", "system");
                            String sql2 = "DELETE FROM Fav WHERE songs = (?)";
                            PreparedStatement preparedStatement2 = con2.prepareStatement(sql2);
                            preparedStatement2.setString(1, tempo);
                            int rowsAffected = preparedStatement2.executeUpdate();
                            System.out.println(rowsAffected + " rows deleted");
                            preparedStatement2.close();
                            con2.close();

                            favouritePlayList.remove(tempo);
                        }
                        catch (Exception err)
                        {
                            err.printStackTrace();
                        }

                    }
                });




                favMenu.add(exit);
                favMenu.add(delete);
                favMenu.add(cb);



                favMenu.setVisible(true);
            }
            con.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


//    public void playPlayList(ActionEvent actionEvent)
//    {
//        System.out.println("hello");
//        ArrayList<String> tempList = (ArrayList<String>)favouritePlayList.stream().collect(Collectors.toList());
//        int i=0;
//        while(i != tempList.size()) {
//            media=new Media(tempList.get(i));
//            player=new MediaPlayer(media);
//            mediaView.setMediaPlayer(player);
//            if(player.getCurrentTime().toMinutes() == player.getMedia().getDuration().toMinutes() ) {i+=1;}
//        }
//    }
}