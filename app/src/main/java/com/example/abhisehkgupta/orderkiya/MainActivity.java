package com.example.abhisehkgupta.orderkiya;
//login and registration
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import static android.content.Context.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    EditText et_mobile,et_pass;
    Button bt_login;
    TextView txtNewUser;

    public static String MyPREFERENCES = "MyPrefs" ;
   // public static String Mobile = "mobileKey";
   // public static String Password = "passKey";
    SharedPreferences sharedpreferences;

    //defining the API URL inorder have the connectivity between the API and application
    public static String LOGIN_URL = "http://192.168.43.15/TestAs/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_mobile=findViewById(R.id.et_mobile);
        et_pass=findViewById(R.id.et_pass);
        bt_login=findViewById(R.id.bt_login);
        txtNewUser=findViewById(R.id.txtNewUser);
        bt_login.setOnClickListener(this);
        txtNewUser.setOnClickListener(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        //this condition is used so that if a user exits the application and next time when comes
        // he may not have to login again and again
        if(sharedpreferences != null && sharedpreferences.getBoolean("loggedIn",false))
        {
            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v)
    {
      if(v.getId()==R.id.bt_login)
      {
          String mobile = et_mobile.getText().toString();
          String pass  = et_pass.getText().toString();

           if (et_mobile.getText().toString().equals("") && et_pass.getText().toString().equals(""))
           {
               //new AlertDialog.Builder(MainActivity.this).setTitle("Error").setMessage("Entries  can't be empty").setPositiveButton("OK",
             Toast.makeText(MainActivity.this, "Please enter the details", Toast.LENGTH_SHORT).show();
             return;
           }

           else if (et_mobile.getText().toString().equals(""))
           {
             // new AlertDialog.Builder(MainActivity.this).setTitle("Error").setMessage("UserId Can't be Empty").setPositiveButton("OK", null).show();
              Toast.makeText(MainActivity.this, "UserID cannot be left empty", Toast.LENGTH_SHORT).show();
              return;
           }

          else if (et_pass.getText().toString().equals(""))
              //   new AlertDialog.Builder(MainActivity.this).setTitle("Error").setMessage("Password Can't be Empty").setPositiveButton("OK", null).show();
          {
              Toast.makeText(MainActivity.this, "Password cannot be left empty", Toast.LENGTH_SHORT).show();
              return;
          }

          else if(et_mobile.getText().toString().length()<10)
           {
               Toast.makeText(MainActivity.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
               return;
           }

          else
              {
                  //post method is executed which takes the entries being filled by the user and pass it on JSON parsing
                  new SendPostRequest().execute(mobile,pass);
              }
      }

      else if(v.getId()==R.id.txtNewUser)
      {
          Intent intent = new Intent(getApplicationContext(),Register.class);
          startActivity(intent);
      }

    }

     //AsyncTask<Params, Progress, Result>


    public class SendPostRequest extends AsyncTask<String, Void, String>
    {
        protected void onPreExecute()
        {
         //invoked on the UI thread before the task is executed.
            // This step is normally used to setup the task, for instance by showing a progress bar in the user interface.
        }


        protected String doInBackground(String... arg)
        {
            //this method is used to do work in the background where all the internal working is hiiden from the user.
           // invoked on the background thread immediately after onPreExecute() finishes executing.
            // This step is used to perform background computation that can take a long time.
            try
            {

                URL url = new URL(LOGIN_URL); //here is THE URL path

                //data can be carried by two ways: 1: XML 2:JSON
                //here JSON is used to carry the data

                //defining JSON object which would carry data

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("mobile", arg[0]);
                postDataParams.put("password", arg[1]);

                //entries being made
                Log.e("params",postDataParams.toString());

                //here http connection is set up
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));
                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK)
                {
                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();
                }

                else
                    {
                      return new String("false : "+responseCode);
                    }
            }
            catch(Exception e)
            {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result)
        {
            //invoked on the UI thread after the background computation finishes.
            // The result of the background computation is passed to this step as a parameter.
            getResponse(result);
            //Toast.makeText(getApplicationContext(), result,Toast.LENGTH_LONG).show();
        }
    }

    public String getPostDataString(JSONObject params) throws Exception
    {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext())
        {

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    public void getResponse(String res)
    {
        try
        {
            //declaring a JSON Object under which two  parameters are passed
            //which are success and message

            JSONObject object = new JSONObject(res);

            int success = object.getInt("success");
            String msg = object.getString("message");
            if(success == 1)
            {
                JSONObject objUser = object.getJSONObject("user");
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("mobile", objUser.getString("mobile"));
                editor.putString("name", objUser.getString("name"));
                editor.putInt("userType", objUser.getInt("user_type"));
                editor.putBoolean("loggedIn", true);
                editor.commit();
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
                finish();

            }
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

}
