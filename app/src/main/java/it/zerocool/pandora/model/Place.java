/**
 * Project: Pandora
 * File it.zerocool.pandora.model/Place.java
 * @author Marco Battisti
 */
package it.zerocool.pandora.model;

import android.graphics.drawable.Drawable;
import android.location.Location;

import java.util.LinkedList;
import java.util.Locale;
import java.util.StringTokenizer;

import it.zerocool.pandora.utilities.Constraints;

/**
 * Class representing city's places
 *
 * @author Marco Battisti
 */
public class Place implements Cardable {

    protected int id;
    protected String name;
    protected String image;
    protected String fsqrLink;
    protected Drawable drawableImagery;

    protected LinkedList<String> tags;
    protected String description;
    protected ContactCard contact;
    protected TimeCard timeCard;
    protected Location location;


    /**
     * Public constructor
     */
    public Place(int id) {
        this.id = id;
        this.tags = new LinkedList<String>();
    }


    /**
     * @return the id
     */
    public int getId() {
        return id;
    }


    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * @return the name of the place
     */
    public String getName() {
        return name;
    }


    /**
     * @param name the name  of the place to set
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * @return the image of the place
     */
    public String getImage() {
        return image;
    }


    /**
     * @param image the image of the place to set
     */
    public void setImage(String image) {
        if (!image.equals(Constraints.EMPTY_VALUE)) {
            this.image = image;
        } else
            this.image = null;
    }


    /**
     * @return the link to 4square
     */
    public String getFsqrLink() {
        return fsqrLink;
    }


    /**
     * @param fsqrLink the 4square link to set
     */
    public void setFsqrLink(String fsqrLink) {
        if (!fsqrLink.equals(Constraints.EMPTY_VALUE)) {
            this.fsqrLink = fsqrLink;
        } else
            this.fsqrLink = null;
    }


    /**
     * @return the tags list
     */
    public LinkedList<String> getTags() {
        return tags;
    }


    /**
     * @param tags the tags list to set
     */
    public void setTags(LinkedList<String> tags) {
        this.tags = tags;
    }

    /**
     * Add the tags to tags' list from a string in CSV format
     *
     * @param csv is the string in CSV format
     */
    public void setTagsFromCSV(String csv) {
        if (csv != null && !csv.equals(Constraints.EMPTY_VALUE)) {
            StringTokenizer tokenizer = new StringTokenizer(csv, ",");
            while (tokenizer.hasMoreTokens()) {
                String toAdd = tokenizer.nextToken();
                toAdd = toAdd.trim();
                toAdd = toAdd.substring(0, 1).toUpperCase(Locale.ITALY) + toAdd.substring(1);
                if (!getTags().contains(toAdd)) {
                    getTags().add(toAdd);
                }
            }
        }
    }


    /**
     * @return the description of the place
     */
    public String getDescription() {
        return description;
    }


    /**
     * @param description the description of the place to set
     */
    public void setDescription(String description) {
        if (!description.equals(Constraints.EMPTY_VALUE)) {
            this.description = description;
        } else
            this.description = null;
    }


    /**
     * @return the contact card of the place
     */
    public ContactCard getContact() {
        return contact;
    }


    /**
     * @param contact the contact card of the place to set
     */
    public void setContact(ContactCard contact) {
        this.contact = contact;
    }


    /**
     * @return the timeCard
     */
    public TimeCard getTimeCard() {
        return timeCard;
    }


    /**
     * @param timeCard the timeCard to set
     */
    public void setTimeCard(TimeCard timeCard) {
        this.timeCard = timeCard;
    }


    /**
     * @return the location of the place
     */
    public Location getLocation() {
        return location;
    }

    /*public void setDrawableImagery(Drawable imagery) {
        this.drawableImagery = imagery;
    }

    public Drawable getDrawableImagery() {
        return drawableImagery;
    }*/

    /**
     * @param location is the location of the place to set
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Redefine equals: 2 places are equals if their ids are equals
     */
    public boolean equals(Object o) {
        if (o != null && o.getClass() == Place.class) {
            Place p = (Place) o;
            if (p.getId() == this.getId())
                return true;
            else
                return false;
        }
        return false;
    }


    /**
     * Get the header title of the item
     *
     * @return a String representing card's header
     */
    @Override
    public String getHeader() {
        return getName();
    }

    /**
     * Get the imagery for the item
     *
     * @return a String representing the imagery for the card
     */
    @Override
    public String getImagery() {
        return getImage();
    }

    /**
     * Get the sub-header, if any
     *
     * @return a String representing card's sub-header
     */
    @Override
    public String getSubheader() {
        return null;
    }
}
