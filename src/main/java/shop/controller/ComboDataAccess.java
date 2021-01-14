package shop.controller;

import shop.model.Country;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.*;

public class ComboDataAccess {

    public static Map<String, String> getInformations() throws ParseException {

        return infoReader("config/prefix.txt");
    }

    public static Map<String, String> infoReader(String file) {

        Map<String, String> isoCountries = new HashMap<>();

        String iso;
        String prefix;
        String line;

        try {
            BufferedReader is = new BufferedReader(
                    new InputStreamReader(Objects.requireNonNull(ComboDataAccess.class.getClassLoader().getResourceAsStream(file))));
            while ((line = is.readLine()) != null) {
                String[] data = line.trim().split(",");
                iso = data[0].trim();
                prefix = data[1].trim();
                isoCountries.put(iso, prefix);
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException ignored) {
        }

        return isoCountries;

    }

}
