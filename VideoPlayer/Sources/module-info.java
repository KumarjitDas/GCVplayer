module project {
	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.media;
	
	opens adamas_university.soet.ece.year2017.project.final_year to javafx.base, javafx.controls, javafx.fxml, javafx.graphics, javafx.media;
}

//module video_player {
//	requires javafx.base;
//	requires javafx.controls;
//	requires javafx.fxml;
//	requires javafx.graphics;
//	requires javafx.media;
//	
//	opens adamas_university.soet.ece.year2017.project.final_year to javafx.base, javafx.controls, javafx.fxml, javafx.graphics, javafx.media;
//}