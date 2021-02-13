package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class FXMLDocumentController implements Initializable{

	private String path;
	private MediaPlayer mediaPlayer;
	@FXML
	private MediaView mediaView;
	@FXML
	private Slider progressBar;
	@FXML
	private Slider volumeSlider;
	@FXML
	private Button playButton;
	
	public void chooseFileMethod(ActionEvent event) {
		FileChooser fileChooser=new FileChooser();
		File file=fileChooser.showOpenDialog(null);
		path=file.toURI().toString();
		
		if(path!=null) {
			Media media=new Media(path);
			mediaPlayer=new MediaPlayer(media);
			mediaView.setMediaPlayer(mediaPlayer);
			
			DoubleProperty width=mediaView.fitWidthProperty();
			DoubleProperty height=mediaView.fitHeightProperty();
			width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
			height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
			
			mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
				@Override
				public void changed(ObservableValue<? extends Duration> observableValue, Duration oldValue, Duration newValue) {
					progressBar.setValue(newValue.toSeconds());	
				}
			});
			
			mediaPlayer.setOnReady(new Runnable() {
				
				@Override
				public void run() {
					Duration total=media.getDuration();
					progressBar.setMax(total.toSeconds());
				}
			});
			
			progressBar.setOnMousePressed(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
					
				}
			});
			
			progressBar.setOnMouseDragged(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
					
				}
			});
			
			volumeSlider.setValue(mediaPlayer.getVolume()*100);
			volumeSlider.valueProperty().addListener(new InvalidationListener() {
				
				@Override
				public void invalidated(Observable event) {
					mediaPlayer.setVolume(volumeSlider.getValue()/100);
				}
			});
			
			mediaPlayer.play();

		}
	}

	public void pausePlay(ActionEvent event) {
		MediaPlayer.Status status=mediaPlayer.getStatus();
		
		if(status==MediaPlayer.Status.PAUSED) {
			mediaPlayer.play();
			playButton.setText("Pause");
		}else {
			mediaPlayer.pause();
			playButton.setText("Play");
		}
	}
	public void addTime(ActionEvent event) {
		mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(10)));
	}
	public void deleteTime(ActionEvent event) {
		mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(-10)));
	}
	public void prev(ActionEvent event) {
		
	}
	public void next(ActionEvent event) {
	
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}