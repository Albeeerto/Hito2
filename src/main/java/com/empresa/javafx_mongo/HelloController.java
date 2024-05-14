package com.empresa.javafx_mongo;

import com.mongodb.Block;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.FindIterable;
import org.bson.Document;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private TableView<Document> tableView;

    @FXML
    private TableColumn<Document, String> nameColumn;

    @FXML
    private TableColumn<Document, String> cityColumn;

    @FXML
    private TableColumn<Document, Integer> ageColumn;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");

        // Conexión a MongoDB Atlas
        MongoClient mongoClient = MongoClients.create("mongodb+srv://Lector:Lector@cluster2.unsikqk.mongodb.net/");
        MongoDatabase database = mongoClient.getDatabase("DB_Javafx");
        MongoCollection<Document> collection = database.getCollection("Javafx");

        // Obtener los documentos de la colección
        FindIterable<Document> documents = collection.find();

        // Limpiar la tabla antes de cargar nuevos datos
        tableView.getItems().clear();

        // Cargar los datos en la tabla
        documents.forEach((Block<? super Document>) document -> {
            tableView.getItems().add(document);
        });

        // Mapear los campos de los documentos a las columnas de la tabla
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getString("nombre")));
        ageColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInteger("edad")).asObject());
        cityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getString("ciudad")));

    }
}
