package br.com.expressobits.hbus.model;

/**
 * Define a empresa de transporte no quesito de itinerário, codigos e buses
 * @author Rafael Correa
 * @since 26/05/16
 */
public class Company implements Comparable<Company>{
    private String id;
    private String name;
    private String email;
    private String website;
    private String phoneNumber;
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return this.getName();
    }


    @Override
    public int compareTo(Company company) {
        return this.getName().compareTo(company.getName());

    }
}
