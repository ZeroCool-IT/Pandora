/**
 * Project: Pandora
 * File: it.zerocool.batmacaana.model/News.java
 * @author Marco Battisti
 */
package it.zerocool.batmacaana.model;

import java.util.GregorianCalendar;

import it.zerocool.batmacaana.utilities.Constraints;
import it.zerocool.batmacaana.utilities.ParsingUtilities;

/**
 * Public class representing city's news
 *
 * @author Marco Battisti
 */
public class News implements Cardable {

    protected int id;
    protected String title;
    protected String body;
    protected GregorianCalendar date;
    protected String image;
    protected String url;

    /**
     * Public constructor
     */
    public News(int id) {
        this.id = id;
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
     * @return the title of the news
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title of the news to set
     */
    public void setTitle(String title) {
        if (!title.equals(Constraints.EMPTY_VALUE)) {
            this.title = title;
        } else
            this.title = null;
    }

    /**
     * @return the body of the news
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        if (!body.equals(Constraints.EMPTY_VALUE)) {
            this.body = body;
        } else
            this.body = null;
    }

    /**
     * @return the date of the news
     */
    public GregorianCalendar getDate() {
        return date;
    }

    /**
     * @param date the date of the news to set
     */
    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    /**
     * Set the news date parsing infos from String
     *
     * @param date it's the start date to set (YYYY-mm-DD format)
     */
    public void setDate(String date) {
        GregorianCalendar g = ParsingUtilities.parseDate(date);
        setDate(g);

    }

    /**
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image the image of the news to set
     */
    public void setImage(String image) {
        if (!image.equals(Constraints.EMPTY_VALUE)) {
            this.image = image;
        } else
            this.image = null;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url of the news to set
     */
    public void setUrl(String url) {
        if (!url.equals(Constraints.EMPTY_VALUE)) {
            String toSet = url.replace("\\/", "/");
            this.url = toSet;
        } else
            this.url = null;
    }

    /**
     * Redefine equals: 2 news are equals if their ids are equals
     */
    public boolean equals(Object o) {
        if (o != null && o.getClass() == News.class) {
            News n = (News) o;
            if (n.getId() == this.getId())
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
        return getTitle();
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

    /**
     * Get the accent info, if any
     *
     * @return a String representing the accent information of the card
     */
    @Override
    public String getAccentInfo() {
        /*GregorianCalendar date = getDate();
        Context context = HomeActivity.getContext();
        String [] array = context.getResources().getStringArray(R.array.month);
        String month = array[date.get(GregorianCalendar.MONTH)];
        String day = Integer.valueOf(date.get(GregorianCalendar.DAY_OF_MONTH)).toString();
        return day + " " + month;*/
        return null;
    }
}
