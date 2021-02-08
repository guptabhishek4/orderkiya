package com.example.abhisehkgupta.orderkiya.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.abhisehkgupta.orderkiya.Constants;
import com.example.abhisehkgupta.orderkiya.R;
import com.example.abhisehkgupta.orderkiya.RecyclerAdapter;
import com.example.abhisehkgupta.orderkiya.User;

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

public class OrderHistory extends Fragment
{

    public OrderHistory()
    {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    List<User> userList;
    SharedPreferences sharedpreferences;
    public static String MyPREFERENCES = "MyPrefs" ;
    int userTypeToList;



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        //System.out.println("oncreateeee");

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        recyclerView = root.findViewById(R.id.recycler_userlist);
        userList = new ArrayList<>();

        if(sharedpreferences.getInt("userType",0) == 0)
        {
            userTypeToList = 1;
        }
        else
        {
            userTypeToList = 0;
        }

        new  SendPostRequest().execute(""+userTypeToList);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),1);
        recyclerView.addItemDecoration(dividerItemDecoration);
        RecyclerAdapter adapter = new RecyclerAdapter(userList,getActivity(),recyclerView);
        recyclerView.setAdapter(adapter);


        // Inflate the layout for this fragment
        return root;
    }



    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }

    public class SendPostRequest extends AsyncTask<String, Void, String>
    {
        protected void onPreExecute()
        {
            //invoked on the UI thread before the task is executed.
            // This step is normally used to setup the task, for instance by showing a progress bar in the user interface.
        }

        protected String doInBackground(String... arg0)
        {
            // invoked on the background thread immediately after onPreExecute() finishes executing.
            // This step is used to perform background computation that can take a long time.

            try
            {

                URL url = new URL(Constants.USER_LIST);// here is THE URL path

                //data can be carried by two ways: first: XML second:JSON
                //here JSON is used to carry the data

                //defining JSON object which would carry data
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("user_type", arg0[0]);
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

            System.out.println(result);
            getResponse(result);
            // Toast.makeText(getApplicationContext(), result,Toast.LENGTH_LONG).show();
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
            JSONObject object = new JSONObject(res);

            int success = object.getInt("success");
            String msg = object.getString("message");
            Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
            if(success == 1)
            {
                JSONArray jsonArray = object.getJSONArray("user");
                if(jsonArray !=null && jsonArray.length()>0)
                {
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String name = obj.getString("name");
                        String mobile = obj.getString("mobile");
                        String address = obj.getString("address");
                        String userType = obj.getString("user_type");
                        User user = new User(name,mobile,address,userType);
                        userList.add(user);
                    }

                    recyclerView.getAdapter().notifyDataSetChanged();
                }

            }

        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
