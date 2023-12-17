package com.android.myapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.text.NumberFormat;
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

        holder.itemView.setOnClickListener(view -> holder.showBottomDialog());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, hutang, date;
        SQLite sqLite;
        DataContainer index;
        int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            hutang = itemView.findViewById(R.id.hutang);
            date = itemView.findViewById(R.id.tgl);
            sqLite = new SQLite(itemView.getContext());
        }

        void bindData(DataContainer index, int position) {
            this.index = index;
            this.position = position;

            NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
            var formattedInt = numberFormat.format(index.getHutang());
            name.setText(index.getName());
            hutang.setText(formattedInt);
            date.setText(index.getTanggal());
        }

        void editButtonAction() {
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

        void showBottomDialog() {
            var view = LayoutInflater.from(itemView.getContext()).inflate(R.layout.bottom_sheet_layout, null);
            var editBt = view.findViewById(R.id.editBt);
            var deleteBt = view.findViewById(R.id.deleteBt);
            var dialog = new BottomSheetDialog(itemView.getContext());
            dialog.setContentView(view);

            editBt.setOnClickListener(view12 -> {
                editButtonAction();
                dialog.dismiss();
            });

            deleteBt.setOnClickListener(view1 -> {
                materialAlertDialog();
                dialog.dismiss();
            });

            dialog.show();
        }

        void materialAlertDialog() {
            var builder = new MaterialAlertDialogBuilder(itemView.getContext());
            builder.setTitle("Konfirmasi")
                    .setMessage("Yakin ingin menghapus ?")
                    .setNegativeButton("Batal", (dialogInterface, i) -> dialogInterface.dismiss())
                    .setPositiveButton("Ok", (dialogInterface, i) -> {
                        sqLite.delete(index.getId());
                        removeItem();
                        showToast();
                    })
                    .create()
                    .show();
        }

        void removeItem() {
            data.remove(position);
            notifyItemRemoved(position);
        }

        private void showToast() {
            Toast.makeText(itemView.getContext(), index.getName() + " Dihapus", Toast.LENGTH_SHORT).show();
        }
    }
}
