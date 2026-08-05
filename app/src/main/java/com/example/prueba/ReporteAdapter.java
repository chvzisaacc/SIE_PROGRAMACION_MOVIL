package com.example.prueba;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ReporteAdapter extends RecyclerView.Adapter<ReporteAdapter.ViewHolder> {

    private List<String> filas = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String data = filas.get(position);
        String[] partes = data.split("\\|");
        holder.tv1.setText(partes[0].trim());
        if (partes.length > 1) {
            holder.tv2.setText(partes[1].trim());
        } else {
            holder.tv2.setText("");
        }
        holder.tv1.setTextColor(android.graphics.Color.BLACK);
        holder.tv2.setTextColor(android.graphics.Color.DKGRAY);
    }

    @Override
    public int getItemCount() {
        return filas.size();
    }

    public void setDatos(List<String> nuevasFilas) {
        this.filas = nuevasFilas;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv1, tv2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(android.R.id.text1);
            tv2 = itemView.findViewById(android.R.id.text2);
        }
    }
}