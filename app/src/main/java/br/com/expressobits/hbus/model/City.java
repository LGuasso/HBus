package br.com.expressobits.hbus.model;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by rafael on 18/10/15.
 */
public class City {

    private String name;
    private Bitmap imageBitmap;
    private Drawable imageDrawable;


    public String getName() {
        return name;
    }

    public Bitmap getImage() {
        return imageBitmap;
    }

    public Drawable getImageDrawable() {
        return imageDrawable;
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
}
