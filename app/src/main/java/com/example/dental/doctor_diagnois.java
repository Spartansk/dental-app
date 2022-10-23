package com.example.dental;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class doctor_diagnois extends AppCompatActivity {

    String ip;
    String opp_id;
    String name ,status;
    TextView name_tv;
    ImageView cam,pic;
    EditText advice;
    TextView diagonois_tx;
    TextView problem_tv;
    String d_advice , p_problem;

    private static final int pic_id = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_diagnois);


        SharedPreferences sharedPreferences= getSharedPreferences("myPref", MODE_PRIVATE);
        ip=sharedPreferences.getString("ip", "");
        common.create_pd(doctor_diagnois.this);

        Bundle extras = getIntent().getExtras();
        opp_id=extras.getString("opp_id");
        name=extras.getString("name");
        status=extras.getString("status");
        Toast.makeText(this, ""+opp_id, Toast.LENGTH_SHORT).show();

        name_tv=findViewById(R.id.name);
        name_tv.setText(name);

        cam=findViewById(R.id.cam);
        pic=findViewById(R.id.pic);

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Start the activity with camera_intent, and request pic id
                startActivityForResult(camera_intent, pic_id);
            }
        });
        advice=findViewById(R.id.advice);
        diagonois_tx=findViewById(R.id.diagnois);
        diagonois_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**send to db*/
                d_advice=advice.getText().toString();
//                BitmapDrawable bitmapDrawable = (BitmapDrawable) pic.getDrawable();
////            Bitmap bitmap = bitmapDrawable.getBitmap();
////            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
////            bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
////            byte[] bytes = byteArrayOutputStream.toByteArray();
////            String image = Base64.encodeToString(bytes , Base64.DEFAULT);
////           // Log.d("image byte", "doInBackground: "+image);

                new bg("insert").execute();

            }
        });

        problem_tv=findViewById(R.id.problem);
        new bg("fetch").execute();
        if(status.contains("done"))
        {
            advice.setClickable(false);
            diagonois_tx.setVisibility(View.INVISIBLE);
            new bg("fetch").execute();
        }


    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Match the request 'pic id with requestCode
        if (requestCode == pic_id) {
            // BitMap is data structure of image file which store the image in memory
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // Set the image in imageview for display
            pic.setImageBitmap(photo);
        }
    }

    class bg extends AsyncTask<Object,Void,Void>
    {

        private String action;

        bg(String action) {
            this.action = action;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            common.show();
        }

        @Override
        protected Void doInBackground(Object... objects) {
//            BitmapDrawable bitmapDrawable = (BitmapDrawable) pic.getDrawable();
//            Bitmap bitmap = bitmapDrawable.getBitmap();
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
//            byte[] bytes = byteArrayOutputStream.toByteArray();
//            String image = Base64.encodeToString(bytes , Base64.DEFAULT);
//           // Log.d("image byte", "doInBackground: "+image);
                    if(action.contains("insert")) {
                        common.send_req(ip, "c_qry=UPDATE appointment SET d_advice='" + d_advice + "',status='done' where opp_id='" + opp_id + "'");
                    }
                    else  if(action.contains("fetch"))
                    {
                        JSONArray jsonArr;
                        jsonArr=  common.send_req(ip,"c_qry=SELECT  * FROM appointment where opp_id='"+opp_id+"'");

                        for (int i = 0; i < jsonArr.length(); i++)
                        {
                            JSONObject jsonObj = null;
                            try {
                                jsonObj = jsonArr.getJSONObject(i);
                                Log.d("appointments", "jarray: : "+jsonObj);
                                p_problem=jsonObj.getString("problem");
                                d_advice=jsonObj.getString("d_advice");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            common.dism();
            advice.setText(d_advice);
            problem_tv.setText(p_problem);

        }
    }

}