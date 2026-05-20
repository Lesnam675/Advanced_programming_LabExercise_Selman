package com.aastu.notepadfx;

import com.aastu.notepadfx.controllers.EditorTab;
import com.aastu.notepadfx.utils.FileManager;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.DirectoryChooser;

public class Main extends Application {

    private TabPane tabPane;

    private Label statusLabel;

    private TreeView<File> fileTree;

    @Override
    public void start(Stage stage) {

        tabPane = new TabPane();
        fileTree = new TreeView<>();
        fileTree.setShowRoot(true);

        fileTree.setPrefWidth(250);

        fileTree.setCellFactory(tv -> new TreeCell<>() {

            @Override
            protected void updateItem(
                    File file,
                    boolean empty
            ) {

                super.updateItem(file, empty);

                if (empty || file == null) {

                    setText(null);

                } else {

                    String icon;

                    if (file.isDirectory()) {

                        icon = "📁 ";

                    } else {

                        icon = getFileIcon(file);
                    }

                    setText(icon + file.getName());
                }
            }
        });

        MenuBar menuBar = createMenuBar(stage);

        statusLabel = new Label(
                "Lines: 1 | Characters: 0"
        );

        HBox statusBar = new HBox(statusLabel);

        statusBar.setAlignment(Pos.CENTER_LEFT);

        BorderPane root = new BorderPane();

        root.setTop(menuBar);

        root.setCenter(tabPane);

        root.setBottom(statusBar);

        root.setLeft(fileTree);

        Scene scene = new Scene(root, 1000, 700);

        scene.getStylesheets().add(
                getClass()
                        .getResource("/styles/dark-theme.css")
                        .toExternalForm()
        );

        fileTree.setOnMouseClicked(event -> {

            TreeItem<File> selectedItem =
                    fileTree.getSelectionModel()
                            .getSelectedItem();

            if (selectedItem == null) {
                return;
            }

            File selectedFile =
                    selectedItem.getValue();

            if (selectedFile.isFile()) {

                try {

                    String content =
                            FileManager.readFile(selectedFile);

                    EditorTab tab =
                            new EditorTab(
                                    selectedFile.getName()
                            );

                    tab.getTextArea().setText(content);

                    tab.setCurrentFile(selectedFile);

                    tab.setModified(false);

                    tabPane.getTabs().add(tab);

                    tabPane.getSelectionModel()
                            .select(tab);

                } catch (IOException ex) {

                    ex.printStackTrace();
                }
            }
        });

        createNewTab();

        stage.setTitle("Notepad FX");

        stage.setScene(scene);

        stage.show();
    }

    private String getFileIcon(File file) {

        String name =
                file.getName().toLowerCase();

        if (name.endsWith(".java")) {
            return "☕ ";
        }

        if (name.endsWith(".txt")) {
            return "📄 ";
        }

        if (name.endsWith(".png")
                || name.endsWith(".jpg")) {

            return "🖼 ";
        }

        if (name.endsWith(".mp3")
                || name.endsWith(".wav")) {

            return "🎵 ";
        }

        if (name.endsWith(".mp4")) {
            return "🎬 ";
        }

        if (name.endsWith(".pdf")) {
            return "📕 ";
        }

        return "📄 ";
    }

    private MenuBar createMenuBar(Stage stage) {

        MenuBar menuBar = new MenuBar();

        MenuItem openFolder =
                new MenuItem("Open Folder");

        Menu fileMenu = new Menu("File");

        MenuItem newFile = new MenuItem("New");

        MenuItem openFile = new MenuItem("Open");

        MenuItem saveFile = new MenuItem("Save");

        MenuItem closeTab = new MenuItem("Close Tab");

        MenuItem exitFile = new MenuItem("Exit");

        MenuItem findText = new MenuItem("Find");

        newFile.setAccelerator(
                KeyCombination.keyCombination("Ctrl+N")
        );

        openFile.setAccelerator(
                KeyCombination.keyCombination("Ctrl+O")
        );

        saveFile.setAccelerator(
                KeyCombination.keyCombination("Ctrl+S")
        );

        closeTab.setAccelerator(
                KeyCombination.keyCombination("Ctrl+W")
        );

        findText.setAccelerator(
                KeyCombination.keyCombination("Ctrl+F")
        );

        newFile.setOnAction(e -> createNewTab());

        openFile.setOnAction(e -> openFile(stage));

        saveFile.setOnAction(e -> saveFile(stage));

        closeTab.setOnAction(e -> closeCurrentTab());

        exitFile.setOnAction(e -> stage.close());

        findText.setOnAction(e -> findText());

        openFolder.setOnAction(
                e -> openFolder(stage)
        );

        fileMenu.getItems().addAll(
                newFile,
                openFile,
                openFolder,
                saveFile,
                findText,
                closeTab,
                new SeparatorMenuItem(),
                exitFile
        );

        menuBar.getMenus().add(fileMenu);

        return menuBar;
    }

    private void createNewTab() {

        EditorTab tab = new EditorTab("Untitled");

        tab.getTextArea().textProperty().addListener(
                (obs, oldText, newText) -> updateStatus()
        );

        tabPane.getTabs().add(tab);

        tabPane.getSelectionModel().select(tab);

        updateStatus();
    }

    private EditorTab getCurrentTab() {

        return (EditorTab)
                tabPane.getSelectionModel()
                        .getSelectedItem();
    }

    private void openFile(Stage stage) {

        FileChooser fileChooser = new FileChooser();

        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {

            try {

                String content =
                        FileManager.readFile(file);

                EditorTab tab =
                        new EditorTab(file.getName());

                tab.getTextArea().setText(content);

                tab.setCurrentFile(file);

                tab.setModified(false);

                tab.getTextArea().textProperty().addListener(
                        (obs, oldText, newText) -> updateStatus()
                );

                tabPane.getTabs().add(tab);

                tabPane.getSelectionModel().select(tab);

                updateStatus();

            } catch (IOException ex) {

                ex.printStackTrace();
            }
        }
    }
    private void openFolder(Stage stage) {

        DirectoryChooser directoryChooser =
                new DirectoryChooser();

        File folder =
                directoryChooser.showDialog(stage);

        if (folder != null) {

            TreeItem<File> rootItem =
                    createFileTree(folder);

            fileTree.setRoot(rootItem);
        }
    }
    private TreeItem<File> createFileTree(File file) {

        TreeItem<File> item =
                new TreeItem<>(file);

        item.setExpanded(true);

        if (file.isDirectory()) {

            File[] files = file.listFiles();

            if (files != null) {

                for (File child : files) {

                    item.getChildren().add(
                            createFileTree(child)
                    );
                }
            }
        }

        return item;
    }

    private void saveFile(Stage stage) {

        EditorTab currentTab = getCurrentTab();

        if (currentTab == null) {
            return;
        }

        try {

            if (currentTab.getCurrentFile() == null) {

                FileChooser fileChooser =
                        new FileChooser();

                File file =
                        fileChooser.showSaveDialog(stage);

                currentTab.setCurrentFile(file);
            }

            if (currentTab.getCurrentFile() != null) {

                FileManager.writeFile(
                        currentTab.getCurrentFile(),
                        currentTab.getTextArea().getText()
                );

                currentTab.setModified(false);
            }

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }

    private void findText() {

        EditorTab currentTab = getCurrentTab();

        if (currentTab == null) {
            return;
        }

        TextInputDialog dialog =
                new TextInputDialog();

        dialog.setTitle("Find");

        dialog.setHeaderText("Find Text");

        dialog.setContentText("Enter text:");

        Optional<String> result =
                dialog.showAndWait();

        if (result.isEmpty()) {
            return;
        }

        String searchText = result.get();

        String fullText =
                currentTab.getTextArea().getText();

        int index =
                fullText.indexOf(searchText);

        if (index >= 0) {

            currentTab.getTextArea().requestFocus();

            currentTab.getTextArea().selectRange(
                    index,
                    index + searchText.length()
            );

        } else {

            Alert alert = new Alert(
                    Alert.AlertType.INFORMATION
            );

            alert.setTitle("Not Found");

            alert.setHeaderText(null);

            alert.setContentText(
                    "Text not found."
            );

            alert.showAndWait();
        }
    }


    private void closeCurrentTab() {

        EditorTab currentTab = getCurrentTab();

        if (currentTab == null) {
            return;
        }

        if (currentTab.isModified()) {

            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION
            );

            alert.setTitle("Unsaved Changes");

            alert.setHeaderText(
                    "This tab has unsaved changes."
            );

            alert.setContentText(
                    "Close anyway?"
            );

            Optional<ButtonType> result =
                    alert.showAndWait();

            if (result.isEmpty()
                    || result.get() != ButtonType.OK) {

                return;
            }
        }

        tabPane.getTabs().remove(currentTab);

        if (tabPane.getTabs().isEmpty()) {

            createNewTab();
        }
    }

    private void updateStatus() {

        EditorTab currentTab = getCurrentTab();

        if (currentTab == null) {
            return;
        }

        String text =
                currentTab.getTextArea().getText();

        int characters = text.length();

        int lines = text.isEmpty()
                ? 1
                : text.split("\n").length;

        statusLabel.setText(
                "Lines: " + lines +
                        " | Characters: " + characters
        );
    }

    public static void main(String[] args) {
        launch();
    }
}