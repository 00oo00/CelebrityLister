package celebrityLister;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

/**
 * FXML Controller class
 *
 * @author _o0
 */
public class EditListController implements Initializable {
     //for communication with backend
    Client client = ClientBuilder.newClient();

    //warning sign
    @FXML
    private Label error;
    //configure table
    @FXML
    private TableView<Person> tableView;
    @FXML
    private TableColumn<Person, String> firstNameColumn;
    @FXML
    private TableColumn<Person, String> lastNameColumn;
    @FXML
    private TableColumn<Person, String> birthdayColumn;
    //for new Person objects
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
     @FXML
    private TextField birthdayTextField;
    //for searchbar
    @FXML
    private TextField searchBar;
  
    //for repopulating tableView after operations
    List<Person> tempPersonList = new ArrayList<>();

    private ObservableList<Person> persons = FXCollections.observableArrayList();
    @FXML
    private Button btn_delete;
    private TextField lastNameTextField1;
    private TextField lastNameTextField2;
    @FXML
    private TextField textField_name;
   
   

    //PUT------------------------------------------------------------------------------------PUT
//    public void changeFirstNameCellEvent(TableColumn.CellEditEvent<Person, String> t) {
//////        Person personSelected = tableView.getSelectionModel().getSelectedItem();
////        ((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setFirstName(t.getNewValue());
////        int id = t.getRowValue().idPrpoerty().get();
////        String newEditedFirstName = t.getNewValue();
////        String nonEditetLastName = t.getRowValue().getLastName();
////
////        changePerson(id,newEditedFirstName,nonEditetLastName);
//    }
//
//    //doubleclick table view and change Person object
//    public void changeLastNameCellEvent(TableColumn.CellEditEvent<Person, String> t) {
//////        Person personSelected = tableView.getSelectionModel().getSelectedItem();
////        ((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLastName(t.getNewValue());
////        int id = t.getRowValue().idPrpoerty().get();
////        String newEditedLastName = t.getNewValue();
////        String nonEditetLastName = t.getRowValue().getLastName();
//    }

//    public void changeBirthdayCellEvent(CellEditEvent edittedCell){
//        Person personSelected = tableView.getSelectionModel().getSelectedItem();
//        personSelected.setBirthday((Date) edittedCell.getNewValue());
//    }
    //send first and last name to backend
    public void changePerson(int id, String firstName, String lastname) {
//        PersonNoProperty updatePerson = new PersonNoProperty();
//        updatePerson.setFirstName(firstName);
//        updatePerson.setLastName(lastname);
//        client.target("http://localhost:8080/CelebrityListerBackend/webapi/persons").path("/" + id)
//                .request()
//                .put(Entity.entity(updatePerson, MediaType.APPLICATION_JSON));
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //columns for the table
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("birthday"));
       
        //load the data
        tableView.itemsProperty().setValue(persons);
//        tableView.setItems(persons);
       
         persons.clear();
        persons.addAll(getPersons());
        tableView.setEditable(true);
//         searchPerson();
        updateColum();
    }
public void searchPerson(){
    FilteredList<Person> filterdPersons = new FilteredList<> (persons, pers->true);
    searchBar.textProperty().addListener(( observable , t1, t2)-> {
       filterdPersons.setPredicate( p -> {
            if(t2 == null || t2.isEmpty()){
                return true;
            }
            String lowCase = t2.toLowerCase();
            return p.getFirsttName().toLowerCase().contains(lowCase);
         
     });    
    });
    SortedList<Person> sortedPerson = new SortedList<>(filterdPersons);
    sortedPerson.comparatorProperty().bind(tableView.comparatorProperty());
    tableView.setItems(sortedPerson);
    
}
    public void updateColum() {
        

    }

    //DELETE------------------------------------------------------------------------DELETE
    @FXML
    public void deleteButtonPushed(ActionEvent event) {
         Person person = (Person) tableView.getSelectionModel().getSelectedItem();
        int indexSelect = tableView.getSelectionModel().getSelectedIndex();
        client = ClientBuilder.newClient();
        client.target("http://localhost:8080/CelebrityListerBackend/webapi/persons")
                .path(Integer.toString(person.getId()))
                .request(MediaType.APPLICATION_JSON)
                .delete();

        getPersons();

    }

    //POST-------------------------------------------------------------------------POST
    @FXML
    public void newPersonButtonPushed(ActionEvent event) throws IOException {
        System.out.println("test add");
        Person p = new Person();
        p.setFirstName(firstNameTextField.getText());
        p.setLastName(lastNameTextField.getText());
        p.setBirhthday(birthdayTextField.getText());
//    
        
        System.out.println("test add2");
        
        client = ClientBuilder.newClient();
        client.target("http://localhost:8080/CelebrityListerBackend/webapi/persons/")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(p), Person.class);
    }

    //GET-----------------------------------------------------------------------------GET
    public List<Person> getPersons() {

        tableView.getItems().clear();
        client = ClientBuilder.newClient();

        List<PersonNoProperty> listOfPersonNoProperty
                = client.target("http://localhost:8080/CelebrityListerBackend/webapi/persons")
                        .request(MediaType.APPLICATION_JSON)
                        .get(new GenericType<List<PersonNoProperty>>() {
                        });

        List<Person> tempListOfPerson = new ArrayList();

        listOfPersonNoProperty.stream().forEach((p)
                -> {
            tempListOfPerson.add(new Person(p.getId(), p.getFirstName(), p.getLastName(), p.getBirthday()));
        });

        tempListOfPerson.stream().forEach((pers) -> {
            persons.add(pers);
            tableView.setItems(persons);

        });
        return tempPersonList;

    }

    //for repopulating tableView after operations
    public List<Person> getTempPersonList() {
        return tempPersonList;
    }

    @FXML
    private void changeFirstNameCellEvent(TableColumn.CellEditEvent<Person, String> event) {
    }

    @FXML
    private void changeLastNameCellEvent(TableColumn.CellEditEvent<Person, String> event) {
    }

    @FXML
    private void changeBirthdayCellEvent(TableColumn.CellEditEvent<Person, String> event) {
    }

   
}
