package com.example.abhisehkgupta.orderkiya;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class OrderActivity extends AppCompatActivity
{
    TextView txtPlaceOrder;
    RecyclerView recyclerView;
    //List<String> unitList;
    List<PlaceOrderData> placeOrderDataList;
    public static String ORDER_LIST ="http://192.168.43.15/TestAs/place_order.php";
    JSONArray jsonArray;
    SharedPreferences sharedpreferences;
    public static String MyPREFERENCES = "MyPrefs" ;

    private String toMobile;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
       // spinner_unit = findViewById(R.id.spinner_unit);
        txtPlaceOrder =findViewById(R.id.txtPlaceOrder);
        recyclerView = findViewById(R.id.orderlist);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        toMobile = getIntent().getStringExtra("mobile");

        placeOrderDataList = new ArrayList<>();
        PlaceOrderData placeOrderData = new PlaceOrderData("","","");
        placeOrderDataList.add(placeOrderData);
        placeOrderDataList.add(placeOrderData);
        placeOrderDataList.add(placeOrderData);

        //linear layout manager is used to show the list or row item linearlly
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        //adding a divider to the list
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(this,1);
        recyclerView.addItemDecoration(dividerItemDecoration);
        PlaceOrderAdapter adapter = new PlaceOrderAdapter(placeOrderDataList,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemViewCacheSize(placeOrderDataList.size());
        recyclerView.setHasFixedSize(true);


        txtPlaceOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                jsonArray = new JSONArray();
                for(int i=0; i < placeOrderDataList.size(); i++)
                {
                    PlaceOrderData placeData = placeOrderDataList.get(i);
                    System.out.println("item "+ i +"-"+placeData.itemName);

                    try {
                        if(!placeData.itemName.equals("") && !placeData.qty.equals("")) {
                            JSONObject object = new JSONObject();
                            object.put("item_name", placeData.itemName);
                            object.put("quantity", placeData.qty);
                            object.put("unit", placeData.itemUnit);
                            jsonArray.put(object);
                        }

                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

                String fromMobile = sharedpreferences.getString("mobile","");

                if(jsonArray.length() > 0)
                {
                    new SendPostRequest().execute(toMobile, fromMobile);
                }

            }

        });


    }

    public class SendPostRequest extends AsyncTask<String, Void, String>
    {

        protected void onPreExecute() {}
        @Override
        protected String doInBackground(String... arg0)
        {
            try
            {
                URL url = new URL(ORDER_LIST);// here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("order_data", jsonArray);
                postDataParams.put("order_to", arg0[0]);
                postDataParams.put("order_from", arg0[1]);

                //entries being made
                Log.e("params",postDataParams.toString());

                //establishing the connection
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
//                conn.setRequestProperty("Content-Type","application/json");
//                conn.setRequestProperty("charset", "utf-8");
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
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            try
            {
                JSONObject jsonObject = new JSONObject(s);
                int success = jsonObject.getInt("success");
                String msg = jsonObject.getString("message");

                Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
                if(success == 1)
                {


                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
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
}


