package com.bunizz.instapetts.listeners;

public interface RatePetListener {
    void onRate(double rate,String comment,int id_pet,int id_usuario,String uuid);
}
