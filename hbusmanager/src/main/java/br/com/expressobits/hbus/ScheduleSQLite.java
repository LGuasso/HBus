package br.com.expressobits.hbus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import br.com.expressobits.hbus.dao.BusContract;
import br.com.expressobits.hbus.dao.CodeContract;
import br.com.expressobits.hbus.dao.CompanyContract;
import br.com.expressobits.hbus.dao.ItineraryContract;
import br.com.expressobits.hbus.model.*;

/**
 * @author Rafael Correa
 * @since 01/03/17
 */
public class ScheduleSQLite {

    private Connection connection;
    private String name;

    public ScheduleSQLite(String name){
        this.name = name;
    }

    private Connection createConnection(String name) throws ClassNotFoundException,SQLException{
        Class.forName("org.sqlite.JDBC");
        Connection c = DriverManager.getConnection("jdbc:sqlite:"+name);
        System.out.println("Opened database successfully");
        return c;
    }

    public void createTables() {

        try {
            createTable(CompanyContract.SQL_CREATE_TABLE,
                    CompanyContract.Company.TABLE_NAME);
            createTable(ItineraryContract.SQL_CREATE_TABLE,
                    ItineraryContract.Itinerary.TABLE_NAME);
            createTable(CodeContract.SQL_CREATE_TABLE,
                    CodeContract.Code.TABLE_NAME);
            createTable(BusContract.SQL_CREATE_TABLE,
                    BusContract.Bus.TABLE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createTable(String sql,String tableName) throws Exception{
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
        System.out.println("Table "+tableName+" created successfully");
    }

    public void insert(City city, Company company){
        try {
            insert(CompanyContract.getInsertSQL(company), CompanyContract.Company.TABLE_NAME);
            System.out.println("Insert "+ CompanyContract.Company.TABLE_NAME+"- "+company.getName()+" successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(City city,Company company,Itinerary itinerary){
        try {
            insert(ItineraryContract.getInsertSQL(itinerary),ItineraryContract.Itinerary.TABLE_NAME);
            System.out.println("\tInsert "+ ItineraryContract.Itinerary.TABLE_NAME+"- "+itinerary.getName()+" successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(City city,Company company,Code code){
        try {
            insert(CodeContract.getInsertSQL(code), CodeContract.Code.TABLE_NAME);
            System.out.println("\tInsert "+ CodeContract.Code.TABLE_NAME+"- "+code.getName()+" successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(City city,Company company,Itinerary itinerary,Bus bus){
        try {
            insert(BusContract.getInsertSQL(bus),BusContract.Bus.TABLE_NAME);
        } catch (Exception e) {
            System.err.print(bus.getId()+" "+e.getMessage());
            e.printStackTrace();
        }
    }

    private void insert(String sql,String tableName) throws Exception{
        Statement stmt = null;
        if(connection==null){
            System.out.println("connection null");
        }
        stmt = connection.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
        //connection.commit();
    }

    public void open() {
        try {
            connection = createConnection(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
