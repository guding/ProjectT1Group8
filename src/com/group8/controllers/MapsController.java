package com.group8.controllers;

import com.group8.database.tables.Beer;
import com.group8.database.tables.MapMarker;
import com.group8.database.tables.Pub;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventHandler;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.plaf.IconUIResource;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by AnkanX on 15-10-24.
 *
 * TODO learn the googlefx API and implement it
 *
 * The Google maps scene originateing from the BeerDetail scene
 * When a beer is sold by a pub this will display a google maps window with markers of those pubs
 * locations, and a tableview sorted by the price of the beer you previously selected.
 *
 */
public class MapsController implements Initializable,MapComponentInitializedListener {

    @FXML
    public Button Back;
    @FXML
    public Button Home;
    @FXML
    public GoogleMapView mapView;
    @FXML
    public GoogleMap map;
    @FXML
    public TableColumn<MapMarker,String> pubsColumn;
    @FXML
    public TableColumn<MapMarker,String> priceColumn;
    @FXML
    public TableView showPubs;

    private ArrayList<Marker> markers = new ArrayList<>();

    public ObservableList<MapMarker> masterData = FXCollections.observableArrayList(BeerData.markers);
    private int _pubID;

    /**
     * Back button pressed takes you back to "result screen"
     * @param event
     * @throws IOException
     */
    @FXML
    public void backAction(ActionEvent event) throws IOException {
        Parent homescreen = FXMLLoader.load(getClass().getResource(Navigation.beerDetailviewFXML));
        Scene result_scene = new Scene(homescreen, 800, 600);
        Stage main_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        main_stage.setScene(result_scene);
        main_stage.show();
    }

    /**
     * Home Button
     * @param event
     * @throws IOException
     */
    @FXML
    public void returnHome(ActionEvent event) throws IOException {
        Parent homescreen = FXMLLoader.load(getClass().getResource(Navigation.homescreenFXML));
        Scene result_scene = new Scene(homescreen, 800, 600);
        Stage main_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        main_stage.setScene(result_scene);
        main_stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Navigation.mapviewFXML = "/com/group8/resources/views/googleMaps.fxml";


        System.out.println("Initialized google maps!");
        mapView.addMapInializedListener(this);
        pubsColumn.setCellValueFactory(new PropertyValueFactory<MapMarker, String>("PubName"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<MapMarker, String>("Price"));
        showPubs.setItems(masterData);
    }

    /**
     * Select a Pub row and proceed to the beerDetail scene
     */
    public void getRow() {
        showPubs.setOnMouseClicked(new EventHandler<MouseEvent>() {
            // Select item will only be displayed when dubbleclicked

            /**
             * Dubleclick event
             *
             * @param event
             */
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {

                    // Set the selectedMarker instance of beer we have to selected item
                    int z = showPubs.getSelectionModel().getSelectedIndex();


                    for (int i = 0; i < markers.size(); i++) {
                        // Remove existing markers
                        map.removeMarker(markers.get(i));

                        // Repaint Hack
                        map.setZoom(14);
                        map.setZoom(15);
                        if(i == z) {
                            // Add the selected marker back to the map
                            map.addMarker(markers.get(z));
                            // make the infowindow for the selected marker
                            InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
                            infoWindowOptions.content("<h2>" + BeerData.markers.get(i).getPubName() + "</h2>"
                                    + "Address: " + BeerData.markers.get(i).getAddress() + "<br>"
                                    + "Price. " + BeerData.markers.get(i).getPrice() + ":- <br>"
                                    + "in stock: " + BeerData.markers.get(i).InStock());
                            InfoWindow markerWindow = new InfoWindow(infoWindowOptions);
                            // Open the infowindow
                            markerWindow.open(map, markers.get(z));
                            // Set the center of the screen to the active markers position
                            map.setCenter(new LatLong(BeerData.markers.get(z).getLatitude(),BeerData.markers.get(z).getLongitude()));
                            // Set zoom to a good overview zoom
                            map.setZoom(15);
                        }

                    }


                }
            }

        });
    }



        @Override
    public void mapInitialized() {

        // TODO add the geoPosition column to the pub table ( string double,double ( maybe have secure input method in addpub?))
        // TODO make string -> double double converter
        // TODO set coordinates to BeerData.selectedBeerPubs.geoPosition(1 result)
        // TODO set propertyValueFactory of the showPubs column to populate with all the pubs that have the beer
        // TODO set markers of all the pubs on map, and add dubbleClick listener so when u click a beer in the column the view is transported to that marker
        // TODO able to "follow" pubs marked by markers

        //Set the initial properties of the map.
        MapOptions mapOptions = new MapOptions();

        mapOptions.center(new LatLong(BeerData.markers.get(0).getLatitude(), BeerData.markers.get(0).getLongitude()))
                .mapType(MapTypeIdEnum.ROADMAP)
                .overviewMapControl(true)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(true)
                .zoom(15);


        map = mapView.createMap(mapOptions);

        for(int i = 0; i < BeerData.markers.size(); i++) {

            LatLong markerLocation = new LatLong(BeerData.markers.get(i).getLatitude(), BeerData.markers.get(i).getLongitude());
            System.out.println("Loaded: " + BeerData.markers.get(i).getLatitude() + " " + BeerData.markers.get(i).getLongitude());

            //Add markers to the map
            MarkerOptions markerOptions1 = new MarkerOptions();
            markerOptions1.position(markerLocation);

           // markerOptions1.icon("html/mymarker.png");
            markerOptions1.visible(true);


            Marker marker = new Marker(markerOptions1);
            markers.add(marker);


            if(i == 0) {
                map.addMarker(markers.get(i));
                InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
                infoWindowOptions.content("<h2>" + BeerData.markers.get(i).getPubName() + "</h2>"
                        + "Address: " + BeerData.markers.get(i).getAddress() + "<br>"
                        + "Price. " + BeerData.markers.get(i).getPrice() + ":- <br>"
                        + "in stock: " + BeerData.markers.get(i).InStock());
                InfoWindow markerWindow = new InfoWindow(infoWindowOptions);
                markerWindow.open(map, markers.get(i));
                map.setCenter(new LatLong(BeerData.markers.get(i).getLatitude(),BeerData.markers.get(i).getLongitude()));
                map.setZoom(15);
            }else {
                map.addMarker(markers.get(i));
            }



            System.out.println(BeerData.markers.get(i).getAddress());
            }
      
        }
        public int get_PubID(){
        	return _pubID;
        }
}
