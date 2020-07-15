package com.example.echoseedlinginventory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

public class InsertDataPackager {



    private  String mainName;

    private String[] otherNames;

    private String[] tags;



    private double price;

    private String url;

    public InsertDataPackager( String mainName, String[] otherNames, String[] tags,  double price, String url) {

        this.mainName = mainName;
        this.otherNames = otherNames;
        this.tags = tags;

        this.price = price;
        this.url = url;

    }

    public String packData()
        {
            JSONObject jo=new JSONObject();
            StringBuffer packedData=new StringBuffer();

            try
            {
                jo.put("itemName",mainName);
                jo.put("itemOtherNames",otherNames);
                jo.put("newTags",tags);
                jo.put("itemPrice",price);
                jo.put("itemTagIDs",price);
                jo.put("imageURL", url);



                Boolean firstValue=true;

                Iterator it=jo.keys();

                do {
                    String key=it.next().toString();
                    String value=jo.get(key).toString();

                    if(firstValue)
                    {
                        firstValue=false;
                    }else
                    {
                        packedData.append("&");
                    }

                    packedData.append(URLEncoder.encode(key,"UTF-8"));
                    packedData.append("=");
                    packedData.append(URLEncoder.encode(value,"UTF-8"));

                }while (it.hasNext());

                return packedData.toString();

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return null;
        }

    }





