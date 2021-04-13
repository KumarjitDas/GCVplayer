package adamas_university.soet.ece.year2017.project.final_year;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

public class FXMLDocumentController implements Initializable {

	private List<String> mediaFileList;
	private int currentMediaFileIndex;
	
	private MediaPlayer mediaPlayer;
	@FXML
	private MediaView mediaView;
	@FXML
	private Slider progressBar;
	@FXML
	private Slider volumeSlider;
	@FXML
	private Button playButton;

	private boolean playButtonClicked = false;
	
	private void playMedia(String mediaFileURI) {
		if (mediaFileURI != null) {
			Media media = new Media(mediaFileURI);
			mediaPlayer = new MediaPlayer(media);
			mediaView.setMediaPlayer(mediaPlayer);

			DoubleProperty width = mediaView.fitWidthProperty();
			DoubleProperty height = mediaView.fitHeightProperty();
			width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
			height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));

			mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
				@Override
				public void changed(ObservableValue<? extends Duration> observableValue, Duration oldValue,
						Duration newValue) {
					progressBar.setValue(newValue.toSeconds());
				}
			});

			mediaPlayer.setOnReady(new Runnable() {

				@Override
				public void run() {
					Duration total = media.getDuration();
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

			volumeSlider.setValue(mediaPlayer.getVolume() * 100);
			volumeSlider.valueProperty().addListener(new InvalidationListener() {

				@Override
				public void invalidated(Observable event) {
					mediaPlayer.setVolume(volumeSlider.getValue() / 100);
				}
			});
			
			playButtonClicked = true;
			playButton.setText("Pause");

			mediaPlayer.play();

		}
	}

	public void chooseFileMethod(ActionEvent event) {
		mediaFileList = new ArrayList<String>();
		File mediaFile = new FileChooser().showOpenDialog(null);

		File parentFolder = new File(mediaFile.getParent());
		for (File f: parentFolder.listFiles()) {
			mediaFileList.add(f.getAbsolutePath());
		}
		
		currentMediaFileIndex = mediaFileList.indexOf(mediaFile.getAbsolutePath());
		
		playMedia(mediaFile.toURI().toString());
	}

	public void pausePlay(ActionEvent event) {
		if (mediaPlayer != null) {
			MediaPlayer.Status status = mediaPlayer.getStatus();

			if (status == MediaPlayer.Status.PAUSED) {
				mediaPlayer.play();
			} else {
				mediaPlayer.pause();
			}
			
			if (playButtonClicked) {
				playButtonClicked = false;
				playButton.setText("Play");
			} else {
				playButtonClicked = true;
				playButton.setText("Pause");
			}
		}
	}

	public void addTime(ActionEvent event) {
		if (mediaPlayer != null) {
			mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(10)));			
		}
	}

	public void deleteTime(ActionEvent event) {
		if (mediaPlayer != null) {
			mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(-10)));			
		}
	}

	public void prev(ActionEvent event) {
		if (mediaPlayer != null && mediaFileList.size() > 1) {
			currentMediaFileIndex = currentMediaFileIndex <= 0 ?
									mediaFileList.size() - 1 : 
									(currentMediaFileIndex - 1);
			File mediaFile = new File(mediaFileList.get(currentMediaFileIndex));
			
			mediaPlayer.stop();
			playMedia(mediaFile.toURI().toString());			
		}
	}

	public void next(ActionEvent event) {
		if (mediaPlayer != null && mediaFileList.size() > 1) {
			currentMediaFileIndex = (currentMediaFileIndex + 1) >= mediaFileList.size() ?
									0 : 
									(currentMediaFileIndex + 1);
			File mediaFile = new File(mediaFileList.get(currentMediaFileIndex));
			
			mediaPlayer.stop();
			playMedia(mediaFile.toURI().toString());			
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

}