package gui;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.util.logging.LogRecord;

public class TextAreaHandler extends java.util.logging.Handler {

    private TextArea textArea;

    public TextAreaHandler (TextArea textArea){
        this.textArea = textArea;
    }

    @Override
    public void publish(LogRecord record) {
        Platform.runLater(() -> {textArea.appendText(record.getMessage()); textArea.appendText("\n");});
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
