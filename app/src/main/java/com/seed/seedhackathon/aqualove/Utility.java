package com.seed.seedhackathon.aqualove;

import android.content.Context;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by wesley on 8/1/15.
 */
public class Utility {
    // Set up log tag
    public static final String LOG_TAG = Utility.class.getSimpleName();

    // Set up fake data
    public static final String[] user_name={"Linus","Xiaoru","Smart","Calvin"};
    public static final String[] user_latitude={"39.904211","31.230416","30.651652","22.396428"};
    public static final String[] user_longitude={"116.407395","121.473701","104.075931","114.109497"};
    public static final String[] user_text = {"Ew..","这个水质不行","最新更新的数据","Damn..."};
    public static final String[] user_data_key={null, null, "7 4 2 5", null};
    public static final String[] user_image={null,null,null,null};
    public static final String[][] dummyData = {user_name,user_latitude,user_longitude,user_text,user_data_key,user_image};
    // Put everthing in a table
    public static final int[] tableKey={R.string.user_key,R.string.user_latitude_key,R.string.user_longitude_key,
            R.string.user_text_key,R.string.user_data_key,R.string.user_image_key};

    // Separate the comment array string
    public static final String[][] user_comment={null,{"感谢！"},{"我也觉得!","天啊!", "有关部门请注意！"},null};

    // CreateAt Data Project
    public static String DATE_FORMAT_NOW = "yyyy-MM-dd";
    public static SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);

    // Utility Function for Parse
    public static void createDummyParse(Context context){
        for(int i = 0; i < user_name.length; i++){  // for each user
            // create a ParseObject
            ParseObject dummyObject = new ParseObject(context.getString(R.string.test_table_name));
            // for each column
            for(int j = 0; j<tableKey.length; j++){
                if(dummyData[j][i]==null){
                    dummyObject.put(context.getString(tableKey[j]), JSONObject.NULL);
                }else{
                    dummyObject.put(context.getString(tableKey[j]), dummyData[j][i]);
                }
            }
            // enter comment array if there is any
            if(user_comment[i] == null){
                dummyObject.put(context.getString(R.string.user_comment_key), JSONObject.NULL);
            }else {
                JSONArray mJSONArray = new JSONArray(Arrays.asList(user_comment[i]));
                dummyObject.put(context.getString(R.string.user_comment_key), mJSONArray);
            }
            dummyObject.saveInBackground();
        }
    }

    public static List<List<String>> getAllOnlineData(Context context){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(context.getString(R.string.test_table_name));
        query.orderByDescending("updatedAt");

        try {
            List<ParseObject> results = query.find();
            return Utility.convertListParseObjec(context, results);
        } catch (ParseException e) {
            Log.d(LOG_TAG, "Cannot get the whole data");
            return null;
        }
    }

    // given a list of ParseObject return a list of array String
    // ID0,createTimeStamp1(Date->String, convert back for comparing using the str2date),userName2,userLatitude3,userLongitude4,userText5,userData6,userImage7, <A list of comment start at index 8, if there is any>
    public static List<List<String>> convertListParseObjec(Context context, List<ParseObject> results){
        List<List<String>> convertedList = new ArrayList<List<String>> ();
        // for each post
        for(ParseObject post: results){
            List<String> singlePost = new ArrayList<String>();
            singlePost.add(post.getObjectId()); // add id
            singlePost.add(sdf.format(post.getCreatedAt()));    // add date, which is converted to String
            singlePost.add(post.getString(context.getString(R.string.user_key)));
            singlePost.add(post.getString(context.getString(R.string.user_latitude_key)));
            singlePost.add(post.getString(context.getString(R.string.user_longitude_key)));
            singlePost.add(post.getString(context.getString(R.string.user_text_key)));
            singlePost.add(post.getString(context.getString(R.string.user_data_key)));
            // image key is currently null, but potentially we want it to be getParseFile
            singlePost.add(post.getString(context.getString(R.string.user_image_key)));
            // add comments
            JSONArray comments = post.getJSONArray(context.getString(R.string.user_comment_key));
            if(comments != null) {
                for (int i = 0; i < comments.length(); i++) {
                    try {
                        String comment = comments.getString(i);
                        singlePost.add(comment);
                    }catch(JSONException e){
                        Log.d(LOG_TAG,"Problem occurs when trying to convert from json to string: " + e.toString());
                    }
                }
            }
            convertedList.add(singlePost);
        }

        return convertedList;
    }

    // the best single test row, the format is simialr to convertListParseObject, but with a single row
    public static List<String> singleTestRow(Context context){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(context.getString(R.string.test_table_name));
        query.whereEqualTo("objectId", "muEY8lW1dO");
        List<String> testRow = null;
        try{
            testRow=convertListParseObjec(context, query.find()).get(0);
        }catch(ParseException e){
            Log.d(LOG_TAG, "Probelm occurs during getting single testing row "+e.toString());
        }

        return testRow;
    }

    // Given a string to date and you can compare Date obejct
    public static Date str2date(String date){
        Date strDate = null;
        try {
            strDate =  sdf.parse(date);
        }catch(java.text.ParseException e){
            Log.d(LOG_TAG, "Problem occurs when trying to convert from String to Date: "+ e.toString());
        }
        return strDate;
    }

    // a post function for posting to parse, testData is in array format and image is a ParseFile.
    // if any of this element is missing, just pass null
    public static void post(Context context, String user, String latitude, String longitude, String text, int[] data, ParseFile image){
        // Make a string array
        String[] newPost = {user,latitude,longitude,text,data2str(data)};

        // create a ParseObject
        ParseObject newObject = new ParseObject(context.getString(R.string.test_table_name));
        // for each column
        for(int j = 0; j<tableKey.length-1; j++){
            if(newPost[j]==null){
                newObject.put(context.getString(tableKey[j]), JSONObject.NULL);
            }else{
                newObject.put(context.getString(tableKey[j]), newPost[j]);
            }
        }
        // Set image
        if(image == null){
            newObject.put(context.getString(tableKey[tableKey.length-1]), JSONObject.NULL);
        }else{
            newObject.put(context.getString(tableKey[tableKey.length-1]), image);
        }

        newObject.saveInBackground();
    }

    public static String data2str(int[] data){
        String dataStr = data[0]+" "+data[1]+" "+data[2]+" "+data[3];
        return dataStr;
    }
}
