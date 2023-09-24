package com.android.myapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    List<DataContainer> data;
    Context context;

    public Adapter(List<DataContainer> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fill_recycler,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.id.setText(data.get(position).getId());
        holder.name.setText(data.get(position).getName());
        holder.phone.setText(data.get(position).getPhone());
        holder.hutang.setText("Rp " + data.get(position).getHutang());
        holder.date.setText(data.get(position).getTanggal());
        holder.keterangan.setText(data.get(position).getKeterangan());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", data.get(position).getId());
                intent.putExtra("name", data.get(position).getName());
                intent.putExtra("phone", data.get(position).getPhone());
                intent.putExtra("hutang", data.get(position).getHutang());
                intent.putExtra("date", data.get(position).getTanggal());
                intent.putExtra("keterangan", data.get(position).getKeterangan());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id, name, phone, hutang, date, keterangan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            hutang = itemView.findViewById(R.id.hutang);
            date = itemView.findViewById(R.id.tgl);
            keterangan = itemView.findViewById(R.id.keterangan);
        }
    }
}
