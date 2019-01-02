package com.example.ed.fireqr.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ed.fireqr.Class.QR;
import com.example.ed.fireqr.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context cx;
    private ArrayList<QR> qrArrayList;

    public MyAdapter(Context cx, ArrayList<QR> qrArrayList) {
        this.cx = cx;
        this.qrArrayList = qrArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(cx).inflate(R.layout.custom_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.mNameOfCard.setText(qrArrayList.get(position).getNameOfCard());
        holder.mName.setText(qrArrayList.get(position).getName());
        holder.mOrg.setText(qrArrayList.get(position).getOrg());
        holder.mEmail.setText(qrArrayList.get(position).getEmail());
        holder.mUrl.setText(qrArrayList.get(position).getUrl());
        holder.mTel.setText(qrArrayList.get(position).getTell());
        holder.mCell.setText(qrArrayList.get(position).getCell());
        holder.mAddress.setText(qrArrayList.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        if (qrArrayList.size() != 0) {
            return qrArrayList.size();
        }
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mNameOfCard, mName, mOrg, mEmail, mUrl, mTel, mCell, mAddress;

        MyViewHolder(View itemView) {

            super(itemView);

            mNameOfCard = itemView.findViewById(R.id.row_cardName);
            mName =  itemView.findViewById(R.id.row_name);
            mOrg =  itemView.findViewById(R.id.row_org);
            mEmail = itemView.findViewById(R.id.row_email);
            mUrl = itemView.findViewById(R.id.row_url);
            mTel = itemView.findViewById(R.id.row_tell);
            mCell = itemView.findViewById(R.id.row_cell);
            mAddress = itemView.findViewById(R.id.row_addr);
        }

    }
}
