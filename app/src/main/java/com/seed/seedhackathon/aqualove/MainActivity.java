package com.seed.seedhackathon.aqualove;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    // Set up log tag
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    GoogleMap mMap;

    private List<Double> latList, lngList;
    private List<String> userNameList, commentList;

    private ListView listView;

    private CustomAdapter commentsAdaptor;

    private List<Marker> markerList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        commentsAdaptor = new CustomAdapter(this);

        listView = (ListView)findViewById(R.id.comments_list_view);
        listView.setAdapter(commentsAdaptor);
        commentsAdaptor.loadObjects();


        // Create New Map
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();

        setUpMap();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refresh();
    }

    private void setUpMap() {

        List<List<String>> allOnlineData = Utility.getAllOnlineData(getApplicationContext());

        latList = new ArrayList<Double>();
        lngList = new ArrayList<Double>();
        userNameList = new ArrayList<String>();
        commentList = new ArrayList<String>();

        markerList = new ArrayList<Marker>();

        Iterator<List<String>> allOnlineDataIter = allOnlineData.iterator();
        while (allOnlineDataIter.hasNext()){
            List<String> obj_str = allOnlineDataIter.next();
            latList.add(Double.parseDouble(obj_str.get(3)));
            lngList.add(Double.parseDouble(obj_str.get(4)));
            userNameList.add(obj_str.get(2));
            commentList.add(obj_str.get(5));
        }


        mMap.clear();

        mMap.setMyLocationEnabled(true);
        // For dropping a marker at a point on the Map
        for (int i = 0; i < lngList.size(); i++){
            LatLng posCor = new LatLng(latList.get(i), lngList.get(i));
            Marker currentMarker = mMap.addMarker(new MarkerOptions().position(posCor).
                    title(userNameList.get(i)).snippet(commentList.get(i)));
            markerList.add(currentMarker);
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markerList) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();

        int padding = 100; // offset from edges of the map in pixels
        final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.animateCamera(cu);
            }
        });

        mMap.getUiSettings().setZoomControlsEnabled(true);

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
        if (id == R.id.action_refresh) {
            refresh();
            return true;
        }

        if (id == R.id.action_post) {
            final Intent goToPostActivity = new Intent(this, PostActivity.class);
            startActivity(goToPostActivity);
        }

        return super.onOptionsItemSelected(item);
    }

    private void refresh() {
        commentsAdaptor.notifyDataSetInvalidated();
        commentsAdaptor.clear();
        commentsAdaptor.loadObjects();
        commentsAdaptor.notifyDataSetChanged();
        setUpMap();
    }


}

