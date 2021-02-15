# GCVplayer
A gesture-controlled video player.

# Project Setup

1. Git clone in a directory and open with Eclipse IDE.

2. Import the JavaFX 11.0.2v library files.
    # Steps to import-
        a. On the Project Explorer  [Folder on which you have cloned the files] -> src -> application.
        b. Right click -> Build Path -> Libraries -> Import JavaFX 11.0.2v library files here save & apply.

3. On Run Configuration -> Arguments -> VM arguments paste the lines- 
    `--module-path "C:\Program Files\Java\javafx-sdk-11.0.2\lib" --add-modules javafx.controls,javafx.fxml,javafx.media` without backticks (`).

4. Provide the root path there the lirary files are downloaded in your PC.

# Download JavaFX Library Files - `https://gluonhq.com/download/javafx-11-0-2-sdk-windows/` and extract in the mentioned folder.