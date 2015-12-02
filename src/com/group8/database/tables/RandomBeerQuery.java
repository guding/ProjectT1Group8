package com.group8.database.tables;

import com.group8.controllers.RandomBeerControllers.Scene1Controller;
import com.group8.controllers.RandomBeerControllers.Scene2Controller;
import com.group8.controllers.RandomBeerControllers.Scene3Controller;
import com.group8.database.MysqlDriver;

public class RandomBeerQuery extends MysqlDriver {

    String query;
    String query2;

    Scene1Controller scene1 = new Scene1Controller();
    Scene2Controller scene2 = new Scene2Controller();
    Scene3Controller scene3 = new Scene3Controller();


// Search string for the user choice
    public String resultQuery(){

        if (scene3.getTypesPicked().equals("(") || scene3.getTypesPicked().equals(")")){
            query = "SELECT beerID,name,image,description,beerTypeEN,countryName,percentage,producerName,volume,isTap,packageTypeEN,price,avStars,countryFlag" +
                    " FROM beers, beerType, origin, package" +
                    " WHERE (price < " + scene1.getPricePickedHigh() +
                    " AND price > " + scene1.getPricePickedLow() +
                    " AND percentage < " + scene2.getPercentagePickedHigh() +
                    " AND percentage > " + scene2.getPercentagePickedLow() +
                    " AND beers.beerTypeID = beerType.beerTypeID " +
                    " AND beers.originID = origin.originID " +
                    " AND beers.package = package.packageID "+
                    ") order by rand() limit 0, 100";
        }
        else {
            query = "SELECT beerID,name,image,description,beerTypeEN,countryName,percentage,producerName,volume,isTap,packageTypeEN,price,avStars,countryFlag" +
                    " FROM beers, beerType, origin, package" +
                    " WHERE (price < " + scene1.getPricePickedHigh() +
                    " AND price > " + scene1.getPricePickedLow() +
                    " AND percentage < " + scene2.getPercentagePickedHigh() +
                    " AND percentage > " + scene2.getPercentagePickedLow() +
                    " AND beers.beerTypeID = beerType.beerTypeID " +
                    " AND beers.originID = origin.originID " +
                    " AND beers.package = package.packageID "+
                    " AND beerTypeID IN " + scene3.getTypesPicked() +
                    ") order by rand() limit 0, 100";
        }

        //Test print
        System.out.println(query);

       return query;
    }

    // Search string for the random beer
    public String randomQuery(int integer){

        query2 = "SELECT beerID,name,image,description,beerTypeEN,countryName,percentage,producerName,volume,isTap,packageTypeEN,price,avStars,countryFlag" +
                " FROM beers, beerType, origin, package" +
                " WHERE beerID = "+ integer +
                " AND beers.beerTypeID = beerType.beerTypeID " +
                " AND beers.originID = origin.originID " +
                " AND beers.package = package.packageID "+
                " order by rand() limit 0, 100";

        //Test print
        System.out.println(query2);

        return query2;
    }

}
