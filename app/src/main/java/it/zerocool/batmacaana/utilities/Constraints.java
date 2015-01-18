/**
 * Project: Pandora
 * File it.zerocool.batmacaana.utility/Contraints.java
 * @author Marco Battisti
 */
package it.zerocool.batmacaana.utilities;

/**
 * Class containing constraint values
 *
 * @author Marco Battisti
 */
public class Constraints {

    //USER ID
    public static final int USER_ID = 594;

    //Empty value
    public static final String EMPTY_VALUE = "";


    //Error fallback type fragment
    public static final int CONNECTION_ERROR = 0;
    public static final int NO_RESULTS = 1;

    //Places types
    public static final int TYPE_TOSEE = 1;
    public static final int TYPE_EAT = 2;
    public static final int TYPE_SLEEP = 3;
    public static final int TYPE_SERVICE = 4;
    public static final int TYPE_SHOP = 5;

    //Navigation Drawer Fragments
    public static final int TOSEE = 0;
    public static final int EVENT = 1;
    public static final int EAT = 2;
    public static final int SLEEP = 3;
    public static final int NEWS = 4;
    public static final int SHOP = 5;
    public static final int SERVICES = 6;
    public static final int ABOUT = 7;
    public static final int PLACE = 8;

    //Requests URI
    public static final String URI_TOSEE = "http://www.ilmiositodemo.altervista.org/app/json/vedere.php?user=";
    public static final String URI_EAT = "http://www.ilmiositodemo.altervista.org/app/json/mangiare.php?user=";
    public static final String URI_SLEEP = "http://www.ilmiositodemo.altervista.org/app/json/dormire.php?user=";
    public static final String URI_SERVICES = "http://www.ilmiositodemo.altervista.org/app/json/servizi.php?user=";
    public static final String URI_SHOP = "http://www.ilmiositodemo.altervista.org/app/json/shop.php?user=";
    public static final String URI_NEWS = "http://www.ilmiositodemo.altervista.org/app/json/news.php?user=";
    public static final String URI_EVENT = "http://www.ilmiositodemo.altervista.org/app/json/eventi.php?user=";


    //Week Days
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;
    public static final int SUNDAY = 7;

    //Holydays
    public static final int JAN_01 = 0;
    public static final int JAN_06 = 1;
    public static final int EASTER = 2;
    public static final int EASTERS_MONDAY = 3;
    public static final int APRIL_25 = 4;
    public static final int MAY_01 = 5;
    public static final int JUNE_02 = 6;
    public static final int AUG_15 = 7;
    public static final int NOV_01 = 8;
    public static final int DEC_08 = 9;
    public static final int DEC_25 = 10;
    public static final int DEC_26 = 11;


    /**
     *
     */
    private Constraints() {
        // TODO Auto-generated constructor stub
    }

}
