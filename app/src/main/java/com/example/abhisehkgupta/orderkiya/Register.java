package com.example.abhisehkgupta.orderkiya;
//registration
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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

public class Register extends AppCompatActivity implements View.OnClickListener
{
    EditText et_name, et_mobile, et_add, et_pass;
    Button bt_reg;
    RadioButton radiobt_customer,radiobt_seller;
    // Progress Dialog
   // private ProgressDialog pDialog;
    // JSONParser jsonParser = new JSONParser();
    int userType=0;

    public static String REGISTER_URL = "http://192.168.43.15/TestAs/register.php";

    public static String MyPREFERENCES = "MyPrefs" ;
//    public static String Name="nameKey";
//    public static String Address="addKey";
//    public static String Mobile = "mobileKey";
//    public static String Password = "passKey";
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et_name = findViewById(R.id.et_name);
        et_mobile = findViewById(R.id.et_mobile);
        et_add = findViewById(R.id.et_add);
        et_pass = findViewById(R.id.et_pass);
        radiobt_customer=findViewById(R.id.radiobt_customer);
        radiobt_seller=findViewById(R.id.radiobt_seller);
        bt_reg=findViewById(R.id.bt_reg);

        bt_reg.setOnClickListener(this);
        radiobt_customer.setOnClickListener(this);
        radiobt_seller.setOnClickListener(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

    }

    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.bt_reg)
        {
            String name = et_name.getText().toString();
            String mobile = et_mobile.getText().toString();
            String address = et_add.getText().toString();
            String pass = et_pass.getText().toString();
            if(radiobt_seller.isChecked() == true)
            {
                userType = 1;  //Shopkeeper
            }
            else
            {
                userType = 0;  //Customer
            }

            if (name.length()==0)
            {
                Toast.makeText(Register.this,"Please enter your name",Toast.LENGTH_SHORT).show();
                return;
            }
            else if (mobile.length()!=10)
            {
                Toast.makeText(Register.this,"Please enter a valid mobile number",Toast.LENGTH_SHORT).show();
                return;
            }
            else if (address.length()==0)
            {
                Toast.makeText(Register.this,"Please enter your address",Toast.LENGTH_SHORT).show();
                return;
            }
            else if (pass.length()<8)
            {
                Toast.makeText(Register.this,"Password must be of minimum 8 characters",Toast.LENGTH_SHORT).show();
                return;
            }

            new SendPostRequest().execute(name,mobile,pass,address);

//            SharedPreferences.Editor editor = sharedpreferences.edit();
//            editor.putString(Name, name);
//            editor.putString(Mobile, mobile);
//            editor.putString(Address, address);
//            editor.putString(Password, pass);
//            editor.commit();
//            Toast.makeText(Register.this, "Registered", Toast.LENGTH_LONG).show();
        }


    }

    public void onRadioButtonClicked(View view)
    {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId())
        {
            case R.id.radiobt_customer:
                if (checked)
                    break;

            case R.id.radiobt_seller:
                if (checked)
                    break;
        }
    }

    /**
     * Background Async Task to Create new product
     * */
    public class SendPostRequest extends AsyncTask<String, Void, String>
    {
         protected void onPreExecute() {}

        protected String doInBackground(String... arg)
        {
            try
            {
                URL url = new URL(REGISTER_URL);// here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("name", arg[0]);
                postDataParams.put("mobile", arg[1]);
                postDataParams.put("password", arg[2]);
                postDataParams.put("address", arg[3]);
                postDataParams.put("user_type", userType);

                //entries being made
                Log.e("params",postDataParams.toString());

                //establishing the connection

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");

                conn.setDoInput(true);
                conn.setDoOutput(true);

                //WriterStream and OutputStream are used to write the data here
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
             getResData(result);
//            Toast.makeText(getApplicationContext(), result,Toast.LENGTH_LONG).show();
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

    public void getResData(String res)
    {
        try
        {
            JSONObject obj = new JSONObject(res);
            int success = obj.getInt("success");
            String msg = obj.getString("message");
            if(success == 1)
            {
                startActivity(new Intent(Register.this,MainActivity.class));
                finish();
            }
            Toast.makeText(Register.this,msg,Toast.LENGTH_SHORT).show();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
