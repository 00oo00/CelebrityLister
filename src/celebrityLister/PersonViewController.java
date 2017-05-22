/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package celebrityLister;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author _o0
 */
public class PersonViewController implements Initializable {
    
    private Person selectedPerson;
    
    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label birthdayLabel;
    @FXML private Label ageLabel;
    
    //accepts person to initialize the detailed person view
    public void initData (Person person){
        selectedPerson = person;
        firstNameLabel.setText(selectedPerson.getFirstName());
        lastNameLabel.setText(selectedPerson.getLastName());
        birthdayLabel.setText(selectedPerson.getBirthday().toString());
        ageLabel.setText(Integer.toString(selectedPerson.getAge()));
    }

    //Detailed view --> TableView object
    @FXML
      public void changeScreenButtonPushed(ActionEvent event) throws IOException{
          
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("EditList.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        
        //get Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
            window.setScene(tableViewScene);
            window.show();  
        }
    
    /**
     * Initializes the controller class.
     * 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
