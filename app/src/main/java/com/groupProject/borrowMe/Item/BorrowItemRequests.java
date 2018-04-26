package com.groupProject.borrowMe.Item;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.groupProject.borrowMe.R;
import com.groupProject.borrowMe.adaptors.BorrowRequestAdaptor;
import com.groupProject.borrowMe.models.BorrowItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

/**
 * Created by Enache on 25/04/2018.
 */

public class BorrowItemRequests extends AppCompatActivity {


    RecyclerView recyclerView;


    List<BorrowItem> requests;



    String request_url ="https://myxstyle120.000webhostapp.com/borrowRequests.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_item_requests);


        recyclerView = (RecyclerView) findViewById(R.id.BorrowAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        requests = new ArrayList<>();

        Intent intent = getIntent();
        final String email = intent.getStringExtra( "email" );

        sendRequest(email);
    }

    public void sendRequest(final String email) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, request_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            Log.d("Content-----:",response);
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject item = array.getJSONObject(i);

                                //adding the product to product list
                                requests.add(new BorrowItem(
                                        item.getString("Borrow_ID"),
                                        item.getString("Borrower_email")

                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            BorrowRequestAdaptor adapter = new BorrowRequestAdaptor(BorrowItemRequests.this, requests);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put( "Lender_email", email ); //Add the data you'd like to send to the server.
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }


        } ;

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
