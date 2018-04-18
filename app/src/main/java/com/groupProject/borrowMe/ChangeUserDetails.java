package com.groupProject.borrowMe;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangeUserDetails extends AppCompatActivity {

    String newName, newPassword, Email, newPhone, newCity, newAddress, newPostcode ;
    EditText email,password,name,phone,city,address,postcode;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_details);


        update = (Button) findViewById(R.id.bUpdate);

        name = (EditText) findViewById(R.id.etName);

        phone = (EditText) findViewById(R.id.etPhone);

        password = (EditText) findViewById(R.id.etPassword);

        address = (EditText) findViewById(R.id.etAddress);


        city = (EditText) findViewById(R.id.etCity);


        postcode = (EditText) findViewById(R.id.etPostcode);


        Intent intent = getIntent();
        newName = intent.getStringExtra("name");
        Email = intent.getStringExtra("email");
        newPhone = intent.getStringExtra("phone");
        newAddress = intent.getStringExtra("address");
        newCity = intent.getStringExtra("city");
        newPostcode = intent.getStringExtra("postcode");
        newPassword = intent.getStringExtra("password");

        password.setText(newPassword);

        name.setText(newName);
        phone.setText(newPhone);
        address.setText(newAddress);
        city.setText(newCity);
        postcode.setText(newPostcode);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               GetDataFromEditText();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject( response );
                            boolean success = jsonResponse.getBoolean( "success" );
                            if (success) {

                                Intent intent = new Intent( ChangeUserDetails.this, UserDetails.class );
                                ChangeUserDetails.this.startActivity( intent );
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder( ChangeUserDetails.this );
                                builder.setMessage( "Update Failed" )
                                        .setNegativeButton( "Retry", null )
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                UpdateRequest registerRequest = new UpdateRequest( Email, newPassword, newName, newPhone, newAddress, newCity, newPostcode, responseListener );
                RequestQueue queue = Volley.newRequestQueue( ChangeUserDetails.this );
                queue.add( registerRequest );


            }
        });

    }

    public void GetDataFromEditText(){

        newName = name.getText().toString();
        newPassword = password.getText().toString();
        newPhone = phone.getText().toString();
        newAddress = address.getText().toString();
        newCity = city.getText().toString();
        newPostcode = postcode.getText().toString();


    }
}
