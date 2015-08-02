package com.seed.seedhackathon.aqualove;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ParseQueryAdapter<ParseObject> {



    public CustomAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {

            public ParseQuery create() {
                ParseQuery query = new ParseQuery("postTestObject");
                query.orderByDescending("updatedAt");
                return query;
            }
        });
    }

    // Customize the layout by overriding getItemView
    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.single_comment, null);
        }

        super.getItemView(object, v, parent);


        List<ParseObject> object_list = new ArrayList<ParseObject>();
        object_list.add(object);
        List<String> object_str = Utility.convertListParseObjec(getContext(),object_list).get(0);

        TextView user_name_view = (TextView) v.findViewById(R.id.post_user);
        user_name_view.setText(object_str.get(2));

        TextView complain_view = (TextView) v.findViewById(R.id.post_complain);
        complain_view.setText(object_str.get(5));

        String[] test_data = {"-1","-1","-1","-1"};
        if(object_str.get(6) != null)
                test_data = object_str.get(6).split("\\s+");

        TextView fu_value_view = (TextView) v.findViewById(R.id.fu_element_value);
        fu_value_view.setText(test_data[0]);

        TextView f_value_view = (TextView) v.findViewById(R.id.f_element_value);
        f_value_view.setText(test_data[1]);

        TextView cd_value_view = (TextView) v.findViewById(R.id.cd_element_value);
        cd_value_view.setText(test_data[2]);

        TextView i_value_view = (TextView) v.findViewById(R.id.i_element_value);
        i_value_view.setText(test_data[3]);



//        // Add and download the image
//        ParseImageView todoImage = (ParseImageView) v.findViewById(R.id.icon);
//        ParseFile imageFile = object.getParseFile("image");
//        if (imageFile != null) {
//            todoImage.setParseFile(imageFile);
//            todoImage.loadInBackground();
//        }
//
//        // Add the title view
//        TextView titleTextView = (TextView) v.findViewById(R.id.text1);
//        titleTextView.setText(object.getString("title"));
//
//        // Add a reminder of how long this item has been outstanding
//        TextView timestampView = (TextView) v.findViewById(R.id.timestamp);
//        timestampView.setText(object.getCreatedAt().toString());
        return v;
    }

}