package celebrityLister;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * FXML Controller class
 *
 * @author _o0
 */
public class EditListController implements Initializable {

    //configure table
    @FXML
    private TableView<Person> tableView;
    @FXML
    private TableColumn<Person, String> firstNameColumn;
    @FXML
    private TableColumn<Person, String> lastNameColumn;
    @FXML
    private TableColumn<Person, LocalDate> birthdayColumn;
    //for new Person objects
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private DatePicker birthdayDatePicker;
    //for searchbar
    @FXML
    private TextField searchBar;
    //for communication with backend
    Client client = ClientBuilder.newClient();
    //for repopulating tableView after operations
    List<Person> tempPersonList = new ArrayList<>();

    private ObservableList<Person> persons = FXCollections.observableArrayList();

    //PUT------------------------------------------------------------------------------------PUT
    public void changeFirstNameCellEvent(TableColumn.CellEditEvent<Person, String> t) {
//        Person personSelected = tableView.getSelectionModel().getSelectedItem();
        ((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setFirstName(t.getNewValue());
        int id = t.getRowValue().getId().get();
        String newEditedFirstName = t.getNewValue();
        String nonEditetLastName = t.getRowValue().getLastName();

        changePerson(id,newEditedFirstName,nonEditetLastName);
    }

    //doubleclick table view and change Person object
    public void changeLastNameCellEvent(TableColumn.CellEditEvent<Person, String> t) {
//        Person personSelected = tableView.getSelectionModel().getSelectedItem();
        ((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLastName(t.getNewValue());
        int id = t.getRowValue().getId().get();
        String newEditedLastName = t.getNewValue();
        String nonEditetLastName = t.getRowValue().getLastName();
    }
    
//    public void changeBirthdayCellEvent(CellEditEvent edittedCell){
//        Person personSelected = tableView.getSelectionModel().getSelectedItem();
//        personSelected.setBirthday((Date) edittedCell.getNewValue());
//    }

    //send first and last name to backend
    public void changePerson(int id, String firstName, String lastname) {
    PersonNoProperty updatePerson = new PersonNoProperty();
    updatePerson.setFirstName(firstName);
    updatePerson.setLastName(lastname);
    client.target("http://localhost:8080/CelebrityListerBackend/webapi/persons").path("/" + id)
                .request()
                .put(Entity.entity(updatePerson, MediaType.APPLICATION_JSON));
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

        //load the data
        tableView.setItems(persons);
        persons.clear();
        persons.addAll(getPersons());

        //Allow cell edit of First and Last name
        tableView.setEditable(true);
        firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        //selecting multiple rows
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        searchBar.textProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                persons.clear();
                tempPersonList.stream()
                        .filter(t-> t.getFirstName().toUpperCase().contains(searchBar.getText().toUpperCase())|| t.getLastName().toUpperCase().contains(searchBar.getText().toUpperCase()))
                        .forEach(k-> persons.addAll(k));

//                if (searchBar.equals(null)) {
//                    System.out.println("sdvalkjhfasd");
//                } else {
//
//                    ObservableList<Person> searchList = FXCollections.observableArrayList();
//
//                    for (int i = 0; i < getPersons().size(); i++) {
//                        if (getPersons().get(i).getFirstName().toLowerCase().contains(searchBar.getText().toLowerCase())) {
//                            searchList.add(getPersons().get(i));
//
//                            //columns for the table
//                            firstNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
//                            lastNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
//                            birthdayColumn.setCellValueFactory(new PropertyValueFactory<Person, LocalDate>("birthday"));
//
//                        }
//                    }
//                    //clear temporary list
//                    tableView.setItems(null);
//
//                    //load the data
//
//                    tableView.setItems(searchList);
//
//
//                }

            }
        });

    }

    //DELETE------------------------------------------------------------------------DELETE
    public void deleteButtonPushed() {
        Person person = tableView.getSelectionModel().getSelectedItem();

        Response r = client.target("http://localhost:8080/CelebrityListerBackend/webapi/persons/")
                .path(person.getId().getValue() + "")
                .request(MediaType.APPLICATION_JSON)
                .delete();
        for (int i = 0; i < tempPersonList.size(); i++) {
            if(person.getId()==tempPersonList.get(i).getId()){
                tempPersonList.remove(i);
            }
        }
        
        persons.clear();
        persons.addAll(tempPersonList);
    }

    //POST-------------------------------------------------------------------------POST
    public void newPersonButtonPushed() {

        PersonNoProperty newPerson = new PersonNoProperty();
        newPerson.setFirstName(firstNameTextField.getText());
        newPerson.setLastName(lastNameTextField.getText());
        newPerson.setBirthday(java.sql.Date.valueOf(birthdayDatePicker.getValue()));
        client.target("http://localhost:8080/CelebrityListerBackend/webapi/persons/")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(newPerson), Person.class);
        
        
                 
       
        Person temp = new Person( firstNameTextField.getText(), lastNameTextField.getText(), java.sql.Date.valueOf(birthdayDatePicker.getValue()));
       
        
        persons.addAll(temp);

        //get all items from the table as list, add new Person to that list
        //tableView.getItems().add(newPerson);
        
    }

    //GET-----------------------------------------------------------------------------GET
    public List<Person> getPersons(){
        List<PersonNoProperty> ListNoProperties = new ArrayList<>();
        
        
        
        ListNoProperties = client.target("http://localhost:8080/CelebrityListerBackend/webapi/persons")
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<PersonNoProperty>>() {
                    
                });
        
        ListNoProperties.stream().forEach((row)->{
        Person temp = new Person(row.getId(), row.getFirstName(), row.getLastName(), row.getBirthday());
        tempPersonList.add(temp);
        });
        
        return tempPersonList;

    }
    //for repopulating tableView after operations
    public List<Person> getTempPersonList(){
        return tempPersonList;
    }

}
