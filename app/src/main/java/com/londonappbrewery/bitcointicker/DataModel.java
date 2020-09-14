package com.londonappbrewery.bitcointicker;

import org.json.JSONException;
import org.json.JSONObject;

public class DataModel {
    private static float price;

    public static DataModel fromJson(JSONObject response) {

        DataModel object = new DataModel();

        try {
            price = (float) (response.getDouble("last"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    public float getPrice() {
        return price;
    }
}
