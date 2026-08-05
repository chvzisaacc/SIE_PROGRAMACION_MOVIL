package com.example.prueba;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Categoria categoria);
        void onDeshabilitar(Categoria categoria);
    }

    private List<Categoria> lista;
    private OnItemClickListener listener;

    public CategoriaAdapter(List<Categoria> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categoria c = lista.get(position);
        holder.tvTitulo.setText(c.getNombre_categoria());
        holder.tvSubtitulo.setText("Descripción: " + c.getDescripcion());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(c);
        });

        holder.btnDeshabilitar.setOnClickListener(v -> {
            if (listener != null) listener.onDeshabilitar(c);
        });
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    public void setLista(List<Categoria> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvSubtitulo;
        ImageButton btnDeshabilitar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTituloRow);
            tvSubtitulo = itemView.findViewById(R.id.tvSubtituloRow);
            btnDeshabilitar = itemView.findViewById(R.id.btnDeshabilitarRow);
        }
    }
}