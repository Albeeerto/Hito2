package com.empresa.javafx_mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class ConexionMongo {
    private static final String CONNECTION_STRING = "mongodb+srv://admin:admin@cluster2.unsikqk.mongodb.net/";
    private static final String DATABASE_NAME = "DB_Hito2";

    private MongoClient mongoClient;
    private MongoDatabase database;

    public ConexionMongo() {
        try {
            // Crear una nueva instancia del cliente MongoDB
            mongoClient = MongoClients.create(CONNECTION_STRING);
            // Conectar a la base de datos especificada
            database = mongoClient.getDatabase(DATABASE_NAME);
            System.out.println("Conexión a MongoDB establecida.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    public static void main(String[] args) {
        // Prueba de conexión
        ConexionMongo conexionMongo = new ConexionMongo();
        MongoDatabase db = conexionMongo.getDatabase();
        System.out.println("Nombre de la base de datos: " + db.getName());
        conexionMongo.closeConnection();
    }
}
