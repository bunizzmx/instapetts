package com.bunizz.instapetts.activitys.wizardPets;

import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.web.parameters.PostActions;
import com.bunizz.instapetts.web.responses.SimpleResponse;

import java.util.ArrayList;

public interface WizardPetContract {
    interface Presenter {
         void newPet(PetBean petBean);

    }

    interface View{
        void petSaved(int id_pet_from_web);
    }
}
