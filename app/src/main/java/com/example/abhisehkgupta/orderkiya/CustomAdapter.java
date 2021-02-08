package com.example.abhisehkgupta.orderkiya;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    public static ArrayList<PlaceOrderData> editModelArrayList;


    public CustomAdapter(Context ctx, ArrayList<PlaceOrderData> editModelArrayList){

        inflater = LayoutInflater.from(ctx);
        this.editModelArrayList = editModelArrayList;
    }

    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_orderlist, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final CustomAdapter.MyViewHolder holder, final int position) {


        holder.edtItem.setText(editModelArrayList.get(position).itemName);
        holder.edtQty.setText(editModelArrayList.get(position).qty);
        Log.d("print","yes");

    }

    @Override
    public int getItemCount() {
        return editModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private EditText edtItem;
        private EditText edtQty;
        public Spinner spinnerUnit;
        public ImageView removefield;

        public MyViewHolder(View itemView) {
            super(itemView);

            edtItem = itemView.findViewById(R.id.et_item_name);
            edtQty =  itemView.findViewById(R.id.et_qty);
            spinnerUnit = itemView.findViewById(R.id.spinner_unit);
            removefield=itemView.findViewById(R.id.remove_field);

            edtItem.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    editModelArrayList.get(getAdapterPosition()).itemName = edtItem.getText().toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            edtQty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    editModelArrayList.get(getAdapterPosition()).qty = edtQty.getText().toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }

    }
}
