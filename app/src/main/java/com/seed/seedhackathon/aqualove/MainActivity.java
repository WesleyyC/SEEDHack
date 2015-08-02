package com.seed.seedhackathon.aqualove;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseObject;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    // Set up log tag
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    GoogleMap mMap;
    private static Double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Test
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();

        // Test Sample
        //Utility.createDummyParse(getApplicationContext());
        List<String> testRow = Utility.singleTestRow(getApplicationContext());

        // Create New Map
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();

        latitude = 26.78;
        longitude = -72.56;

        setUpMap();


    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        // For dropping a marker at a point on the Map
        final LatLng posCor = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(posCor).title("My Home").snippet("Home Address"));

        // For zooming automatically to the Dropped PIN Location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posCor, 12.0f));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

