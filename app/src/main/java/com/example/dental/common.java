package com.example.dental;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.ViewGroup;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class common {

    public static Dialog dialog;

    public static void create_pd(Context c)
    {
//      progressDialog=new ProgressDialog(c);
//      progressDialog.setTitle(null);
//      progressDialog.setMessage(null);
//      progressDialog.setCanceledOnTouchOutside(false);


        dialog=new Dialog(c);
        dialog.setContentView(R.layout.x);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
    }
    public static void show()
    {
        dialog.show();
    }
    public static void dism()
    {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();
            }}, 1000);


    }

    public static JSONArray send_req(String ip, String qry)
    {
        JSONArray jsonArr = null;
        StrictMode.enableDefaults();
        try {
            HttpClient httpClient=new DefaultHttpClient();
            String url=ip+qry;
            HttpPost httpPost=new HttpPost(url.replace(" ","%20").replace("'","%27"));
            HttpResponse response=httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
//            String obj=result.replace("[","").replace("]","");

            jsonArr = new JSONArray(result);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.d("er", "onCreate: "+e);
        }
        return jsonArr ;
    }

    public  static void insert_diagnois(String img_data,String img_id)
    {
        HttpClient httpClient=new DefaultHttpClient();
        HttpPost httpPost=new HttpPost("http://dentaldb.42web.io/img_sql.php");
        try {
        List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("imgData",img_data));
            nameValuePairs.add(new BasicNameValuePair("ImgId",img_id));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response=httpClient.execute(httpPost);
        } catch (IOException e)
        {

        }
    }





}
