package com.example.abhisehkgupta.orderkiya;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PlaceOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<PlaceOrderData> placeOrderDataList;
    private Context context;

    List<String> unitList;

    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;

    public class ViewHolderItem extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
         EditText edtItem;
         EditText edtQty;
         Spinner spinnerUnit;
         ImageView removefield;

        public View layout;

        public ViewHolderItem(View v)
        {
            super(v);
            layout = v;
            edtItem = v.findViewById(R.id.et_item_name);
            edtQty =  v.findViewById(R.id.et_qty);
            spinnerUnit = v.findViewById(R.id.spinner_unit);
            removefield=v.findViewById(R.id.remove_field);
        }
    }

    public class ViewHolderFooter extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public Button btnAddField;
        public View layout;

        public ViewHolderFooter(View v)
        {
            super(v);
            layout = v;
            btnAddField = v.findViewById(R.id.footer_text);

        }
    }

    //idhar PlaceOrderData reference type pass hua hai LIST ke liye jis mein custom type pass ho rha hai placeOrderDataList
    public PlaceOrderAdapter(List<PlaceOrderData> placeOrderDataList, Context context)
    {
        this.placeOrderDataList = placeOrderDataList;
        this.context = context;
        addUnitList();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        if(viewType == TYPE_ITEM)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderlist, parent, false);
            return new ViewHolderItem(view);
        }
        else
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer, parent, false);
            return new ViewHolderFooter(view);
        }

        //return new ViewHolder(view);
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
////
////        View v = inflater.inflate(R.layout.item_orderlist, parent, false);
////        // set the view's size, margins, paddings and layout parameters
////        ViewHolder vh = new ViewHolder(v);
////        return vh;

    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {
        if(holder instanceof ViewHolderItem)
        {
            //defining holderitem object for ViewHolderItem class
            ViewHolderItem holderItem = (ViewHolderItem) holder;
            final PlaceOrderData placeOrderData = placeOrderDataList.get(position);
            holderItem.edtItem.setText(placeOrderData.itemName);
            holderItem.edtQty.setText(placeOrderData.qty);

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, unitList);
            holderItem.spinnerUnit.setAdapter(dataAdapter);
            holderItem.edtQty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    placeOrderData.qty = s.toString();
                }
            });

            holderItem.edtItem.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    placeOrderData.itemName = s.toString();
                }
            });



//            ((ViewHolderItem) holder).spinnerUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                    placeOrderData.itemUnit = unitList.get(position);
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });


           //removing the item/entry from recylcerview
            holderItem.removefield.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    if(placeOrderDataList.size()==1)
                        Toast.makeText(context,"One Entry has to be present",Toast.LENGTH_SHORT).show();
                    else
                    {
                        placeOrderDataList.remove(position);
                        notifyDataSetChanged();
                    }

                }
            });


        }
        else
            //adding an another field in the recylerview
            //if(holder instanceof ViewHolderFooter)
        {
            ViewHolderFooter holderFooter = (ViewHolderFooter) holder;
            holderFooter.btnAddField.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    PlaceOrderData placeOrderData = new PlaceOrderData("","","");
                    placeOrderDataList.add(placeOrderData);
                    notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public int getItemCount()
    {
        return placeOrderDataList.size() +1;
    }

    @Override
    public int getItemViewType(int position)
    {
        if (position == placeOrderDataList.size())
         {
            return TYPE_FOOTER;
         }
            return TYPE_ITEM;
    }

    public void addUnitList()
    {
        unitList = new ArrayList<String>();
        unitList.add("Kg");
        unitList.add("Grams");
        unitList.add("Litres");
        unitList.add("mL");
        unitList.add("pounds");
    }
}
