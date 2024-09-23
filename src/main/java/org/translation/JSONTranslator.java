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

    private final JSONArray jsonArray;

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

            String jsonString = Files.readString(Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(filename)).toURI()));
            this.jsonArray = new JSONArray(jsonString);

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        List<String> languages = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            if (jsonObject.getString("country").equalsIgnoreCase(country)) {
                JSONArray languagesArray = jsonObject.getJSONArray("languages");

                for (int j = 0; j < languagesArray.length(); j++) {
                    languages.add(languagesArray.getString(j));
                }

                break;
            }
        }
        return languages;
    }

    @Override
    public List<String> getCountries() {
        List<String> countries = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            countries.add(jsonObject.getString("country"));
        }

        return countries;
    }

    @Override
    public String translate(String country, String language) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            if (jsonObject.getString("country").equalsIgnoreCase(country)) {
                JSONArray translations = jsonObject.getJSONArray("translations");

                for (int j = 0; j < translations.length(); j++) {
                    JSONObject translationObj = translations.getJSONObject(j);

                    if (translationObj.getString("language").equalsIgnoreCase(language)) {
                        return translationObj.getString("text");
                    }
                }
            }
        }
        return null;
    }
}
