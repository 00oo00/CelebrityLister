/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package celebrityLister;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author _o0
 */
public class EditListController implements Initializable {
    
    //configure table
    @FXML private TableView<Person> tableView;
    @FXML private TableColumn<Person, String> firstNameColumn;
    @FXML private TableColumn<Person, String> lastNameColumn;
    @FXML private TableColumn<Person, LocalDate> birthdayColumn;
    //for new Person objects
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private DatePicker birthdayDatePicker;
    
    @FXML private Button detailedPersonViewButton;
    
    @FXML private TextField searchBar; 
    
    
    
    //making editing possible in table view
    public void changeFirstNameCellEvent(CellEditEvent edittedCell){
        Person personSelected = tableView.getSelectionModel().getSelectedItem();
        personSelected.setFirstName(edittedCell.getNewValue().toString());
    }
    
    //activates Detailed View button when row(s) selected
    public void userClickedOnTable(){
        this.detailedPersonViewButton.setDisable(false);
    }
            
    //doubleclick table view and change Person object
    public void changeLastNameCellEvent(CellEditEvent edittedCell){
        Person personSelected = tableView.getSelectionModel().getSelectedItem();
        personSelected.setLastName(edittedCell.getNewValue().toString());
    }
    

      
      //passing the selected Person object to the detailed view
      public void changeSceneToDetailedPersonView(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("PersonView.fxml"));
        Parent tableViewParent = loader.load();
        
        Scene tableViewScene = new Scene(tableViewParent);
        
        //access the controller and call a method
        PersonViewController controller = loader.getController();
        controller.initData(tableView.getSelectionModel().getSelectedItem());
        
        //get Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
      }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //columns for the table
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<Person, LocalDate>("birthday"));
        
    
    //load the actual data
    tableView.setItems(getPeople());

    //Allow cell edit of First and Last name
    tableView.setEditable(true);
    firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    
    //selecting multiple rows
    tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);   

    //disable the Detailed Person View button until a row is selected
    this.detailedPersonViewButton.setDisable(true);
    
    searchBar.textProperty().addListener(new ChangeListener(){
        @Override
        public void changed(ObservableValue obervable, Object oldValue, Object newValue){

            if (searchBar.equals(null)) {
                System.out.println("sdvalkjhfasd");
            } else {
               
                 ObservableList<Person> searchList = FXCollections.observableArrayList();
                
                for (int i = 0; i < getPeople().size(); i++) {
                    if (getPeople().get(i).getFirstName().toLowerCase().contains(searchBar.getText().toLowerCase())) {
                        searchList.add(getPeople().get(i));
                        
                        //columns for the table
                        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
                        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
                        birthdayColumn.setCellValueFactory(new PropertyValueFactory<Person, LocalDate>("birthday"));

                    }
                }
                //clear temporary searchlist
                tableView.setItems(null);

                //load the actual data
//                for (int i = 0; i < searchList.size(); i++) {
                tableView.setItems(searchList);
//                }


            }
            
        }
    });
    
 }
    
    
    //remove selected row(s)
    public void deleteButtonPushed(){
        ObservableList<Person> selectedRows, allPeople;
        allPeople = tableView.getItems();
        //gets selected rows
        selectedRows = tableView.getSelectionModel().getSelectedItems();
        //loop selected rows and remove selected Person objects from table
        for(Person person: selectedRows){
            allPeople.remove(person);
        }        
    }
    
    //creating new Person objects
    public void newPersonButtonPushed(){
        Person newPerson = new Person(firstNameTextField.getText(),
                                      lastNameTextField.getText(),
                                      birthdayDatePicker.getValue());
        
        //get all items from the table as list, add new Person to that list
        tableView.getItems().add(newPerson);
    }
//return ObservableList of People objects
    public ObservableList<Person> getPeople(){
        ObservableList<Person> people = FXCollections.observableArrayList();
        people.add(new Person("Frank", "Sinatra",LocalDate.of(1915, Month.DECEMBER, 12)));
        people.add(new Person("Rebecca", "Fergusson",LocalDate.of(1986, Month.JULY, 21)));
        people.add(new Person("Mr.", "T",LocalDate.of(1952, Month.MAY, 21)));
        return people;
        
}
    
}
