package com.seed.seedhackathon.aqualove;

/**
 * Created by wesley on 7/24/15.
 */
import android.app.Application;
import com.parse.Parse;

public class App extends Application {

    @Override public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "K6gQKgy1awauF8bOgkkxu9D6Zblz9eFo2LlyLdIL", "FxWULU4zXI9tV7guHLb2Y82Sgl3gM271u9ve9QFg");
    }
}