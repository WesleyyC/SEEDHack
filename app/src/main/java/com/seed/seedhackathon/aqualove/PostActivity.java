package com.seed.seedhackathon.aqualove;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Taibo on 8/2/15.
 */
public class PostActivity extends ActionBarActivity {


    EditText userName;
    EditText userComment;
    EditText tieData;
    EditText fuData;
    EditText dianData;
    EditText geData;

    Button postButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        userName = (EditText)findViewById(R.id.edit_post_user);
        userComment = (EditText) findViewById(R.id.edit_post_text);
        tieData = (EditText) findViewById(R.id.edit_post_fe_element_value);
        fuData = (EditText) findViewById(R.id.edit_post_f_element_value);
        geData = (EditText) findViewById(R.id.edit_post_cd_element_value);
        dianData = (EditText) findViewById(R.id.edit_post_i_element_value);

        postButton = (Button) findViewById(R.id.button_send);

        View.OnClickListener postClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int tie, fu, ge, dian;
                String tieStr = tieData.getText().toString();
                String fuStr = fuData.getText().toString();
                String geStr = geData.getText().toString();
                String dianStr = dianData.getText().toString();
                String userNameStr = userName.getText().toString();
                String userCommentStr = userComment.getText().toString();

                if (userNameStr.isEmpty()){
                    Toast.makeText(getApplicationContext(), "请输入用户名", Toast.LENGTH_LONG).show();
                }else if (userCommentStr.isEmpty()){
                    Toast.makeText(getApplicationContext(), "请输入评价", Toast.LENGTH_LONG).show();
                }else {
                    if (tieStr.isEmpty()) {
                        tie = -1;
                    } else {
                        tie = Integer.parseInt(tieStr);
                    }
                    if (fuStr.isEmpty()) {
                        fu = -1;
                    } else {
                        fu = Integer.parseInt(fuStr);
                    }
                    if (geStr.isEmpty()) {
                        ge = -1;
                    } else {
                        ge = Integer.parseInt(geStr);
                    }
                    if (dianStr.isEmpty()) {
                        dian = -1;
                    } else {
                        dian = Integer.parseInt(dianStr);
                    }

                    String[] location = getLocation(34.341568, 108.940175, 500000);


                    int[] data = {tie, fu, ge, dian};
                    Utility.post(getApplicationContext(), userNameStr, location[0], location[1], userCommentStr, data, null);
                    finish();
                }
            }
        };

        postButton.setOnClickListener(postClick);





//        // Get the username from the intent
//        Intent fromMainActivity = getIntent();
//        String username = fromMainActivity.getStringExtra("username");
//        helloText.setText("Welcome back, " + username);

    }

    public static String[] getLocation(double x0, double y0, int radius) {
        Random random = new Random();

        // Convert radius from meters to degrees
        double radiusInDegrees = radius / 111000f;

        double u = random.nextDouble();
        double v = random.nextDouble();
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        double x = w * Math.cos(t);
        double y = w * Math.sin(t);

        // Adjust the x-coordinate for the shrinking of the east-west distances
        double new_x = x / Math.cos(y0);

        double foundLongitude = new_x + x0;
        double foundLatitude = y + y0;
        String[] location ={Double.toString(foundLongitude), Double.toString(foundLatitude) };
        return location;

    }

}