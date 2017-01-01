package br.com.expressobits.hbus.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rafael Correa
 * @since 18/10/15
 */
public class City {

    private String id;
    private String name;
    private String country;
    private String companyDefault;
    private Map<String,Double> localization = new HashMap<>();
    private boolean actived;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCompanyDefault() {
        return companyDefault;
    }

    public void setCompanyDefault(String companyDefault) {
        this.companyDefault = companyDefault;
    }

    public Map<String,Double>  getLocalization() {
        return localization;
    }

    public void setLocalization(Map<String,Double>  localization) {
        this.localization = localization;
    }

    public boolean isActived() {
        return actived;
    }

    public void setActived(boolean actived) {
        this.actived = actived;
    }

    @Override
    public String toString() {
        return getName();
    }
}
