package com.bunizz.instapetts.beans;

public class PetBean {

    String name_pet;
    String raza_pet;
    int type_pet;

    public PetBean() {
    }

    public PetBean(String name_pet, String raza_pet, int type_pet) {
        this.name_pet = name_pet;
        this.raza_pet = raza_pet;
        this.type_pet = type_pet;
    }

    public String getName_pet() {
        return name_pet;
    }

    public void setName_pet(String name_pet) {
        this.name_pet = name_pet;
    }

    public String getRaza_pet() {
        return raza_pet;
    }

    public void setRaza_pet(String raza_pet) {
        this.raza_pet = raza_pet;
    }

    public int getType_pet() {
        return type_pet;
    }

    public void setType_pet(int type_pet) {
        this.type_pet = type_pet;
    }
}
