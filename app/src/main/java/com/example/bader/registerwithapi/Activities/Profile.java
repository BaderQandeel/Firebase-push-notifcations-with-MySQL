package com.example.bader.registerwithapi.Activities;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.bader.registerwithapi.ApiUtilies.URLs;
import com.example.bader.registerwithapi.ApiUtilies.VolleySingleton;
import com.example.bader.registerwithapi.JavaClasses.User;
import com.example.bader.registerwithapi.R;
import com.example.bader.registerwithapi.Utilis_Notify_SharedPref.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    TextView textViewId, textViewUsername, textViewEmail, textViewGender, textViewtest;
    private Button buttonSendPush;
    private RadioGroup radioGroup;
    private Spinner spinner;
    private ProgressDialog progressDialog;

    private EditText editTextTitle, editTextMessage, editTextImage;

    private boolean isSendAllChecked;
    private List<String> devices;

    public Profile() {
    }

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        String token = SharedPrefManager.getInstance(getApplicationContext()).getDeviceToken();

        //if token is null


        loadRegisteredDevices();

        //if the user is not logged in
        //starting the login activity
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, login.class));
        }
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        spinner = (Spinner) findViewById(R.id.spinnerDevices);
        buttonSendPush = (Button) findViewById(R.id.buttonSendPush);

        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        editTextImage = (EditText) findViewById(R.id.editTextImageUrl);

        devices = new ArrayList<>();
        textViewtest = (TextView) findViewById(R.id.texttest);
        textViewId = (TextView) findViewById(R.id.textViewId);
        textViewUsername = (TextView) findViewById(R.id.textViewUsername);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewGender = (TextView) findViewById(R.id.textViewGender);

        ///EDIT UI WHEN FIREBASE RECVIED by local brodacst
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("RECIVED_MESSAGE", "q");
                // checking for type intent filter
                if (intent.getAction().equals("RECIVED_MESSAGE")) {
                    String message = intent.getStringExtra("title");
                    Log.d("RECIVED_MESSAGE", "qq");
                    Toast.makeText(context, "HHHHHHH", Toast.LENGTH_SHORT);
                    buttonSendPush.setText(message);

                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("RECIVED_MESSAGE"));

        buttonSendPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPush();
            }
        });
        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();

        //setting the values to the textviews
        textViewId.setText(String.valueOf(user.getId()));
        textViewUsername.setText(user.getUsername());
        textViewEmail.setText(user.getEmail());
        textViewGender.setText(user.getGender());
//        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URLs.GET_GEN,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            Log.d("VOLLEY",response);
//                            JSONArray jsonArray=new JSONArray(response);
//                            for (int i=0;i<jsonArray.length();i++){
//                                JSONObject obj=jsonArray.getJSONObject(i);
//                                textViewGender.append(obj.getString("gender"));
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("VOLLEY",error.toString());
//                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                User user = SharedPrefManager.getInstance(Profile.this).getUser();
//                Map<String, String> params = new HashMap<>();
//                params.put("gender", user.getGender());
//                return params;
//            }
//        };
//        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest1);
//        StringRequest stringRequest=new StringRequest(Request.Method.GET, URLs.GET_ALL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    Log.d("VOLLEY",response);
//                    Toast.makeText(Profile.this,response, Toast.LENGTH_SHORT);
//                    JSONArray jsonArray=new JSONArray(response);
//                    for (int i=0;i<jsonArray.length();i++){
//                        JSONObject obi=jsonArray.getJSONObject(i);
//                        textViewId.append(obi.getString("username"));
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("VOLLEY",error.toString());
//            }
//        });
//        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


//        //when the user presses logout button
        //calling the logout method
        findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                unRegisterFCM( SharedPrefManager.getInstance(getApplicationContext()).getUser().getEmail());
//                loadRegisteredDevices();
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
            }
        });
    }

    private void unRegisterFCM(String email) {

        final String emil = email;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_UN_REGISTER_FCM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TOKENSAV", error.toString());
//                        Toast.makeText(Profile.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", emil);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void loadRegisteredDevices() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Devices...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_GET_REGISTERED_FCM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                JSONArray jsonDevices = obj.getJSONArray("devices");

                                for (int i = 0; i < jsonDevices.length(); i++) {
                                    JSONObject d = jsonDevices.getJSONObject(i);
                                    devices.add(d.getString("email"));
                                }

                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                        Profile.this,
                                        android.R.layout.simple_spinner_dropdown_item,
                                        devices);

                                spinner.setAdapter(arrayAdapter);
                            }
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

        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void sendPush() {
        if (isSendAllChecked) {
            sendMultiplePush();
        } else {
            sendSinglePush();
        }
    }

    private void sendSinglePush() {
        final String title = editTextTitle.getText().toString();
        final String message = editTextMessage.getText().toString();
        final String image = editTextImage.getText().toString();
        final String email = spinner.getSelectedItem().toString();

        progressDialog.setMessage("Sending Push");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_SENDSINGLE_NOTIFY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        Toast.makeText(Profile.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("message", message);

                if (!TextUtils.isEmpty(image))
                    params.put("image", image);

                params.put("email", email);
                params.put("id1", "s");
                params.put("id2", "ss");
                params.put("id3", "sss");
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void sendMultiplePush() {
        final String title = editTextTitle.getText().toString();
        final String message = editTextMessage.getText().toString();
        final String image = editTextImage.getText().toString();

        progressDialog.setMessage("Sending Push");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_SENDMULTI_NOTIFY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        Toast.makeText(Profile.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("message", message);

                if (!TextUtils.isEmpty(image))
                    params.put("image", image);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("RECIVED_MESSAGE"));
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radioButtonSendAll:
                isSendAllChecked = true;
                spinner.setEnabled(false);
                break;

            case R.id.radioButtonSendOne:
                isSendAllChecked = false;
                spinner.setEnabled(true);
                break;

        }
    }

}