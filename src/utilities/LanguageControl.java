package utilities;

/**
 * @author Brian Nason, Student Number xxxxxxxx
 */

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class is to control the language of the user based on their computer's language choice.
 */
public class LanguageControl {

    /**
     * This determines the user's Location, displays it, and also decides whether the user information should be in
     * english or french depending on their locale.
     * @return the user's locale - used for langaue setting.
     */
    public static Locale getUserLocation() {
        //This retrieves the user's location
        Locale locale = Locale.getDefault();

        //FIXME: Code to test French Language - not used in main program
        //Locale locale = new Locale("fr");
        //Locale locale = new Locale("es");

        //FIXME: For debug purposes
        System.out.println("\nYour default language code is: \n" + locale);

        //FIXME: Code to test the language output vs location output
        System.out.println("You are speaking:\n" + locale.getDisplayName() + "\n");


        return locale;
    }

    /**
     * This takes in the user's locale, and outputs the resource bundle object to update all the text in the program
     * to the user's language selection
     * @param locale this is from the above method in the class that determines the user's locale
     * @return returns the resourcebundle object that contains the user's prefered language
     */
    public static ResourceBundle getResourceBundle(Locale locale) {

        ResourceBundle rb = ResourceBundle.getBundle("Language/LanguagePack", locale);
        return rb;
    }

}