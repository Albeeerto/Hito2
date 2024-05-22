package com.empresa.javafx_mongo;

public class DataModel {
    private String id; // Cambiado de text a id para representar el identificador único
    private String nombre;
    private int edad;
    private String sexo;
    private double altura;
    private String aficiones;

    public DataModel(String id, String nombre, int edad, String sexo, double altura, String aficiones) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.sexo = sexo;
        this.altura = altura;
        this.aficiones = aficiones;
    }

    // Métodos getter y setter para los nuevos campos
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public String getAficiones() {
        return aficiones;
    }

    public void setAficiones(String aficiones) {
        this.aficiones = aficiones;
    }

    // Métodos getter y setter para el campo de identificador único
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
