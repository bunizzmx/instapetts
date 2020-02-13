package com.bunizz.instapetts.beans;

public class PropietaryBean {
    String nombre;
    int numbers_pets;
    String image_propietary;

    public PropietaryBean() {
    }

    public PropietaryBean(String image_propietary,String nombre, int numbers_pets) {
        this.nombre = nombre;
        this.numbers_pets = numbers_pets;
        this.image_propietary = image_propietary;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNumbers_pets() {
        return numbers_pets;
    }

    public void setNumbers_pets(int numbers_pets) {
        this.numbers_pets = numbers_pets;
    }

    public String getImage_propietary() {
        return image_propietary;
    }

    public void setImage_propietary(String image_propietary) {
        this.image_propietary = image_propietary;
    }
}
