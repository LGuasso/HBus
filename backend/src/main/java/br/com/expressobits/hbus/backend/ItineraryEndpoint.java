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
        name = "itineraryApi",
        version = "v1",
        resource = "itinerary",
        namespace = @ApiNamespace(
                ownerDomain = "backend.hbus.expressobits.com.br",
                ownerName = "backend.hbus.expressobits.com.br",
                packagePath = ""
        )
)
public class ItineraryEndpoint {

    private static final Logger logger = Logger.getLogger(ItineraryEndpoint.class.getName());

    /**
     * This method gets the <code>Itinerary</code> object associated with the specified <code>id</code>.
     *
     * @param id The id of the object to be returned.
     * @return The <code>Itinerary</code> associated with <code>id</code>.
     */
    @ApiMethod(name = "getItinerary")
    public Itinerary getItinerary(@Named("id") Long id) {
        // TODO: Implement this function
        logger.info("Calling getItinerary method");
        return null;
    }

    /**
     * This inserts a new <code>Itinerary</code> object.
     *
     * @param itinerary The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertItinerary")
    public Itinerary insertItinerary(@Named("country")String country,@Named("cityName")String cityName,Itinerary itinerary) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Transaction txn = datastoreService.beginTransaction();
        try {

            Key countryParentKey = KeyFactory.createKey("country", country);
            Key cityParentKey = KeyFactory.createKey(countryParentKey,"city", cityName);
            Entity cityEntity = new Entity("Itinerary", itinerary.getName(), cityParentKey);
            cityEntity.setProperty("name", itinerary.getName());
            cityEntity.setProperty("ways", itinerary.getWays());
            cityEntity.setProperty("codes", itinerary.getCodes());
            datastoreService.put(cityEntity);
            txn.commit();
        } finally {
            if (txn.isActive()) {
                txn.rollback();
            }
        }
        logger.info("Calling insertItinerary method");
        return itinerary;
    }

    @ApiMethod(name = "getItineraries")
    public List<Itinerary> getItineraries(@Named("country")String country,@Named("cityName")String cityName) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key countryParentKey = KeyFactory.createKey("country", country);
        Key cityParentKey = KeyFactory.createKey(countryParentKey,"city", cityName);
        Query query = new Query(cityParentKey);
        List<Entity> results = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());

        ArrayList<Itinerary> itineraries = new ArrayList<>();
        for (Entity result : results) {
            Itinerary itinerary = new Itinerary();
            //itinerary.setId();
            itinerary.setName((String) result.getProperty("name"));
            itinerary.setCodes((List<String>) result.getProperty("codes"));
            itinerary.setWays((List<String>) result.getProperty("ways"));
            itineraries.add(itinerary);
        }

        return itineraries;
    }

    /**
     * clear list of itineraries in datastore base in country param
     * @param cityName
     */
    @ApiMethod(name = "clearItineraries")
    public void clearItineraries(@Named("cityName")String cityName) {

        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key countryParentKey = KeyFactory.createKey("country", "RS");
        Key cityParentKey = KeyFactory.createKey(countryParentKey,"city", cityName);
        Query query = new Query(cityParentKey);
        List<Entity> results = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());

        ArrayList<Itinerary> itineraries = new ArrayList<>();

        for (Entity result : results) {
            datastoreService.delete(result.getKey());
        }
        /**
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Transaction txn = datastoreService.beginTransaction();
        try {
            //TODO problemas
            Key countryParentKey = KeyFactory.createKey("country","RS");
            //TODO problemas em country parametro
            Key cityParentKey = KeyFactory.createKey(countryParentKey,"city", cityName);
            Query query = new Query(cityParentKey);
            List<Entity> results = datastoreService.prepare(query)
                    .asList(FetchOptions.Builder.withDefaults());
            for (Entity result : results) {
                datastoreService.delete(result.getKey());
            }
            txn.commit();
        } finally {
            if (txn.isActive()) { txn.rollback(); }
        }*/

    }
}