package com.example.abhisehkgupta.orderkiya;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
{
    private List<PastOrder> OrderHistoryList;
    private Context context;
    private RecyclerView RecyclerV;
    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
