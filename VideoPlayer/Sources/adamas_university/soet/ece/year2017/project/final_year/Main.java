package adamas_university.soet.ece.year2017.project.final_year;

import java.io.FileWriter;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

public class Main extends Application {
	public static PythonExecutor python_executor;
	public static TempFileReader temp_file_reader;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
			Scene scene = new Scene(root, 600, 500);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			scene.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					if (event.getClickCount() == 2)
						primaryStage.setFullScreen(true);
				}

			});
			primaryStage.setTitle("GCV Player");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop() {
		try {
			FileWriter file_writer = new FileWriter("__temp__", true);
			file_writer.write((int)('X'));
			file_writer.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);

		try {
			python_executor.kill();
			python_executor.join();
			temp_file_reader.join();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		try {
			FileWriter file_writer = new FileWriter("__temp__");
			file_writer.write((int)('0'));
			file_writer.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}