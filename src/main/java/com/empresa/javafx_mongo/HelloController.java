package com.empresa.javafx_mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class HelloController {

    @FXML
    private TextField nombreField;

    @FXML
    private TextField edadField;

    @FXML
    private ComboBox<String> sexoComboBox;

    @FXML
    private TextField alturaField;

    @FXML
    private TextField aficionesField;

    @FXML
    private TextField filterEdadField;

    @FXML
    private TableView<DataModel> tableView;

    @FXML
    private TableColumn<DataModel, String> textColumn;

    @FXML
    private TableColumn<DataModel, String> nombreColumn;

    @FXML
    private TableColumn<DataModel, Integer> edadColumn;

    @FXML
    private TableColumn<DataModel, String> sexoColumn;

    @FXML
    private TableColumn<DataModel, Double> alturaColumn;

    @FXML
    private TableColumn<DataModel, String> aficionesColumn;

    private ObservableList<DataModel> dataList;

    private ConexionMongo conexionMongo;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public HelloController() {
        conexionMongo = new ConexionMongo();
        database = conexionMongo.getDatabase();
        collection = database.getCollection("Hito2");
        dataList = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        textColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        edadColumn.setCellValueFactory(new PropertyValueFactory<>("edad"));
        sexoColumn.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        alturaColumn.setCellValueFactory(new PropertyValueFactory<>("altura"));
        aficionesColumn.setCellValueFactory(new PropertyValueFactory<>("aficiones"));

        tableView.setItems(dataList);
        loadData();

        // Añadir listener para la selección de filas en la tabla
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillFields(newValue);
            }
        });
    }

    @FXML
    protected void handleSaveButtonAction() {
        String nombre = nombreField.getText();
        String edadText = edadField.getText();
        String sexo = sexoComboBox.getValue();
        String alturaText = alturaField.getText().replace(",", ".");
        String aficiones = aficionesField.getText();

        if (nombre == null || nombre.isEmpty() || edadText.isEmpty() || sexo == null || sexo.isEmpty() || alturaText.isEmpty() || aficiones == null || aficiones.isEmpty()) {
            showAlert("Input Error", "Por favor, completa todos los campos.");
            return;
        }

        try {
            int edad = Integer.parseInt(edadText);
            double altura = Double.parseDouble(alturaText);

            Document document = new Document("nombre", nombre)
                    .append("edad", edad)
                    .append("sexo", sexo)
                    .append("altura", altura)
                    .append("aficiones", aficiones);
            collection.insertOne(document);
            showAlert("Success", "Datos guardados en MongoDB.");
            clearFields();
            loadData();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Edad y altura deben ser números válidos.");
        }
    }

    @FXML
    protected void handleDeleteButtonAction() {
        DataModel selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                ObjectId objectId = new ObjectId(selectedItem.getId());
                collection.deleteOne(new Document("_id", objectId));
                showAlert("Success", "Data deleted from MongoDB.");
                loadData();
            } catch (IllegalArgumentException e) {
                showAlert("Error", "Invalid ID format.");
            }
        } else {
            showAlert("Error", "No item selected.");
        }
    }

    @FXML
    protected void handleUpdateButtonAction() {
        loadData();
    }

    @FXML
    protected void handleModifyButtonAction() {
        DataModel selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String nombre = nombreField.getText();
            String edadText = edadField.getText();
            String sexo = sexoComboBox.getValue();
            String alturaText = alturaField.getText().replace(",", ".");
            String aficiones = aficionesField.getText();

            if (nombre == null || nombre.isEmpty() || edadText.isEmpty() || sexo == null || sexo.isEmpty() || alturaText.isEmpty() || aficiones == null || aficiones.isEmpty()) {
                showAlert("Input Error", "Por favor, completa todos los campos.");
                return;
            }

            try {
                int edad = Integer.parseInt(edadText);
                double altura = Double.parseDouble(alturaText);

                Document updatedDocument = new Document("nombre", nombre)
                        .append("edad", edad)
                        .append("sexo", sexo)
                        .append("altura", altura)
                        .append("aficiones", aficiones);

                ObjectId objectId = new ObjectId(selectedItem.getId());
                collection.updateOne(new Document("_id", objectId), new Document("$set", updatedDocument));
                showAlert("Success", "Data updated in MongoDB.");
                clearFields();
                loadData();
            } catch (NumberFormatException e) {
                showAlert("Input Error", "Edad y altura deben ser números válidos.");
            } catch (IllegalArgumentException e) {
                showAlert("Error", "Invalid ID format.");
            }
        } else {
            showAlert("Error", "No item selected.");
        }
    }

    @FXML
    protected void handleFilterButtonAction() {
        String filterEdadText = filterEdadField.getText();
        if (filterEdadText != null && !filterEdadText.isEmpty()) {
            try {
                int filterEdad = Integer.parseInt(filterEdadText);
                loadData(filterEdad);
            } catch (NumberFormatException e) {
                showAlert("Input Error", "La edad debe ser un número entero.");
            }
        } else {
            loadData();
        }
    }

    private void loadData() {
        loadData(null);
    }

    private void loadData(Integer filterEdad) {
        dataList.clear();
        Iterable<Document> documents;
        if (filterEdad != null) {
            documents = collection.find(Filters.eq("edad", filterEdad));
        } else {
            documents = collection.find();
        }
        List<DataModel> dataModels = StreamSupport.stream(documents.spliterator(), false)
                .map(doc -> new DataModel(
                        doc.getObjectId("_id").toString(),
                        doc.getString("nombre"),
                        doc.getInteger("edad"),
                        doc.getString("sexo"),
                        doc.getDouble("altura") != null ? doc.getDouble("altura") : 0.0, // Valor predeterminado 0.0 si es nulo
                        doc.getString("aficiones")
                ))
                .collect(Collectors.toList());
        dataList.addAll(dataModels);
    }

    private void fillFields(DataModel dataModel) {
        nombreField.setText(dataModel.getNombre());
        edadField.setText(String.valueOf(dataModel.getEdad()));
        sexoComboBox.setValue(dataModel.getSexo());
        alturaField.setText(String.valueOf(dataModel.getAltura()));
        aficionesField.setText(dataModel.getAficiones());
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        nombreField.clear();
        edadField.clear();
        sexoComboBox.getSelectionModel().clearSelection();
        alturaField.clear();
        aficionesField.clear();
    }
}
