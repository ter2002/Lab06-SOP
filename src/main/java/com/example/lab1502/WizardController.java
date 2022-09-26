package com.example.lab1502;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WizardController {
    @Autowired
    private WizardService wizardService;

    @RequestMapping(value = "/wizards", method = RequestMethod.GET)
    public ResponseEntity<?> getData(){
        List<Wizard> wizards = wizardService.retrieveWizards();
        return ResponseEntity.ok(wizards);
    }

    @RequestMapping(value = "/addWizard", method = RequestMethod.POST)
    public void addWizard(@RequestBody Wizard wizard){
        wizardService.insertOneWizard(wizard);
    }

    @RequestMapping(value = "/updateWizard", method = RequestMethod.POST)
    public void updateWizard(@RequestBody Wizard wizard){
            wizardService.updateWizard(wizard);


    }
    @RequestMapping(value = "/deleteWizard", method = RequestMethod.POST)
    public boolean deleteWizard(@RequestBody String name){
        Wizard wizard = wizardService.retrieveWizardByName(name);
        boolean status = wizardService.deleteWizard(wizard);
        return  status;
    }
}
