package br.com.expressobits.hbus.model;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * @author Rafael Correa
 * @since 18/10/15
 */
public class City {

    private Long id;
    private String name;
    private Bitmap imageBitmap;
    private Drawable imageDrawable;
    private String country;
    private String position;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Bitmap getImage() {
        return imageBitmap;
    }

    public Drawable getImageDrawable() {
        return imageDrawable;
    }

    public String getCountry() {
        return country;
    }

    public void setImage(Bitmap image) {
        this.imageBitmap = image;
    }

    public void setImage(Drawable image) {
        this.imageDrawable = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return getName()+" - "+getCountry();
    }
}
