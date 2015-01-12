/**
 * Project: Pandora
 * File: it.zerocool.pandora.model/Cardable.java
 * @author Marco Battisti
 */

package it.zerocool.pandora.model;

/**
 * Public interface representing all the items that can be represented with a CardView
 */
public interface Cardable {

    /**
     * Get the header title of the item
     *
     * @return a String representing card's header
     */
    public String getHeader();

    /**
     * Get the imagery for the item
     *
     * @return a String representing the imagery for the card
     */
    public String getImagery();

    /**
     * Get the sub-header, if any
     *
     * @return a String representing card's sub-header
     */
    public String getSubheader();
}
