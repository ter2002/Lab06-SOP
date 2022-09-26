package com.example.lab1502;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WizardService {
    @Autowired
    private WizardRepository repository;

    public WizardService(WizardRepository repository) {
        this.repository = repository;
    }
    public List<Wizard> retrieveWizards(){
        return repository.findAll();
    }

    public Wizard insertOneWizard(Wizard wizard){
       return repository.save(wizard);
    }
    public Wizard updateWizard(Wizard wizard){
        return repository.save(wizard);
    }

    public Wizard retrieveWizardByName(String name){
        return repository.findByName(name);
    }

    public boolean deleteWizard(Wizard wizard){
        try {
            repository.delete(wizard);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public int countWizard(){
        return (int) repository.count();
    }
}
