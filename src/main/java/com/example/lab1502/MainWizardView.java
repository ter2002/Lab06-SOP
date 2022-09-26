package com.example.lab1502;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.util.ArrayList;
import java.util.List;


//@RestController
@Route(value = "mainPage.it")
public class MainWizardView extends VerticalLayout {
    private TextField fullName, dollars;
    private RadioButtonGroup gender;
    private Select position, school, house;
    private Button previous, next, create, update, delete;
    private Wizards wizards;

    private int index=0;
    private int count;
    boolean check = true;

    public MainWizardView(){
//        this.setResponsiveSteps(new ResponsiveStep("0",5));
        this.wizards = new Wizards();
        dollars = new TextField();
        dollars.setPrefixComponent(new Paragraph("$"));
        position = new Select();
        position.setItems("Student", "Teacher");
        school = new Select();
        school.setItems("Hogwarts","Beauxbatons","Durmstrang");
        house = new Select();
        house.setItems("Gryffindor","Ravenclaw","Hufflepuff","Slyther");
        gender = new RadioButtonGroup();
        gender.setItems("Male","Female");
        fullName = new TextField();
        previous = new Button("<<");
        next = new Button(">>");
        create = new Button("Create");
        update = new Button("Update");
        delete = new Button("Delete");
        fullName.setPlaceholder("Fullname");
        position.setPlaceholder("Positon");
        school.setPlaceholder("School");
        house.setPlaceholder("House");
        HorizontalLayout horiBtn = new HorizontalLayout();
        horiBtn.add(previous,create,update,delete,next);        add(fullName,gender,position,dollars,school,house,horiBtn);
        fetch();
        previous.addClickListener(buttonClickEvent -> {
            if(index <= 0){
                index = count;
            }else {
                index--;
            }
           this.fetch();

        });

        next.addClickListener(buttonClickEvent -> {
            if(index >= count){
                index = 0;

            }
            else {
                index++;
            }

            this.fetch();

        });
        create.addClickListener(buttonClickEvent -> {
            String name = fullName.getValue();
            String gen = gender.getValue().toString();
            String posi = position.getValue().toString();
            int doll = Integer.parseInt(dollars.getValue());
            String scho = school.getValue().toString();
            String hou = house.getValue().toString();

            for (int i=0 ;i<wizards.getModel().size();i++){
                System.out.println(wizards.getModel().get(i).getName()+"==== VS ===="+name);
                if(wizards.getModel().get(i).getName().equals(name)){
                    check = false;
                    System.out.println("Got you");
                }
            }
            if (check){
            WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addWizard")
                    .body(Mono.just(new Wizard(gen, name, scho, hou, doll, posi)), Wizard.class)
                    .retrieve()
                    .bodyToMono(Wizard.class)
                    .block();
            fetch();
                check = true;
            Notification notification = Notification.show("Add to Wizard");
            }else {
                check = true;
                Notification notification = Notification.show("this wizard  is Already");
            }

            System.out.println(check);

        });
        delete.addClickListener(buttonClickEvent -> {
            String name = fullName.getValue();
            WebClient.create()
                    .post()
                    .uri("http://localhost:8080/deleteWizard")
                    .body(Mono.just(name), String.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            index--;
            fetch();
            Notification notification = Notification.show("Delete this Wizard");
        });
        update.addClickListener(buttonClickEvent -> {
            String name = fullName.getValue();
            String gen = gender.getValue().toString();
            String posi = position.getValue().toString();
            int doll = Integer.parseInt(dollars.getValue());
            String scho = school.getValue().toString();
            String hou = house.getValue().toString();
            WebClient.create()
                    .post()
                    .uri("http://localhost:8080/updateWizard")
                    .body(Mono.just(new Wizard(wizards.getModel().get(index).get_id(), gen, name, scho, hou, doll, posi)), Wizard.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            System.out.println(gen);
            fetch();
            System.out.println(gen);
            Notification notification = Notification.show("Update this Wizard");
        });

    }


    public void fetch(){
        List<Wizard> wizardList = WebClient
                .create()
                .get()
                .uri("http://localhost:8080/wizards")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Wizard>>(){})
                .block();
        wizards.setModel(wizardList);
        fullName.setValue(wizards.getModel().get(index).getName());
        gender.setValue(wizards.getModel().get(index).getSex().equals("f")?"Female":"Male");
        position.setValue(wizards.getModel().get(index).getPosition().equals("student")?"Student":"Teacher");
        dollars.setValue(wizards.getModel().get(index).getMoney()+"");
        school.setValue(wizards.getModel().get(index).getSchool());
        house.setValue(wizards.getModel().get(index).getHouse());
        count = wizards.getModel().size()-1;
        System.out.println(count);
    }




}
