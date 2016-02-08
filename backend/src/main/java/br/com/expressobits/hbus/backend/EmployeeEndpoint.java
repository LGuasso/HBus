package br.com.expressobits.hbus.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "employeeApi",
        version = "v1",
        resource = "employee",
        namespace = @ApiNamespace(
                ownerDomain = "backend.hbus.expressobits.com.br",
                ownerName = "backend.hbus.expressobits.com.br",
                packagePath = ""
        )
)
public class EmployeeEndpoint {

    private static final Logger logger = Logger.getLogger(EmployeeEndpoint.class.getName());

    /**
     * This method gets the <code>Employee</code> object associated with the specified <code>id</code>.
     *
     * @param name The id of the object to be returned.
     * @return The <code>Employee</code> associated with <code>id</code>.
     */
    @ApiMethod(name = "getEmployee")
    public Employee getEmployee(@Named("name") String name) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

        Key employeeKey = KeyFactory.createKey("Employee",name);
        logger.info("Calling getEmployee method");
        try {
            Entity employee = datastoreService.get(employeeKey);
            Employee e = new Employee();
            e.setFirstName((String)employee.getProperty("firstName"));
            e.setLastName((String) employee.getProperty("lastName"));
            e.setHireDate((Date) employee.getProperty("hireDate"));
            e.setAttendedHrTraining((Boolean) employee.getProperty("attendedHrTraining"));
            return e;
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @ApiMethod(name = "getEmployees")
    public List<Employee> getEmployees(){
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key employeeParentKey = KeyFactory.createKey("EmployeeParent", "hbus");
        Query query = new Query(employeeParentKey);
        List<Entity> results = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());

        ArrayList<Employee> employees = new ArrayList<Employee>();
        for (Entity result : results) {
            Employee employee = new Employee();
            employee.setFirstName((String)result.getProperty("firstName"));
            employee.setLastName((String) result.getProperty("lastName"));
            employee.setHireDate((Date) result.getProperty("hireDate"));
            employee.setAttendedHrTraining((Boolean)result.getProperty("attendedHrTraining"));
            employees.add(employee);
        }

        return employees;
    }

    /**
     * This inserts a new <code>Employee</code> object.
     *
     * @param employee The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertEmployee")
    public Employee insertEmployee(Employee employee) {
        //Get datastoreservice
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Transaction txn = datastoreService.beginTransaction();

        try{
            Key employeeKey = KeyFactory.createKey("EmployeeParent","hbus");
            Entity entity = new Entity(Employee.class.getSimpleName(),employee.getFirstName()+" "+employee.getLastName(),employeeKey);
            entity.setProperty("firstName", employee.getFirstName());
            entity.setProperty("lastName", employee.getLastName());
            entity.setProperty("hireDate", employee.getHireDate());
            entity.setProperty("attendedHrTraining", employee.isAttendedHrTraining());
            datastoreService.put(entity);

            txn.commit();
        }finally {
            if (txn.isActive()) {
                txn.rollback();
            }
        }





        logger.info("Calling insertEmployee method "+Employee.class.getSimpleName());
        return employee;
    }



    /**
     * This method gets the <code>Employee</code> object associated with the specified <code>id</code>.
     *
     * @param name The id of the object to be returned.
     * @return The <code>Employee</code> associated with <code>id</code>.
     */
    @ApiMethod(name = "searchEmployee")
    public Employee searchEmployee(@Named("name") String name) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

        Filter filter = new Query.FilterPredicate(Entity.KEY_RESERVED_PROPERTY,
                Query.FilterOperator.EQUAL,
                name);

        Query q = new Query("Employee").setFilter(filter);



        Key employeeKey = KeyFactory.createKey("Employee",name);
        logger.info("Calling getEmployee method");
        try {
            Entity employee = datastoreService.get(employeeKey);
            Employee e = new Employee();
            e.setFirstName((String)employee.getProperty("firstName"));
            e.setLastName((String) employee.getProperty("lastName"));
            e.setHireDate((Date) employee.getProperty("hireDate"));
            e.setAttendedHrTraining((Boolean) employee.getProperty("attendedHrTraining"));
            return e;
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


}