package com.group8.controllers;
import com.group8.database.MysqlDriver;
import com.group8.database.tables.Beer;
import com.group8.database.tables.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by AnkanX on 15-10-22.
 *
 * TODO Visual Upgrade & optimizeation
 *
 */

public class MainController implements Initializable {

    // Declaration of FXML elements
    @FXML
    public Button search;
    @FXML
    public Button login;
    @FXML
    public CheckBox runSqlBox;
    @FXML
    public CheckBox advancedType;
    @FXML
    public CheckBox advancedProducer;
    @FXML
    public CheckBox advancedDescription;
    @FXML
    public CheckBox advanced;
    @FXML
    public CheckBox all;
    @FXML
    public CheckBox advancedName;
    @FXML
    public CheckBox advancedOrigin;
    @FXML
    public TextField searchText;
    @FXML
    public TextField loginText;
    @FXML
    public PasswordField pswrdField;
    @FXML
    public Label error;
    @FXML
    public Button randomButton;

    // TODO implement threads
   // public ProgressIndicator load;

    /**
     * Auto clear fields when selected
     * Clear the Search field
     */
    public void clearFieldSearch()
    {
        exitField();

        if (searchText.getText().equals("Search...")) {
            searchText.setText("");
        }
    }
    // Clear the Login field
    public void clearFieldLogin()
    {
        exitField();
        if (loginText.getText().equals("Type here:")) {
            loginText.setText("");
        }
    }
    // Clear the password field
    public void clearFieldPassword()
    {
        exitField();
        if(pswrdField.getText().equals("password"))
        {
            pswrdField.setText("");
        }
    }

    // Checkbox that when checked shows advanced checkboxes
    public void showAdvanced()
    {
        if(runSqlBox.isSelected())
        {
            runSqlBox.setSelected(false);
        }

        // Handle diffrent casesof visability and selection
        if(!advancedDescription.isVisible() && !advancedType.isVisible() && !advancedProducer.isVisible())
        {
            advancedType.setVisible(true);
            advancedProducer.setVisible(true);
            advancedDescription.setVisible(true);
            all.setVisible(true);
            advancedName.setVisible(true);
            advancedOrigin.setVisible(true);
            advancedType.setSelected(false);
            advancedProducer.setSelected(false);
            advancedDescription.setSelected(false);
            advancedName.setSelected(true);
            advancedOrigin.setSelected(false);
            all.setSelected(false);
        }else
        {
            advancedType.setVisible(false);
            advancedProducer.setVisible(false);
            advancedDescription.setVisible(false);
            all.setVisible(false);
            advancedName.setVisible(false);
            advancedOrigin.setVisible(false);
            advancedName.setSelected(true);
        }
    }
    // Checkbox to check all the advanced boxes
    public void checkAll()
    {
        if ( !advancedType.isSelected() || !advancedProducer.isSelected() || !advancedDescription.isSelected()) {
            advancedType.setSelected(true);
            advancedProducer.setSelected(true);
            advancedDescription.setSelected(true);
            advancedName.setSelected(true);
            advancedOrigin.setSelected(true);
        }else{
            advancedType.setSelected(false);
            advancedProducer.setSelected(false);
            advancedDescription.setSelected(false);
            advancedName.setSelected(false);
            advancedOrigin.setSelected(false);
        }


    }

    // Makes the advanced search viseble or inviseble depending on runSQL
    public void noSearch()
    {


        if(runSqlBox.isSelected()) {
            advancedDescription.setVisible(false);
            advancedProducer.setVisible(false);
            advancedType.setVisible(false);
            all.setVisible(false);
            advanced.setSelected(false);
            advancedName.setSelected(true);
            advancedName.setVisible(false);
            advancedOrigin.setVisible(false);

        }else if(!runSqlBox.isSelected() && advanced.isSelected()){
            advancedDescription.setVisible(true);
            advancedProducer.setVisible(true);
            advancedType.setVisible(true);
            all.setVisible(true);
            error.setText("");
            advancedName.setVisible(true);
            advancedName.setSelected(true);
            advancedOrigin.setSelected(true);
        }else
        {
            error.setText("");
        }
    }

    // Resets guide text if no input was made
    public void exitField()
    {
        if (loginText.getText().isEmpty()){
            loginText.setText("Type here:");
        }

        if (searchText.getText().isEmpty()){
            searchText.setText("Search...");
        }

        if (pswrdField.getText().isEmpty()){
            pswrdField.setText("password");
        }


    }

    /**
     * On clicking the Search button execute query through MySqlDriver
     * @param event
     * @throws IOException
     */
    @FXML
    public void onSearch(javafx.event.ActionEvent event) throws IOException {

        // Load wheel until task is finished//
        // load.setVisible(true);

        // Fetch the user input
        BeerData.searchInput="";


        /**
         * SQL query
         *
         * Construct a query as a String dependent on user specifications
         */
        if (runSqlBox.isSelected()) {
            BeerData.searchInput = searchText.getText();
        }else {
            // name search is defualt
            BeerData.searchInput = "select * from beers where ";

            if(advancedName.isSelected()){
                BeerData.searchInput += "name like '%" + searchText.getText() + "%'";
            }


            // Advanced
            if(advanced.isSelected())
            {
                // For reasons
                int selectedIteams=0;

                if (advancedOrigin.isSelected()) {
                    if(advancedName.isSelected() || advancedProducer.isSelected() || advancedType.isSelected() || advancedDescription.isSelected()) {
                        BeerData.searchInput += " or originID like '%" + searchText.getText() + "%'";
                        selectedIteams++;
                    }else{
                        BeerData.searchInput += "originID like '%" + searchText.getText() + "%'";
                    }
                }

                if (advancedType.isSelected()) {
                    if(advancedName.isSelected() || advancedProducer.isSelected() || advancedDescription.isSelected() || advancedOrigin.isSelected()) {
                        BeerData.searchInput += " or beerType like '%" + searchText.getText() + "%'";
                        selectedIteams++;
                    } else{
                        BeerData.searchInput += "beerType like '%" + searchText.getText() + "%'";
                    }
                }
                if (advancedProducer.isSelected()) {
                    if(advancedName.isSelected() || advancedType.isSelected() || advancedDescription.isSelected() ||advancedOrigin.isSelected()) {
                        BeerData.searchInput += " or producerName like '%" + searchText.getText() + "%'";
                        selectedIteams++;
                    }else{
                        BeerData.searchInput += "producerName like '%" + searchText.getText() + "%'";
                    }
                }
                if (advancedDescription.isSelected()) {
                    if(advancedName.isSelected() || advancedProducer.isSelected() || advancedType.isSelected() || advancedOrigin.isSelected()) {
                        BeerData.searchInput += " or description like '%" + searchText.getText() + "%'";
                        selectedIteams++;
                    }else{
                        BeerData.searchInput += "description like '%" + searchText.getText() + "%'";
                    }
                }

                if (!advancedName.isSelected() && selectedIteams > 1){
                    // Test Output
                    System.out.println(BeerData.searchInput.substring(26, 28));

                    BeerData.searchInput = BeerData.searchInput.substring(0,26) + BeerData.searchInput.substring(29);
                    // Test Output
                    System.out.println(BeerData.searchInput);
                }
            }
        }


        // Execute user query
        ArrayList<ArrayList<Object>> sqlData;

        sqlData = MysqlDriver.selectMany(BeerData.searchInput);

        for (int i = 0; i < sqlData.size(); i++) {
            // Add a new Beer to the beer arraylist
            Beer beer = new Beer(sqlData.get(i));
            // Testoutput
            //System.out.print(beer.getName()+"\n");
            BeerData.beer.add(beer);
        }


        if ((BeerData.beer.size()>0)) {

            // Load the result stage
            Parent result = FXMLLoader.load(getClass().getResource("/com/group8/resources/views/resultScreen.fxml"));
            Scene result_scene = new Scene(result,800,600);
            Stage main_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            main_stage.setScene(result_scene);
            main_stage.show();
        }else
        {
           //load.setVisible(false);
           error.setText("Invalid Search String!");
        }
    }


    /**
     * Login Button event
     * @param event
     * @throws IOException
     */
    @FXML
    public void onLogin(javafx.event.ActionEvent event) throws IOException{

        String username = loginText.getText();
        String password = pswrdField.getText();

        String sqlQuery = "Select * from users where username = '" + username + "' and password = '" + password + "';";

        ArrayList<Object> userData = MysqlDriver.select(sqlQuery);

        if(userData == null)
        {

            System.out.println("Is empty");
            return;
        }

        User fetchedUser = new User(userData);

        if(!fetchedUser.get_name().equals(username))
        {
            System.out.println(username);
            System.out.println(fetchedUser.get_name());
            return;
        }

        UserData.userInstance = fetchedUser;

        //System.out.println(fetchedUser.get_isPub());

        if(fetchedUser.get_isPub())
        {
            // Load the pub stage
            Parent result = FXMLLoader.load(getClass().getResource("/com/group8/resources/views/pubInfo.fxml"));
            Scene result_scene = new Scene(result,800,600);
            Stage main_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            main_stage.setScene(result_scene);
            main_stage.show();
        }
        else
        {
            // Load the pub stage
            Parent result = FXMLLoader.load(getClass().getResource("/com/group8/resources/views/loggedInHomescreen.fxml"));
            Scene result_scene = new Scene(result,800,600);
            Stage main_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            main_stage.setScene(result_scene);
            main_stage.show();
        }
    }

    /**
     * On Register
     * @param event
     * @throws IOException
     */
    @FXML
    public void onRegister(javafx.event.ActionEvent event) throws IOException
    {
        // Load the Register stage
        Parent result = FXMLLoader.load(getClass().getResource("/com/group8/resources/views/registerUser.fxml"));
        Scene result_scene = new Scene(result,800,600);
        Stage main_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        main_stage.setScene(result_scene);
        main_stage.show();
    }

    @FXML
    public void onRandom (ActionEvent event) throws Exception{

        Stage stage = (Stage) randomButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/group8/resources/views/RandomBeerScenes/scene1.fxml"));
        Scene scene = new Scene(root);

        stage.setTitle("BeerFinder Alpha Test");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *  Initialize Main controller
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Reset the BeerData Arraylist
        BeerData.beer = new ArrayList<Beer>();
        Navigation.homescreenFXML = "/com/group8/resources/views/homescreen.fxml";

    }



}
