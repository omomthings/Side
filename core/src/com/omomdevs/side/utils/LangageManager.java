package com.omomdevs.side.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import java.util.HashMap;

public class LangageManager {

     private static LangageManager _instance = null;

    private static final String LANGUAGES_FILE = "data/languages.xml";
    private static final String DEFAULT_LANGUAGE = "fr";

    private HashMap<String, String> _language = null;
    private String _languageName = null;

    private LangageManager() {
        _language = new HashMap<String, String>();

        // Try to load system language
        // If it fails, fallback to default language


        _languageName=java.util.Locale.getDefault().toString().substring(0,2);

        if (!loadLanguage(_languageName)) {
            Gdx.app.error("LangageManager","Failed to load language: "+_languageName+"\nswitching to default langage");
            loadLanguage(DEFAULT_LANGUAGE);
            _languageName = DEFAULT_LANGUAGE;
        }
    }

    public static LangageManager getInstance() {
        if (_instance == null) {
            _instance = new LangageManager();
        }

        return _instance;
    }

    public String getLanguage() {
        return _languageName;
    }

    public String getString(String key) {
        String string;

        if (_language != null) {
            // Look for string in selected language
            string = _language.get(key);

            if (string != null) {
                return string;
            }
        }

        Gdx.app.error("LangageManager","Could not load requested string: "+key);
        // Key not found, return the key itself
        return key;
    }


    public boolean loadLanguage(String languageName) {
        try {
            XmlReader reader = new XmlReader();
            Element root = reader.parse(Gdx.files.internal(LANGUAGES_FILE).reader("UTF-8"));

            Array<Element> languages = root.getChildrenByName("language");

            for (int i = 0; i < languages.size; ++i) {
                Element language = languages.get(i);

                if (language.getAttribute("name").equals(languageName)) {
                    _language.clear();
                    Array<Element> strings = language.getChildrenByName("string");

                    for (int j = 0; j < strings.size; ++j) {
                        Element string = strings.get(j);
                        String key = string.getAttribute("key");
                        String value = string.getAttribute("value");
                        value = value.replace("&lt;br /&gt;&lt;br /&gt;", "\n");
                        _language.put(key, value);
                    }

                    return true;
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error loading languages file " + LANGUAGES_FILE);
            return false;
        }

        return false;
    }
}



