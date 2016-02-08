package br.com.expressobits.hbus.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

import java.util.logging.Logger;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "addressApi",
        version = "v1",
        resource = "address",
        namespace = @ApiNamespace(
                ownerDomain = "backend.hbus.expressobits.com.br",
                ownerName = "backend.hbus.expressobits.com.br",
                packagePath = ""
        )
)
public class AddressEndpoint {

    private static final Logger logger = Logger.getLogger(AddressEndpoint.class.getName());

    /**
     * This method gets the <code>Address</code> object associated with the specified <code>id</code>.
     *
     * @param id The id of the object to be returned.
     * @return The <code>Address</code> associated with <code>id</code>.
     */
    @ApiMethod(name = "getAddress")
    public Address getAddress(@Named("id") Long id) {
        // TODO: Implement this function
        logger.info("Calling getAddress method");
        return null;
    }

    /**
     * This inserts a new <code>Address</code> object.
     *
     * @param address The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertAddress")
    public Address insertAddress(Address address) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Entity entity = new Entity(Address.class.getSimpleName(),address.getNumber()+" "+address.getRoad());
        entity.setProperty("road",address.getRoad());
        entity.setProperty("number",address.getNumber());

        datastoreService.put(entity);
        logger.info("Calling insertAddress method");
        return address;
    }
}