package adamas_university.soet.ece.year2017.project.final_year;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
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

	private List<String> media_file_list;
	private int current_media_file_index;
	private boolean play_button_clicked;
	
	@FXML
	private MediaPlayer media_player;
	@FXML
	private MediaView media_view;
	@FXML
	private Slider progress_bar;
	@FXML
	private Slider volume_slider;
	@FXML
	private Button play_pause_button;
	
	private void playMedia(String media_file_uri) {
		if (media_file_uri != null) {
			Media media = new Media(media_file_uri);
			media_player = new MediaPlayer(media);
			media_view.setMediaPlayer(media_player);

			DoubleProperty width = media_view.fitWidthProperty();
			DoubleProperty height = media_view.fitHeightProperty();
			width.bind(Bindings.selectDouble(media_view.sceneProperty(), "width"));
			height.bind(Bindings.selectDouble(media_view.sceneProperty(), "height"));

			media_player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
				@Override
				public void changed(ObservableValue<? extends Duration> observableValue, Duration oldValue,
						Duration newValue) {
					progress_bar.setValue(newValue.toSeconds());
				}
			});

			media_player.setOnReady(new Runnable() {

				@Override
				public void run() {
					Duration total = media.getDuration();
					progress_bar.setMax(total.toSeconds());
				}
			});

			progress_bar.setOnMousePressed(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					media_player.seek(Duration.seconds(progress_bar.getValue()));

				}
			});

			progress_bar.setOnMouseDragged(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					media_player.seek(Duration.seconds(progress_bar.getValue()));

				}
			});

			volume_slider.setValue(media_player.getVolume() * 100);
			volume_slider.valueProperty().addListener(new InvalidationListener() {

				@Override
				public void invalidated(Observable event) {
					media_player.setVolume(volume_slider.getValue() / 100);
				}
			});
			
			play_button_clicked = true;
			play_pause_button.setText("Pause");

			media_player.play();

		}
	}

	public void chooseMediaFile(ActionEvent event) {
		media_file_list = new ArrayList<String>();
		File mediaFile = new FileChooser().showOpenDialog(null);

		File parentFolder = new File(mediaFile.getParent());
		for (File f: parentFolder.listFiles()) {
			media_file_list.add(f.getAbsolutePath());
		}
		
		current_media_file_index = media_file_list.indexOf(mediaFile.getAbsolutePath());
		
		playMedia(mediaFile.toURI().toString());
	}

	public void mediaPlayPause(ActionEvent event) {
		Platform.runLater(() -> {
			if (media_player != null) {
				MediaPlayer.Status status = media_player.getStatus();

				if (status == MediaPlayer.Status.PAUSED) {
					media_player.play();
				} else {
					media_player.pause();
				}
				
				if (play_button_clicked) {
					play_button_clicked = false;
					play_pause_button.setText("Play");
				} else {
					play_button_clicked = true;
					play_pause_button.setText("Pause");
				}
			}
		});
	}

	public void forward10second(ActionEvent event) {
		Platform.runLater(() -> {
			if (media_player != null) {
				media_player.seek(media_player.getCurrentTime().add(Duration.seconds(10)));			
			}
		});
	}

	public void backward10second(ActionEvent event) {
		Platform.runLater(() -> {
			if (media_player != null) {
				media_player.seek(media_player.getCurrentTime().add(Duration.seconds(-10)));			
			}
		});
	}

	public void playPreviousMedia(ActionEvent event) {
		Platform.runLater(() -> {
			if (media_player != null && media_file_list.size() > 1) {
				current_media_file_index = current_media_file_index <= 0 ?
										media_file_list.size() - 1 : 
										(current_media_file_index - 1);
				File mediaFile = new File(media_file_list.get(current_media_file_index));
				
				media_player.stop();
				playMedia(mediaFile.toURI().toString());			
			}
		});
	}

	public void playNextMedia(ActionEvent event) {
		Platform.runLater(() -> {
			if (media_player != null && media_file_list.size() > 1) {
				current_media_file_index = (current_media_file_index + 1) >= media_file_list.size() ?
										0 :
										(current_media_file_index + 1);
				File mediaFile = new File(media_file_list.get(current_media_file_index));
				
				media_player.stop();
				playMedia(mediaFile.toURI().toString());			
			}
		});
	}
	
	public void setMediaVolume(double volume) {
		this.volume_slider.setValue(this.volume_slider.getValue() + volume);
		this.media_player.setVolume(this.volume_slider.getValue() / 100);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.play_button_clicked = false;
		
		try {
			Main.python_executor = new PythonExecutor();
			Main.python_executor.start();

			Main.temp_file_reader = new TempFileReader(this);
			Main.temp_file_reader.start();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}