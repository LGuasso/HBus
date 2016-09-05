package br.com.expressobits.hbus.model;

import java.util.Calendar;
import java.util.List;

/**
 * @author Rafael Correa
 * @since 16/08/16
 */
public class News implements Comparable<News>{

    private String id;
    private String body;
    private String title;
    private String subtitle;
    private List<String> imagesUrls;
    private List<String> itineraryIds;
    private List<String> cityIds;
    private long time;
    private String source;
    private String country;
    private String city;
    private String company;
    private String itinerary;
    private boolean actived;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<String> getImagesUrls() {
        return imagesUrls;
    }

    public void setImagesUrls(List<String> imagesUrls) {
        this.imagesUrls = imagesUrls;
    }

    public List<String> getItineraryIds() {
        return itineraryIds;
    }

    public void setItineraryIds(List<String> itinerariesIds) {
        this.itineraryIds = itinerariesIds;
    }

    public List<String> getCityIds() {
        return cityIds;
    }

    public void setCityIds(List<String> cityIds) {
        this.cityIds = cityIds;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getItinerary() {
        return itinerary;
    }

    public void setItinerary(String itinerary) {
        this.itinerary = itinerary;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setActived(boolean actived) {
        this.actived = actived;
    }

    public boolean isActived() {
        return actived;
    }

    @Override
    public int compareTo(News another) {
        Calendar  calendarThis = Calendar.getInstance();
        calendarThis.setTimeInMillis(this.getTime());
        Calendar  calendarAnother = Calendar.getInstance();
        calendarAnother.setTimeInMillis(another.getTime());
        return calendarAnother.compareTo(calendarThis);
    }

    @Override
    public String toString() {
        return this.getTitle();
    }
}
