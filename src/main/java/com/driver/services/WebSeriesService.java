package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo

        // checking if series is already present
        WebSeries existingWebseries = webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName());
        if(existingWebseries!=null){
            throw new Exception("Series is already present");
        }

        // check if production house is present or not
        Optional<ProductionHouse> OptionalproductionHouse = productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId());
        if(!OptionalproductionHouse.isPresent()){
            throw new Exception("production house not found with this id:" + webSeriesEntryDto.getProductionHouseId());
        }
        ProductionHouse productionHouse = OptionalproductionHouse.get();


        // dto to entry
        WebSeries webSeries = new WebSeries();
        webSeries.setSeriesName(webSeriesEntryDto.getSeriesName());
        webSeries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        webSeries.setRating(webSeriesEntryDto.getRating());
        webSeries.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());



        // bidirectional mapping
        webSeries.setProductionHouse(productionHouse);
        productionHouse.getWebSeriesList().add(webSeries);

        // saved webseries in db
        WebSeries savedWebseries =  webSeriesRepository.save(webSeries);
        productionHouseRepository.save(productionHouse);

        return savedWebseries.getId();
    }

}
