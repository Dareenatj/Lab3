package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    private static CountryCodeConverter countryCodeConverter;
    private static final int COUNTRY_INDEX = 3;
    private static final int LANGUAGE_INDEX = 2;
    private static LanguageCodeConverter languageCodeConverter;
    private final JSONArray jsonArray;
    private final String jCountry = "alpha3";

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(Objects.requireNonNull(getClass()
                    .getClassLoader().getResource(filename)).toURI()));
            this.jsonArray = new JSONArray(jsonString);
            countryCodeConverter = new CountryCodeConverter();
            languageCodeConverter = new LanguageCodeConverter();

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        List<String> languages = new ArrayList<>();
        String countryCode;

        // Determine the country code
        if (country.length() != COUNTRY_INDEX) {
            countryCode = CountryCodeConverter.fromCountry(country);
        }
        else {
            countryCode = country;
        }

        int id = -1;

        // Find the country in the JSON array
        for (int i = 0; i < this.jsonArray.length(); i++) {
            JSONObject jsonObject = this.jsonArray.getJSONObject(i);

            // Compare with alpha3 country code
            if (jsonObject.getString("alpha3").equalsIgnoreCase(countryCode)) {
                id = i;
                break;
            }
        }

        // If the country was not found, handle that case
        if (id == -1) {
            System.out.println("Country not found: " + country);
            return languages;
        }

        // Retrieve languages from the found JSON object
        JSONObject jsonObject = this.jsonArray.getJSONObject(id);

        // Iterate through the keys and filter out "id", "alpha2", and "alpha3"
        for (String key : jsonObject.keySet()) {
            if (!"id".equals(key) && !"alpha2".equals(key) && !jCountry.equals(key)) {
                // Convert language code to full language name and add to the list
                String languageName = LanguageCodeConverter.fromLanguage(key);
                languages.add(languageName);
            }
        }

        // Debugging output
        System.out.println("Languages found for " + country + ": " + languages.size());

        return languages;
    }

    @Override
    public List<String> getCountries() {
        List<String> countries = new ArrayList<>();

        for (int i = 0; i < this.jsonArray.length(); i++) {
            JSONObject jsonObject = this.jsonArray.getJSONObject(i);

            String countryName = jsonObject.getString(jCountry);
            countries.add(countryName);
        }

        return countries;
    }

    @Override
    public String translate(String country, String language) {
        if (country.length() == COUNTRY_INDEX && language.length() == LANGUAGE_INDEX) {
            int id = -1;

            for (int i = 0; i < this.jsonArray.length(); i++) {
                JSONObject jsonObject = this.jsonArray.getJSONObject(i);

                if (country.equals(jsonObject.getString(jCountry))) {
                    id = i;
                    break;
                }
            }

            JSONObject jsonObject2 = this.jsonArray.getJSONObject(id);
            return jsonObject2.getString(language);
        }
        else {
            String countryCode = CountryCodeConverter.fromCountry(country);
            String languageCode = LanguageCodeConverter.fromLanguage(language);
            translate(countryCode, languageCode);
        }
        return "Translation not found for the specified country and language.";
    }
}
