package pl.stormit.tinyurl.service;

import com.maxmind.geoip2.DatabaseReader;

import java.io.File;
import java.io.IOException;

public class GeoLiteDatabaseLoader {

    private static final String LOCALIZATION_DB_GEO_LITE = "src/main/resources/localizationdb/GeoLite2-City.mmdb";

    private static GeoLiteDatabaseLoader instance;
    private DatabaseReader database;

    private GeoLiteDatabaseLoader() {
        try {
            File databaseFile = new File(LOCALIZATION_DB_GEO_LITE);
            this.database = new DatabaseReader.Builder(databaseFile).build();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize GeoLite2 database", e);
        }
    }

    public static GeoLiteDatabaseLoader getInstance() {
        if (instance == null) {
            instance = new GeoLiteDatabaseLoader();
        }
        return instance;
    }

    public DatabaseReader getDatabase(){
        return database;
    }
}
