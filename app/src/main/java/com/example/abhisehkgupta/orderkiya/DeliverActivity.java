package com.example.abhisehkgupta.orderkiya;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

public class DeliverActivity extends AppCompatActivity
{
    TextView txtDeliverOrder;
    RecyclerView recyler_deliverlist;
    List<DeliverOrderData> deliverOrderDataList;
    JSONArray orderIds;


    public static String DELIVER_LIST ="http://192.168.43.15/TestAs/order_list.php";
    public static String DELIVER_ORDER ="http://192.168.43.15/TestAs/deliver_order.php";
    SharedPreferences sharedpreferences;
    public static String MyPREFERENCES = "MyPrefs" ;

    private String fromMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver);

        txtDeliverOrder=findViewById(R.id.txtDeliverOrder);
        recyler_deliverlist=findViewById(R.id.recyler_deliverlist);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        fromMobile = getIntent().getStringExtra("mobile");

        deliverOrderDataList = new ArrayList<>();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyler_deliverlist.setLayoutManager(mLayoutManager);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(this,1);
        recyler_deliverlist.addItemDecoration(dividerItemDecoration);
        DeliverOrderAdapter adapter = new DeliverOrderAdapter(deliverOrderDataList,this);
        recyler_deliverlist.setAdapter(adapter);


        new SendPostRequest().execute(sharedpreferences.getString("mobile",""),fromMobile);

        txtDeliverOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderIds = new JSONArray();
                for(int i=0;i<deliverOrderDataList.size();i++)
                {
                    if(deliverOrderDataList.get(i).checked)
                    {
                        orderIds.put(deliverOrderDataList.get(i).orderId);
                    }
                }
                if(orderIds.length()>0)
                {

                    new UpdatePostRequest().execute();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Select at least 1 item",Toast.LENGTH_SHORT).show();
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
                URL url = new URL(DELIVER_LIST);// here is your URL path

                JSONObject postDataParams = new JSONObject();
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


                if(success == 1)
                {
                   //it is used to iterate the list to the screen from the API
                    JSONArray jsonArray = jsonObject.getJSONArray("orders");

                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            String orderNo = obj.getString("order_id");
                            String itemName = obj.getString("item_name");
                            String qty = obj.getString("quantity");
                            String order_date = obj.getString("order_date");
                            String unit = obj.getString("unit");
                            String status=obj.getString("order_status");
                            DeliverOrderData deliverOrderData = new DeliverOrderData(orderNo,itemName,qty,order_date,unit,status);
                            deliverOrderDataList.add(deliverOrderData);
                        }

                        recyler_deliverlist.getAdapter().notifyDataSetChanged();


                }
                Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }




    public class UpdatePostRequest extends AsyncTask<String, Void, String>
    {

        protected void onPreExecute() {}
        @Override
        protected String doInBackground(String... arg0)
        {
            try
            {
                URL url = new URL(DELIVER_ORDER);// here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("order_ids", orderIds);


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


                if(success == 1)
                {


                }
                Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();

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






