package com.example.createnewprojecta.net;

import com.android.volley.VolleyError;

import org.json.JSONObject;


public interface VolleyLo {
    void onResponse(JSONObject jsonObject);
    void onErrorResponse(VolleyError volleyError);
}
