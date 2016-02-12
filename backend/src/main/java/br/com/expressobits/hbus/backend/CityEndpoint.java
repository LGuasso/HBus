package br.com.expressobits.hbus.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;


/**
 * An endpoint class we are exposing
 */
@Api(
        name = "cityApi",
        version = "v1",
        resource = "city",
        namespace = @ApiNamespace(
                ownerDomain = "backend.hbus.expressobits.com.br",
                ownerName = "backend.hbus.expressobits.com.br",
                packagePath = ""
        )
)
public class CityEndpoint {

    private static final Logger logger = Logger.getLogger(CityEndpoint.class.getName());



    /**
     * This inserts a new <code>City</code> object.
     *
     * @param city The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertCity")
    public City insertCity(City city) {

        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Transaction txn = datastoreService.beginTransaction();
        try {

            Key cityParentKey = KeyFactory.createKey("country",city.getCountry());
            Entity cityEntity = new Entity("City", city.getName(), cityParentKey);
            cityEntity.setProperty("name", city.getName());
            cityEntity.setProperty("country", city.getCountry());
            cityEntity.setProperty("location",city.getLocation());
            datastoreService.put(cityEntity);
            txn.commit();
        } finally {
            if (txn.isActive()) {
                txn.rollback();
            }
        }
        logger.info("Calling insertCity method");
        return city;
    }

    @ApiMethod(name = "getCities")
    public List<City> getCities(@Named("country") String country) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key cityParentKey = KeyFactory.createKey("country", country);
        Query query = new Query("City",cityParentKey);
        List<Entity> results = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());

        ArrayList<City> cities = new ArrayList<>();
        for (Entity result : results) {
            City city = new City();
            city.setId((String) result.getProperty("country")+"/"+(String) result.getProperty("name"));
            city.setName((String) result.getProperty("name"));
            city.setCountry((String) result.getProperty("country"));
            city.setLocation((GeoPt)result.getProperty("location"));
            cities.add(city);
        }

        return cities;
    }

    /**
     * clear list of cities in datastore base in country param
     * @param country
     */
    @ApiMethod(name = "clearCities")
    public void clearCities(@Named("country") String country) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key cityParentKey = KeyFactory.createKey("country", country);
        Query query = new Query("City",cityParentKey);
        List<Entity> results = datastoreService.prepare(query)
                .asList(FetchOptions.Builder.withDefaults());
        for (Entity result : results) {
            datastoreService.delete(result.getKey());
        }

    }
}