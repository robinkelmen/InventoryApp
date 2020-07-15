package com.example.echoseedlinginventory;

import android.graphics.Bitmap;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.graphics.BitmapFactory;
import android.widget.Toast;

public class Item  implements Serializable, Comparable{

    private int itemID;

    private  String mainName;

    private ArrayList<String> otherNames;

    private ArrayList<String> languages;

    private ArrayList<String> itags;

    private int amountAvailable;

    private double price;

    private String url;
    private int lownum;

    private Bitmap itemImage;

    public Item(int itemID, String mainName, ArrayList<String> otherNames, ArrayList<String> languages, ArrayList<String> tags, int amountAvailable, double price, int lowNum, String url, Bitmap itemImage) {

        this.itemID = itemID;
        this.mainName = mainName;
        this.otherNames = otherNames;
        this.languages = languages;
        this.itags = tags;
        this.amountAvailable = amountAvailable;
        this.price = price;
        this.url = url;
        this.itemImage = itemImage;
        this.lownum = lowNum;
    }

    public int getLownum(){return lownum;}
    public void setLownum(int nln){this.lownum = nln;}
    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getMainName() {
        return mainName;
    }

    public void setMainName(String mainName) {
        this.mainName = mainName;
    }

    public ArrayList<String> getOtherNames() {
        return otherNames;
    }

    public ArrayList<String> getTags() {
        return itags;
    }

    public int getAmountAvailable() {
        return amountAvailable;
    }

    public void setAmountAvailable(int amountAvailable) {
        this.amountAvailable = amountAvailable;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setOtherNames(ArrayList<String> otherNames) {
        this.otherNames = otherNames;
    }

    public ArrayList<String> getLanguages() {
        return languages;
    }

    public void setLanguages(ArrayList<String> languages) {
        this.languages = languages;
    }

    public void setTags(ArrayList<String> tags) {
        this.itags = tags;
    }

    public Bitmap getItemImage() {
        return itemImage;
    }

    public void setItemImage(Bitmap itemImage) {
        this.itemImage = itemImage;
    }
    public String toString(){
        return mainName + " \n"
                + price + "\n"
                + otherNames + "\n"
                + languages + "\n"
                + itags + "\n";
    }

    @Override
    public int compareTo(Object o) {
        String toComp = (String)o;
        return toComp.compareTo(this.mainName);

    }
}
