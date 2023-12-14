package com.android.myapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    List<DataContainer> data;

    public RecyclerViewAdapter(List<DataContainer> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        
        var index = data.get(position);
        holder.bindData(index, position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.showBottomDialog();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id, name, phone, hutang, date, tipe, keterangan;
        SQLite sqLite;
        DataContainer index;
        int position;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.id);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            hutang = itemView.findViewById(R.id.hutang);
            tipe = itemView.findViewById(R.id.tipe);
            date = itemView.findViewById(R.id.tgl);
            keterangan = itemView.findViewById(R.id.keterangan);
            sqLite = new SQLite(itemView.getContext());
        }

        void bindData(DataContainer index, int position) {
            this.index = index;
            this.position = position;
            id.setText(index.getId());
            name.setText(index.getName());
            phone.setText(index.getPhone());
            hutang.setText("Rp " + index.getHutang());
            date.setText(index.getTanggal());
            tipe.setText(index.getTipe());
            keterangan.setText(index.getKeterangan());
        }

        void setEditBt(){
            Intent intent = new Intent(itemView.getContext(), UpdateActivity.class);
            intent.putExtra("id", index.getId());
            intent.putExtra("name", index.getName());
            intent.putExtra("phone", index.getPhone());
            intent.putExtra("hutang", index.getHutang());
            intent.putExtra("date", index.getTanggal());
            intent.putExtra("tipe", index.getTipe());
            intent.putExtra("keterangan", index.getKeterangan());
            itemView.getContext().startActivity(intent);
        }

        void showBottomDialog(){
            var view = LayoutInflater.from(itemView.getContext()).inflate(R.layout.bottom_sheet_layout, null);
            MaterialCardView editBt = view.findViewById(R.id.editBt);
            MaterialCardView deleteBt = view.findViewById(R.id.deleteBt);
            var dialog = new BottomSheetDialog(itemView.getContext());
            dialog.setContentView(view);

            editBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setEditBt();
                    dialog.dismiss();
                }
            });

            deleteBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    materialAlertDialog();
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

        void materialAlertDialog(){
            var builder = new MaterialAlertDialogBuilder(itemView.getContext());
            builder.setTitle("Konfirmasi")
                    .setMessage("Yakin ingin menghapus ?")
                    .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            sqLite.delete(index.getId());
                            removeItem();
                        }
                    }).create().show();
        }

        void removeItem(){
            data.remove(position);
            notifyItemRemoved(position);
        }
    }
}
