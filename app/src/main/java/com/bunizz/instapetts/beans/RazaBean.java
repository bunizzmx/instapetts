package com.bunizz.instapetts.beans;

public class RazaBean {

    String name_raza_esp;
    String name_raza_eng;
    int id_type_pet;

    public RazaBean() {
    }

    public RazaBean(String name_raza_esp, String name_raza_eng, int id_type_pet) {
        this.name_raza_esp = name_raza_esp;
        this.name_raza_eng = name_raza_eng;
        this.id_type_pet = id_type_pet;
    }

    public String getName_raza_esp() {
        return name_raza_esp;
    }

    public void setName_raza_esp(String name_raza_esp) {
        this.name_raza_esp = name_raza_esp;
    }

    public String getName_raza_eng() {
        return name_raza_eng;
    }

    public void setName_raza_eng(String name_raza_eng) {
        this.name_raza_eng = name_raza_eng;
    }

    public int getId_type_pet() {
        return id_type_pet;
    }

    public void setId_type_pet(int id_type_pet) {
        this.id_type_pet = id_type_pet;
    }
}
