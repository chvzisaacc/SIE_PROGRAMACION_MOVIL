package com.example.prueba;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AlmacenAdapter extends RecyclerView.Adapter<AlmacenAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Almacen almacen);
        void onDeshabilitar(Almacen almacen);
    }

    private List<Almacen> lista;
    private OnItemClickListener listener;

    public AlmacenAdapter(List<Almacen> lista, OnItemClickListener listener) {
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
        Almacen a = lista.get(position);
        holder.tvTitulo.setText(a.getNombre_almacen());
        holder.tvSubtitulo.setText("Ubicación: " + a.getUbicacion());

        // Clic en la fila completa para cargar los datos (Editar)
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(a);
            }
        });

        // Clic en el botón para deshabilitar
        holder.btnDeshabilitar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeshabilitar(a);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    public void setLista(List<Almacen> nuevaLista) {
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