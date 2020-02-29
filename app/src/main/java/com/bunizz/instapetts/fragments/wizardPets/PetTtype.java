package com.bunizz.instapetts.fragments.wizardPets;

public class PetTtype {

    int id_drawable;
    String name_pet;
    int id;

    public PetTtype() {
    }

    public PetTtype(int id_drawable, String name_pet, int id) {
        this.id_drawable = id_drawable;
        this.name_pet = name_pet;
        this.id = id;
    }

    public int getId_drawable() {
        return id_drawable;
    }

    public void setId_drawable(int id_drawable) {
        this.id_drawable = id_drawable;
    }

    public String getName_pet() {
        return name_pet;
    }

    public void setName_pet(String name_pet) {
        this.name_pet = name_pet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
