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
        name = "codeApi",
        version = "v1",
        resource = "code",
        namespace = @ApiNamespace(
                ownerDomain = "backend.hbus.expressobits.com.br",
                ownerName = "backend.hbus.expressobits.com.br",
                packagePath = ""
        )
)
public class CodeEndpoint {

    private static final Logger logger = Logger.getLogger(CodeEndpoint.class.getName());

    /**
     * This method gets the <code>Code</code> object associated with the specified <code>id</code>.
     *
     * @param id The id of the object to be returned.
     * @return The <code>Code</code> associated with <code>id</code>.
     */
    @ApiMethod(name = "getCode")
    public Code getCode(@Named("id") Long id) {
        // TODO: Implement this function
        logger.info("Calling getCode method");
        return null;
    }

    /**
     * This inserts a new <code>Code</code> object.
     *
     * @param code The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertCode")
    public Code insertCode(@Named("country")String country,@Named("cityName")String cityName,Code code) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Transaction txn = datastoreService.beginTransaction();
        try {

            Key countryParentKey = KeyFactory.createKey("country", country);
            Key cityParentKey = KeyFactory.createKey(countryParentKey, "city", cityName);
            Entity entity = new Entity("Code", code.getName(), cityParentKey);
            entity.setProperty("name", code.getName());
            entity.setProperty("descrition", code.getDescrition());
            datastoreService.put(entity);
            txn.commit();
        } finally {
            if (txn.isActive()) {
                txn.rollback();
            }
        }
        // TODO: Implement this function
        logger.info("Calling insertCode method");
        return code;
    }

    @ApiMethod(name = "getCodes")
    public List<Code> getCodes(@Named("country") String country, @Named("cityName") String cityName) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key countryParentKey = KeyFactory.createKey("country", country);
        Key cityParentKey = KeyFactory.createKey(countryParentKey, "city", cityName);
        Query query = new Query("Code",cityParentKey);
        List<Entity> results = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());

        ArrayList<Code> codes = new ArrayList<>();
        for (Entity result : results) {
            Code code = new Code();
            code.setId(country+"/"+cityName+"/"+((String) result.getProperty("name")));
            code.setName((String) result.getProperty("name"));
            code.setDescrition((String) result.getProperty("descrition"));
            codes.add(code);
        }

        return codes;
    }
}