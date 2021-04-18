package adamas_university.soet.ece.year2017.project.final_year;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class TempFileReader extends Thread {
	private final int MAIN_COMMAND  = 0;
	private final int PREVIOUS      = 1;
	private final int NEXT          = 2;
	private final int INDEX_COMMAND = 3;
	private final int PLAY_PAUSE    = 4;
	private final int VOLUME_UP     = 5;
	private final int VOLUME_DOWN   = 6;
	private final int FORWARD       = 7;
	private final int BACKWARD      = 8;
    
	private FXMLDocumentController fxml_document_controller;
	
	public TempFileReader(FXMLDocumentController instance) {
		this.fxml_document_controller = instance;
	}
	
	public void run() {
		try {
			try { Thread.sleep(500); } catch(Exception e) {}

			BufferedInputStream reader = new BufferedInputStream(new FileInputStream("__temp__" ));

		    while (true) {
		        if (reader.available() > 0) {
		        	int ch = reader.read();
		        	
		        	if (ch == (int)'\n' || ch == (int)'\r') continue;
		        	if (ch == (int)'X') break;

//		            System.out.println((char)ch);
		            ch -= 48;
		        	switch (ch) {
		        	case MAIN_COMMAND:
		        		break;
		        	case PREVIOUS:
		        		this.fxml_document_controller.playPreviousMedia(null);
		        		break;
		        	case NEXT:
		        		this.fxml_document_controller.playNextMedia(null);
		        		break;
		        	case INDEX_COMMAND:
		        		break;
		        	case PLAY_PAUSE:
		        		this.fxml_document_controller.mediaPlayPause(null);
		        		break;
		        	case VOLUME_UP:
		        		this.fxml_document_controller.setMediaVolume(1.0);
		        		break;
		        	case VOLUME_DOWN:
		        		this.fxml_document_controller.setMediaVolume(-1.0);
		        		break;
		        	case FORWARD:
		        		this.fxml_document_controller.forward10second(null);
		        		break;
		        	case BACKWARD:
		        		this.fxml_document_controller.backward10second(null);
		        		break;
		        	}
		        }
		    }
		    reader.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
