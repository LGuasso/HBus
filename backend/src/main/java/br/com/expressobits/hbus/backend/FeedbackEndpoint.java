package br.com.expressobits.hbus.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

import java.util.logging.Logger;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "feedbackApi",
        version = "v1",
        resource = "feedback",
        namespace = @ApiNamespace(
                ownerDomain = "backend.hbus.expressobits.com.br",
                ownerName = "backend.hbus.expressobits.com.br",
                packagePath = ""
        )
)
public class FeedbackEndpoint {

    private static final Logger logger = Logger.getLogger(FeedbackEndpoint.class.getName());

    /**
     * This method gets the <code>Feedback</code> object associated with the specified <code>id</code>.
     *
     * @param id The id of the object to be returned.
     * @return The <code>Feedback</code> associated with <code>id</code>.
     */
    @ApiMethod(name = "getFeedback")
    public Feedback getFeedback(@Named("id") Long id) {
        // TODO: Implement this function
        logger.info("Calling getFeedback method");
        return null;
    }

    /**
     * This inserts a new <code>Feedback</code> object.
     *
     * @param feedback The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertFeedback")
    public Feedback insertFeedback(Feedback feedback) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Transaction txn = datastoreService.beginTransaction();
        try {
            Entity entity = new Entity("Feedback",System.currentTimeMillis()+feedback.getMessage());
            entity.setProperty("type", feedback.getType());
            entity.setProperty("email", feedback.getEmail());
            entity.setProperty("informationSystem", feedback.getInformationSystem());
            entity.setProperty("message", feedback.getMessage());
            datastoreService.put(entity);
            txn.commit();
        } finally {
            if (txn.isActive()) {
                txn.rollback();
            }
        }
        // TODO: Implement this function
        logger.info("Calling insertFeedback method");
        return feedback;
    }
}