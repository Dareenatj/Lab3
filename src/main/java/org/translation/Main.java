package org.translation;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for this program.
 * Complete the code according to the "to do" notes.<br/>
 * The system will:<br/>
 * - prompt the user to pick a country name from a list<br/>
 * - prompt the user to pick the language they want it translated to from a list<br/>
 * - output the translation<br/>
 * - at any time, the user can type quit to quit the program<br/>
 */
public class Main {

    private static final String QUIT = "quit";

    /**
     * This is the main entry point of our Translation System!<br/>
     * A class implementing the Translator interface is created and passed into a call to runProgram.
     * @param args not used by the program
     */
    public static void main(String[] args) {

        Translator translator = new JSONTranslator();
        runProgram(translator);
    }

    /**
     * This is the method which we will use to test your overall program, since
     * it allows us to pass in whatever translator object that we want!
     * See the class Javadoc for a summary of what the program will do.
     * @param translator the Translator implementation to use in the program
     */
    public static void runProgram(Translator translator) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String country = promptForCountry(translator, scanner);
            if (QUIT.equalsIgnoreCase(country)) {
                break;
            }
            String language = promptForLanguage(translator, country, scanner);
            if (QUIT.equalsIgnoreCase(language)) {
                break;
            }
            // Assuming the translate method takes actual country and language names
            System.out.println(country + " in " + language + " is " + translator.translate(country, language));
            System.out.println("Press enter to continue or type 'quit' to exit.");
            String textTyped = scanner.nextLine();

            if (QUIT.equalsIgnoreCase(textTyped)) {
                break;
            }
        }
        scanner.close();
    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForCountry(Translator translator, Scanner scanner) {
        List<String> countries = translator.getCountries();
        Collections.sort(countries);

        System.out.println("Select a country from the list:");
        for (String country : countries) {
            System.out.println(country);
        }

        return scanner.nextLine();
    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForLanguage(Translator translator, String country, Scanner scanner) {
        List<String> languages = translator.getCountryLanguages(country);
        Collections.sort(languages);

        System.out.println("Select a language from the list:");
        for (String language : languages) {
            System.out.println(language);
        }

        return scanner.nextLine();
    }
}
