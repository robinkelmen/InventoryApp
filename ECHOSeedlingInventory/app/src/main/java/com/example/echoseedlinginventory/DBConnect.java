package com.example.echoseedlinginventory;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ResponseDelivery;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBConnect {

    //private String url;

    private static  StringRequest request;


    public static RequestQueue requestQueue;

    public static RecyclerView.Adapter adapterThis;

    private static ArrayList<String> oldtags = new ArrayList<>();
    private static ArrayList<Integer> oldTagids= new ArrayList<>();

    public static HashMap< String, Integer> tagHashh = new HashMap<String, Integer>();
    public static String urlHead = "http://10.0.0.121/";






    public  static void setRecyclerViewAdapter(RecyclerView.Adapter adapter){
        adapterThis = adapter;
    }



    public static void setUptags(Context context, HashMap<String,Integer> toHash, List<String> ct, int itemID){
        for (int i = 0; i < ct.size(); i++) {
            if (ct.get(i).equals(null)) {
                Toast.makeText(context, "Error checked tags is null", Toast.LENGTH_SHORT).show();

            } else {

                String tag = ct.get(i);
                Toast.makeText(context, "Current Tagid size " + DBConnect.oldTagids.size(), Toast.LENGTH_SHORT).show();
                int tagid = toHash.get(tag);
                if (tagid != -1) {

                    addItemTags(context, itemID, tagid);
                }
            }
        }
    }

    public static void  getTagsForId(final Context context, final HashMap<String,Integer> toHash,final  List<String> ct, final int itemID){
        String url = urlHead + "get_tags.php";
        requestQueue = Volley.newRequestQueue(context);
        request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonObject = new JSONArray(response);

                    if(jsonObject.length() == 0) {

                        Toast.makeText(context, "No Tags Found ", Toast.LENGTH_SHORT).show();

                    }else{
                        for (int i = 0; i < jsonObject.length(); i++) {
                            JSONObject tag = jsonObject.getJSONObject(i);
                            toHash.put( tag.getString("TagName"), tag.getInt("TagID"));

                        }
                        DBConnect.setUptags(context, toHash,ct, itemID);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error Getting Tags from Database "+ error, Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(request);


    }



    public static void getTags(final Context context,   List<String> tagList,  List<Integer> tagIDs ){
        final List<Integer> tids = tagIDs;
        final List<String> tl = tagList;
        String url = urlHead + "get_tags.php";

       requestQueue = Volley.newRequestQueue(context);
        request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonObject = new JSONArray(response);

                    if(jsonObject.length() == 0) {

                        Toast.makeText(context, "No Tags Found ", Toast.LENGTH_SHORT).show();

                    }else{
                        for (int i = 0; i < jsonObject.length(); i++) {
                            JSONObject tag = jsonObject.getJSONObject(i);

                            tids.add(tag.getInt("TagID"));
                            tl.add(tag.getString("TagName"));


                            if(adapterThis != null)
                                adapterThis.notifyDataSetChanged();
                        }
                        Collections.sort(tl);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error Getting Tags from Database "+ error, Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(request);


    }
    public static void addTags(final Context context, String nTag){
        final String tagtag = nTag;
        String url = urlHead + "add_item_stats.php";
        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("SUCCESS")){
                        Toast.makeText(context,"SUCCESS "+jsonObject.getString("SUCCESS"),Toast.LENGTH_SHORT).show();
                    }else {

                        Toast.makeText(context, "Error" +jsonObject.getString("FAILED"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error Adding Tag to Database "+ error, Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("op", "addNewTags");
                hashMap.put("tag", tagtag);

                return hashMap;
            }
        };
        requestQueue.add(request);


    }


    public static void addItemTags(final Context context, int itemIDs, int tagID){
        final String tagtag = Integer.toString(tagID);
        final String itemId = Integer.toString(itemIDs);
        String url = urlHead + "add_item_stats.php";
        Toast.makeText(context,"adding tag TagId: "+ tagtag + " ItemId: " + itemId,Toast.LENGTH_SHORT).show();
        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("SUCCESS")){
                        Toast.makeText(context,"SUCCESS "+jsonObject.getString("SUCCESS"),Toast.LENGTH_SHORT).show();
                    }else {

                        Toast.makeText(context, "Error " +jsonObject.getString("FAILED"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error Adding Tag to Database "+ error, Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("op", "addPlantTag");
                hashMap.put("itemID",itemId );
                hashMap.put("TagID", tagtag);


                return hashMap;
            }
        };
        requestQueue.add(request);


    }

    public static void addItem(final Context context, final String name, final double price, final int lowNum,  List<String> checkedTags,  List<String> otherNameList,  List<String> languageList){

        final List<String> on = otherNameList;
        final List<String> ll = languageList;
        final List<String> ct = checkedTags;
        String url = urlHead + "add_item.php";


        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("itemID")){
                        Toast.makeText(context, "SUCCESS " +jsonObject.getString("itemID"), Toast.LENGTH_SHORT).show();


                     for (int i = 0 ; i < on.size(); i++){
                         addOtherNames(context, ll.get(i), on.get(i), jsonObject.getInt("itemID"));

                     }
                     HashMap<String, Integer> toHash = new HashMap<>();

                     DBConnect.getTagsForId(context, toHash, ct,jsonObject.getInt("itemID") );





                    }else {
                        Toast.makeText(context, "Error" +jsonObject.getString("FAILED"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error Adding Item to Database "+ error, Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("itemName", name);
                hashMap.put("itemPrice",Double.toString(price));
                hashMap.put("lowNum", Integer.toString(lowNum));

                return hashMap;
            }
        };
        requestQueue.add(request);

        //;
    }

    public static void addOtherNames(final Context context,  final String language, final String name, final int itemIDs){

        Toast.makeText(context,"adding other names" ,Toast.LENGTH_SHORT).show();
        String url = urlHead + "add_other_names.php";
        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("SUCCESS")){
                        Toast.makeText(context,"SUCCESS: "+jsonObject.getString("SUCCESS"),Toast.LENGTH_SHORT).show();
                    }else {

                        Toast.makeText(context, "Error " +jsonObject.getString("FAILED"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error Adding Name to Database "+ error, Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("Name", name);
                hashMap.put("Language",language);
                hashMap.put("itemID", Integer.toString(itemIDs));


                return hashMap;
            }
        };
        requestQueue.add(request);



    }

    public static void getAllItems(final Context context, final ArrayList<Item> items){

        String url = urlHead + "get_all_items.php";
        requestQueue = Volley.newRequestQueue(context);
        request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Toast.makeText(context, "Retrieving Items from Database", Toast.LENGTH_SHORT).show();
                    JSONArray jsonObject = new JSONArray(response);

                    if(jsonObject.length() == 0) {

                        Toast.makeText(context, "No Items Found in Database ", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(context, "Retrieving Items from Database... " +jsonObject.length(), Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < jsonObject.length(); i++) {
                            JSONObject item = jsonObject.getJSONObject(i);


                            int itemID = item.getInt("id");
                            String mainName = item.getString("name");
                            double price = item.getDouble("price");


                            int lowNum = item.getInt("lowNum");

                            int itemAmount = item.getInt("AmountAvailable");

                            ArrayList<String> ONlist = new ArrayList<>();
                            ArrayList<String> languages = new ArrayList<>();
                            ArrayList<String> tags = new ArrayList<>();

                            JSONArray names = item.getJSONArray("othernames");



                            JSONArray tag = item.getJSONArray("tags");


                            for (int j = 0; j < tag.length(); j++){
                                    tags.add(tag.getString(j));

                            }
                            for (int k = 0; k < names.length(); k++){
                                JSONObject name = names.getJSONObject(k);
                                ONlist.add(name.getString("otherN"));
                                languages.add(name.getString("language"));

                            }

                           Item it = new Item(itemID, mainName,ONlist,languages,tags,itemAmount,price,lowNum,"",null);




                           items.add(it);



                        }

                        ListFragment.adapter.notifyDataSetChanged();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error Getting Items from Database "+ error, Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(request);


    }

    public static void updateItem(final Context context,  final Item item){
        String url = urlHead + "update_item.php";
        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("SUCCESS")){
                        Toast.makeText(context,"SUCCESS: "+jsonObject.getString("SUCCESS"),Toast.LENGTH_SHORT).show();
                    }else {

                        Toast.makeText(context, "Error " +jsonObject.getString("FAILED"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error Adding Name to Database "+ error, Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("itemID", Integer.toString(item.getItemID()));
                hashMap.put("name", item.getMainName());
                hashMap.put("price", Double.toString(item.getPrice()));



                return hashMap;
            }
        };
        requestQueue.add(request);





    }
    public static void sendEmail(final Context context, final String email){


        String url = urlHead + "export_to_csv.php";


        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("SUCCESS")){

                        Toast.makeText(context, "Succes: " +jsonObject.getString("SUCCESS"), Toast.LENGTH_SHORT).show();


                    }else {
                        Toast.makeText(context, "Error Message: " +jsonObject.getString("FAILED"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error sending email "+ error, Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("email", email);


                return hashMap;
            }
        };
        requestQueue.add(request);


    }



}
