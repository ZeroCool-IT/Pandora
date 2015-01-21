/**
 * Project: Pandora
 * File: it.zerocool.batmacaana.model/Event.java
 * @author Marco Battisti
 */
package it.zerocool.batmacaana.model;

import android.location.Location;
import android.text.TextUtils;

import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Locale;
import java.util.StringTokenizer;

import it.zerocool.batmacaana.utilities.Constraints;
import it.zerocool.batmacaana.utilities.ParsingUtilities;

/**
 * Public class representing city's events
 *
 * @author Marco Battisti
 */
public class Event implements Cardable {

    protected int id;
    protected String name;
    protected GregorianCalendar startDate;
    protected GregorianCalendar endDate;
    protected GregorianCalendar startHour;
    protected GregorianCalendar endHour;
    protected String image;
    protected LinkedList<String> tags;
    protected String description;
    protected Location location;
    protected ContactCard contact;


    /**
     * Public constructor
     *
     * @param id is the unique id for the event
     */
    public Event(int id) {
        this.id = id;
        this.tags = new LinkedList<String>();
    }


    /**
     * @return the id of the event
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
     * @return the name of the event
     */
    public String getName() {
        return name;
    }


    /**
     * @param name the name of the event to set
     */
    public void setName(String name) {
        if (!name.equals(Constraints.EMPTY_VALUE)) {
            this.name = name;
        } else
            this.name = null;
    }


    /**
     * @return the start date of the event
     */
    public GregorianCalendar getStartDate() {
        return startDate;
    }


    /**
     * @param date the start date of the event to set
     */
    public void setStartDate(GregorianCalendar date) {
        this.startDate = date;
    }

    /**
     * Set the event's start date parsing infos from String
     *
     * @param date it's the start date to set (YYYY-mm-DD format)
     */
    public void setStartDate(String date) {
        GregorianCalendar g = ParsingUtilities.parseDate(date);
        setStartDate(g);

    }


    /**
     * @return the end date of the event
     */
    public GregorianCalendar getgetEndDate() {
        return endDate;
    }


    /**
     * @param endDate is the end date of the event to set
     */
    public void setEndDate(GregorianCalendar endDate) {
        this.endDate = endDate;
    }

    /**
     * Set the event's end date parsing infos from String
     *
     * @param endDate it's the end date to set (YYYY-mm-DD format)
     */
    public void setEndDate(String endDate) {
        GregorianCalendar g = ParsingUtilities.parseDate(endDate);
        setEndDate(g);

    }

    /**
     * @return the start hour of the event
     */
    public GregorianCalendar getStartHour() {
        return startHour;
    }

    /**
     * @param startHour the start hour of the event to set
     */
    public void setStartHour(GregorianCalendar startHour) {
        this.startHour = startHour;
    }

    /**
     * Set the event's start hour parsing infos from String
     *
     * @param startHour it's the end date to set (HH:mm 24H format)
     */
    public void setStartHour(String startHour) {
        GregorianCalendar g = ParsingUtilities.parseHour(startHour);
        setStartHour(g);
    }

    /**
     * @return the start hour of the event
     */
    public GregorianCalendar getEndHour() {
        return endHour;
    }

    /**
     * @param endHour the end hour of the event to set
     */
    public void setEndHour(GregorianCalendar endHour) {
        this.endHour = endHour;
    }

    /**
     * Set the event's end hour parsing infos from String
     *
     * @param endHour it's the end date to set (HH:mm 24H format)
     */
    public void setEndHour(String endHour) {
        GregorianCalendar g = ParsingUtilities.parseHour(endHour);
        setEndHour(g);
    }


    /**
     * @return the image of the event
     */
    public String getImage() {
        return image;
    }


    /**
     * @param image the image  of the event to set
     */
    public void setImage(String image) {
        if (!image.equals(Constraints.EMPTY_VALUE)) {
            this.image = image;
        } else
            this.image = null;
    }


    /**
     * @return the tags list of the event
     */
    public LinkedList<String> getTags() {
        return tags;
    }


    /**
     * @param tags the tags list of the event to set
     */
    public void setTags(LinkedList<String> tags) {
        this.tags = tags;
    }

    /**
     * Add the tags to tags' list from a string in CSV format
     * SO FICO
     *
     * @param csv is the string in CSV format
     */
    public void setTagsFromCSV(String csv) {
        if (csv != null) {
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
     * @return the description of the event
     */
    public String getDescription() {
        return description;
    }


    /**
     * @param description the description of the event to set
     */
    public void setDescription(String description) {
        if (!description.equals(Constraints.EMPTY_VALUE)) {
            this.description = description;
        } else
            this.description = null;
    }


    /**
     * @return the location of the event
     */
    public Location getLocation() {
        return location;
    }


    /**
     * @param location the location of the event to set
     */
    public void setLocation(Location location) {
        this.location = location;
    }


    /**
     * @return the contact card of the event
     */
    public ContactCard getContact() {
        return contact;
    }


    /**
     * @param contact the contact card of the event to set
     */
    public void setContact(ContactCard contact) {
        this.contact = contact;
    }

    /**
     * Redefine equals: 2 events are equals if their ids are equals
     */
    public boolean equals(Object o) {
        if (o != null && o.getClass() == Event.class) {
            Event e = (Event) o;
            if (e.getId() == this.getId())
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
        return TextUtils.join(", ", getTags());
    }

    /**
     * Get the accent info, if any
     *
     * @return a String representing the accent information of the card
     */
    @Override
    public String getAccentInfo() {
        String day = Integer.valueOf(getStartDate().get(GregorianCalendar.DAY_OF_MONTH)).toString();
        String month = Integer.valueOf((getStartDate().get(GregorianCalendar.MONTH)) + 1).toString();
        String year = Integer.valueOf(getStartDate().get(GregorianCalendar.YEAR)).toString();
        return year + "-" + month + "-" + day;

        /*GregorianCalendar date = getStartDate();
        Context context = HomeActivity.getContext();
        String [] array = context.getResources().getStringArray(R.array.month);
        String month = array[date.get(GregorianCalendar.MONTH)];
        String day = Integer.valueOf(date.get(GregorianCalendar.DAY_OF_MONTH)).toString();
        return day + " " + month;*/
    }
}
