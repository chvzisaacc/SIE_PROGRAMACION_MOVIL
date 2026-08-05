package com.example.prueba;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProveedorAdapter extends RecyclerView.Adapter<ProveedorAdapter.ViewHolder> {

    // Interfaz doble - para seleccionar (editar) y para deshabilitar
    public interface OnItemClickListener {
        void onItemClick(Proveedor proveedor);
        void onDeshabilitar(Proveedor proveedor);
    }

    private List<Proveedor> lista;
    private OnItemClickListener listener;

    public ProveedorAdapter(List<Proveedor> lista, OnItemClickListener listener) {
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
        Proveedor p = lista.get(position);
        holder.tvTitulo.setText(p.getNombre_proveedor());
        holder.tvSubtitulo.setText("Tel: " + p.getTelefono() + " | " + p.getCorreo());

        // Clic en la fila completa para cargar los datos en los EditText
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(p);
            }
        });

        // Clic en el botón específico para borrar/deshabilitar
        holder.btnDeshabilitar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeshabilitar(p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    public void setLista(List<Proveedor> nuevaLista) {
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