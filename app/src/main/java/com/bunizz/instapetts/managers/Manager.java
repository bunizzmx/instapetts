package com.bunizz.instapetts.managers;


public abstract class Manager extends ErrorManager {

    Manager sManager;
    public abstract Manager getManagerInstance();
    public Manager getCustomManager() {
        return sManager;
    }
    public void setCustomManager(Manager sManager) {
        this.sManager = sManager;
    }

    //endregion


}
