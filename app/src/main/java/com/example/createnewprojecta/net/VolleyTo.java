package com.example.createnewprojecta.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.createnewprojecta.AppClient;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Administrator on 2019/10/13 0013.
 */

public class VolleyTo extends Thread {
    private String Url="http://"+ AppClient.getIp()+":"+ AppClient.getDk()+"/Createteacher/";
    private JSONObject jsonObject=new JSONObject();
    private boolean isLoop;
    private int Time;
    private ProgressDialog dialog;
    private VolleyLo volleyLo;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (dialog!=null&&dialog.isShowing()){
                dialog.dismiss();
            }
            return false;
        }
    });

    public VolleyTo setUrl(String url) {
        Url += url;
      /*  try {
            jsonObject.put("UserName",AppClient.getUserName());
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        return this;
    }

    public VolleyTo setJsonObject(String k, Object v) {
        try {
            jsonObject.put(k,v);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public VolleyTo setLoop(boolean loop) {
        isLoop = loop;
        return this;
    }

    public VolleyTo setTime(int time) {
        Time = time;
        return this;
    }

    public VolleyTo setDialog(Context context) {
        dialog = new ProgressDialog(context);
        dialog.setTitle("提示");
        dialog.setMessage("网络请求中。。。");
        dialog.show();
        return this;
    }

    public VolleyTo setVolleyLo(VolleyLo volleyLo) {
        this.volleyLo = volleyLo;
        return this;
    }

    @Override
    public void run() {
        super.run();
        do {
            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    volleyLo.onResponse(jsonObject);
                    handler.sendEmptyMessageDelayed(0x001,2000);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyLo.onErrorResponse(volleyError);
                    handler.sendEmptyMessageDelayed(0x001,2000);
                }
            });

            AppClient.setRequestQueue(jsonObjectRequest);
            try {
                Thread.sleep(Time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while (isLoop);
    }
}
