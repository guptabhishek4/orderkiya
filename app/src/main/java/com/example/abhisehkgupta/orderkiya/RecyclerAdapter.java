package com.example.abhisehkgupta.orderkiya;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
{
    private List<User> PeopleList;
    private Context context;
    private RecyclerView RecyclerV;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public TextView txtName;
        public TextView txtmobile;
        public TextView txtAddress;



        public View layout;

        public ViewHolder(View v)
        {
            super(v);
            layout = v;
            txtName = v.findViewById(R.id.txtName);
            txtmobile =  v.findViewById(R.id.txtmobile);
            txtAddress = v.findViewById(R.id.txtAddress);

        }
    }

    //idhar dataset reference type pass hua hai LIST ke liye jis mein custom type pass ho rha hai USER
    public RecyclerAdapter(List<User> Dataset, Context context, RecyclerView recycler_userlist)
    {
        PeopleList = Dataset;
        this.context = context;
        RecyclerV = recycler_userlist;
    }


    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.item_userlist, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        final User person = PeopleList.get(position);
        holder.txtName.setText("Name: " + person.name);
        holder.txtmobile.setText("Mobile: " + person.mobile);
        holder.txtAddress.setText(person.address);
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(person.userType != null && person.userType.equals("0"))
                {
                    Intent in = new Intent(context,DeliverActivity.class);
                    in.putExtra("mobile",person.mobile);
                    context.startActivity(in);

                }
                else
                {
                    Intent in = new Intent(context,OrderActivity.class);
                    in.putExtra("mobile",person.mobile);
                    context.startActivity(in);
                }
            }
        });
    }




    @Override
    public int getItemCount()

    {
        return PeopleList.size();
    }
}
