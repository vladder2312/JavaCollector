package sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.*;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {
    /**
     * @author Vladislav
     * @version 1.2.3
     * Программный продукт, который собирает .java файлы в один файл внутри папки.
     */
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button in_button;

    @FXML
    private Button out_button;

    @FXML
    private Button done_button;

    @FXML
    private TextField in_text_field;

    @FXML
    private TextField out_text_field;

    @FXML
    private ListView<File> file_list;

    @FXML
    private Label label;

    private ArrayList<File> files;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        in_button.setOnMouseClicked(mouseEvent -> {
            File directory = openDirectoryDialog();
            in_text_field.setText(directory != null ? directory.getAbsolutePath() : "");
            if (directory != null) {
                findJavaFiles(directory);
            }
        });
        out_button.setOnMouseClicked(mouseEvent -> {
            File directory = openDirectoryDialog();
            out_text_field.setText(directory != null ? directory.getAbsolutePath() : "");
        });
        done_button.setOnMouseClicked(mouseEvent -> {
            try {
                assemblyFiles(out_text_field.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private File openDirectoryDialog() {
        /**
         * Метод для открытия диалогового окна для выбора папки
         */
        DirectoryChooser directoryChooser = new DirectoryChooser();

        directoryChooser.setTitle("Выберите папку");
        return directoryChooser.showDialog(Main.stage);
    }

    private void findJavaFiles(File directory) {
        /**
         * Метод для получения списка файлов из папки
         * Если найден файл фомата .java вывод списка в listView
         * Если файлов нет с таким расширением, то выводится сообщение об отсутвии файлов
         */
        files = new ArrayList<File>(Arrays.asList(Objects.requireNonNull(directory.listFiles())));
        files.removeIf(f -> !f.getName().contains(".java"));
        if (directory.listFiles() != null) {
            file_list.setItems(FXCollections.observableArrayList(files));
        } else {
            label.setText("Директория не содержит файлов");
        }
    }

    private void assemblyFiles(String to) throws IOException {
        /**
         * Метод для соединения файлов в один *Необходимо создать iterator для обращения к символам, BufferedReader для чтения файла, FileWriter для записи * Новый файл помещается в выбранную пользователем папку с назвнаием new.java * Если не удалось создать файл,пользователь увидит уведомление
         */
        Iterator<File> iterator;
        BufferedReader reader;
        FileWriter writer;
        File file = new File(to + "/new.java");
        String line;
        if (!file.createNewFile()) {
            label.setText("Не удалось создать файл");
            return;
        }
        writer = new FileWriter(file);
        iterator = files.iterator();
        while (iterator.hasNext()) {
            File current = iterator.next();
            try {
                reader = new BufferedReader(new FileReader(current));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                break;
            }
            while ((line = reader.readLine()) != null) {
                writer.write(line + "\n");
            }
            writer.flush();
            label.setText("Успешно");
        }
    }
}