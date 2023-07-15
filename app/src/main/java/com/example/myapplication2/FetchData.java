package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FetchData extends AppCompatActivity {
    private EditText fname,lname;
    private Button add;
    
    private String BaseUrl="https://digitaldectectives.azdigi.blog/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fetdata_layout);

        TextView fname_get = findViewById(R.id.fname_get);
        TextView lname_get = findViewById(R.id.lname_get);

        RequestQueue requestQueue = Volley.newRequestQueue(this);


        add = (Button)findViewById(R.id.fetch_btn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://digitaldectectives.azdigi.blog/fetch.php";

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("name");

                                JSONObject jsonObject = jsonArray.getJSONObject(1);
                                String fname = jsonObject.getString("fname");
                                String lname = jsonObject.getString("lname");
                                fname_get.setText(fname);
                                lname_get.setText(lname);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FetchData.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                });

                requestQueue.add(jsonObjectRequest);

            }
        });

    }
}