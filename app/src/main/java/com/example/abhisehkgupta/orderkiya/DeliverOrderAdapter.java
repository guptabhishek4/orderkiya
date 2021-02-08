package com.example.abhisehkgupta.orderkiya;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;
public class DeliverOrderAdapter extends RecyclerView.Adapter<DeliverOrderAdapter.ViewHolderItem>
{
    private List<DeliverOrderData> deliverOrderDatalist;
    private Context context;
    private RecyclerView RecyclerV;



    //idhar DeliverOrderData reference type pass hua hai LIST ke liye jis mein custom type pass ho rha hai USER
    public DeliverOrderAdapter(List<DeliverOrderData> deliverOrderDataList, Context context) //RecyclerView recyler_deliverlist)
    {
        this.deliverOrderDatalist = deliverOrderDataList;
        this.context = context;
       // RecyclerV = recyler_deliverlist;
    }

  //  public DeliverOrderAdapter(List<DeliverOrderData> deliverOrderDataList, DeliverActivity deliverActivity) {
    //}


    @NonNull
    @Override
    public ViewHolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.item_deliverlist, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolderItem viewHolder = new ViewHolderItem(v);
        return viewHolder;
    }


    public class ViewHolderItem extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public TextView txt_deliver_item;
        public TextView txt_qty;
        public TextView txt_order_date;
        public TextView txt_unit;
        public TextView txt_status;
        public CheckBox checkBox;

        public View layout;

        public ViewHolderItem(View v)
        {
            super(v);
            layout = v;
            txt_deliver_item = v.findViewById(R.id.txt_deliver_item);
            txt_qty =  v.findViewById(R.id.txt_qty);
            txt_order_date = v.findViewById(R.id.txt_order_date);
            txt_unit=v.findViewById(R.id.txt_unit);
            txt_status=v.findViewById(R.id.txt_status);
            checkBox=v.findViewById(R.id.checkBox);

        }
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolderItem holder, int position)
    {
//        final User person = PeopleList.get(position);
//        holder.txtName.setText("Name: " + person.name);
//        holder.txtmobile.setText("Mobile: " + person.mobile);
//        holder.txtAddress.setText(person.address);
//        holder.itemView.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//
//                if(person.userType != null && person.userType.equals("0"))
//                {
//                    Intent in = new Intent(context,OrderActivity.class);
//                    in.putExtra("mobile",person.mobile);
//                    context.startActivity(in);
//                }
//            }
//        });

        final DeliverOrderData customer = deliverOrderDatalist.get(position);
        holder.txt_deliver_item.setText("Item: " + customer.deliver_item);
        holder.txt_qty.setText("Quantity: " + customer.qty);
        holder.txt_order_date.setText("Order Date: " + customer.orderdate);
        holder.txt_unit.setText("Units: " + customer.units);
        holder.txt_status.setText("Status: " + customer.status);
        if(customer.status !=null && customer.status.equals("Delivered"))
        {
            holder.checkBox.setVisibility(View.GONE);
        }
        else
        {
            holder.checkBox.setVisibility(View.VISIBLE);
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                customer.checked = isChecked;
            }
        });



    }


    @Override
    public int getItemCount()
    {
        return deliverOrderDatalist.size();
    }
}
