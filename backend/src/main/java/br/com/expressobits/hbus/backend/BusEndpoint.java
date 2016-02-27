package br.com.expressobits.hbus.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
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
        name = "busApi",
        version = "v1",
        resource = "bus",
        namespace = @ApiNamespace(
                ownerDomain = "backend.hbus.expressobits.com.br",
                ownerName = "backend.hbus.expressobits.com.br",
                packagePath = ""
        )
)
public class BusEndpoint {

    private static final Logger logger = Logger.getLogger(BusEndpoint.class.getName());

    /**
     * This method gets the <code>Bus</code> object associated with the specified <code>id</code>.
     *
     * @param id The id of the object to be returned.
     * @return The <code>Bus</code> associated with <code>id</code>.
     */
    @ApiMethod(name = "getBus")
    public Bus getBus(@Named("id") Long id) {
        // TODO: Implement this function
        logger.info("Calling getBus method");
        return null;
    }

    /**
     * This inserts a new <code>Bus</code> object.
     *
     * @param bus The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertBus")
    public Bus insertBus(@Named("country")String country,@Named("cityName")String cityName,@Named("itineraryName")String itineraryName,Bus bus) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Transaction txn = datastoreService.beginTransaction();
        try {

            Key countryParentKey = KeyFactory.createKey("country", country);
            Key cityParentKey = KeyFactory.createKey(countryParentKey, "city", cityName);
            Key itineraryParentKey = KeyFactory.createKey(cityParentKey, "itinerary", itineraryName);
            Key wayParentKey = KeyFactory.createKey(itineraryParentKey, "way", bus.getWay());
            Key typedayParentKey = KeyFactory.createKey(wayParentKey, "typeday", bus.getTypeday());
            Entity entity = new Entity("Bus", bus.getTime(), typedayParentKey);
            entity.setProperty("time", bus.getTime());
            entity.setProperty("code", bus.getCode());
            datastoreService.put(entity);
            txn.commit();
        } finally {
            if (txn.isActive()) {
                txn.rollback();
            }
        }
        logger.info("Calling insertBus method");
        return bus;
    }

    @ApiMethod(name = "getBuses")
    public List<Bus> getBuses(@Named("country")String country,@Named("cityName")String cityName,
                               @Named("itineraryName")String itineraryName,@Named("way")String way,@Named("typeday")String typeday) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key countryParentKey = KeyFactory.createKey("country", country);
        Key cityParentKey = KeyFactory.createKey(countryParentKey, "city", cityName);
        Key itineraryParentKey = KeyFactory.createKey(cityParentKey, "itinerary", itineraryName);
        Key wayParentKey = KeyFactory.createKey(itineraryParentKey, "way", way);
        Key typedayParentKey = KeyFactory.createKey(wayParentKey, "typeday", typeday);
        Query query = new Query(typedayParentKey);
        List<Entity> results = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());

        ArrayList<Bus> buses = new ArrayList<>();
        for (Entity result : results) {
            Bus bus = new Bus();
            bus.setTime((String) result.getProperty("time"));
            bus.setCode((String) result.getProperty("code"));
            bus.setWay(way);
            bus.setTypeday(typeday);

            buses.add(bus);
        }
        logger.info("Calling getBuses method");
        return buses;
    }

    @ApiMethod(name = "getAllBuses")
    public List<Bus> getAllBuses(@Named("country")String country,@Named("cityName")String cityName) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key countryParentKey = KeyFactory.createKey("country", country);
        Key cityParentKey = KeyFactory.createKey(countryParentKey, "city", cityName);
        Query query = new Query("Bus",cityParentKey);
        List<Entity> results = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());

        ArrayList<Bus> buses = new ArrayList<>();
        for (Entity result : results) {
            Bus bus = new Bus();
            bus.setTime((String) result.getProperty("time"));
            bus.setCode((String) result.getProperty("code"));

            buses.add(bus);
        }
        logger.info("Calling getBuses method");
        return buses;
    }
}