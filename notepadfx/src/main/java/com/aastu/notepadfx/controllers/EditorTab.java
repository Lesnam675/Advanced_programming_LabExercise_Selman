package com.aastu.notepadfx.controllers;

import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;

import java.io.File;

public class EditorTab extends Tab {

    private final TextArea textArea;

    private File currentFile;

    private boolean modified;

    public EditorTab(String title) {

        setText(title);

        textArea = new TextArea();

        textArea.setWrapText(true);

        setContent(textArea);

        modified = false;

        textArea.textProperty().addListener(
                (obs, oldText, newText) -> {

                    modified = true;

                    updateTitle();
                }
        );
    }

    public TextArea getTextArea() {
        return textArea;
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(File currentFile) {

        this.currentFile = currentFile;

        updateTitle();
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {

        this.modified = modified;

        updateTitle();
    }

    private void updateTitle() {

        String title;

        if (currentFile == null) {

            title = "Untitled";

        } else {

            title = currentFile.getName();
        }

        if (modified) {

            title += " *";
        }

        setText(title);
    }
}