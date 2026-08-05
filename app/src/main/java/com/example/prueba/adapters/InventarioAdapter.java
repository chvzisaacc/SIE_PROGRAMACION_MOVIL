package com.example.prueba.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prueba.Inventario;
import com.example.prueba.MovimientoActivity;
import com.example.prueba.R;

import java.util.List;

public class InventarioAdapter extends RecyclerView.Adapter<InventarioAdapter.ViewHolder> {

    // Lista que contiene los registros del inventario
    private List<Inventario> listaInventario;

    public InventarioAdapter(List<Inventario> listaInventario) {
        this.listaInventario = listaInventario;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Infla el diseño de cada tarjeta del RecyclerView
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inventario, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Obtiene el inventario correspondiente a la posición actual
        Inventario inventario = listaInventario.get(position);

        // Muestra la información en la tarjeta
        holder.txtProducto.setText("Producto ID: " + inventario.getId_producto());
        holder.txtCantidad.setText("Cantidad: " + inventario.getCantidad_actual());
        holder.txtFecha.setText("Actualizado: " + inventario.getFecha_actualizacion());

        // Abre la pantalla para registrar una entrada de inventario
        holder.btnEntrada.setOnClickListener(v -> {

            Intent intent = new Intent(v.getContext(), MovimientoActivity.class);

            // Se envían los datos necesarios a la siguiente Activity
            intent.putExtra("tipo", "ENTRADA");
            intent.putExtra("idInventario", inventario.getId_inventario());
            intent.putExtra("idProducto", inventario.getId_producto());
            intent.putExtra("idAlmacen", inventario.getId_almacen());
            intent.putExtra("cantidadActual", inventario.getCantidad_actual());

            v.getContext().startActivity(intent);

        });

        // Abre la pantalla para registrar una salida de inventario
        holder.btnSalida.setOnClickListener(v -> {

            Intent intent = new Intent(v.getContext(), MovimientoActivity.class);

            // Se envían los datos necesarios a la siguiente Activity
            intent.putExtra("tipo", "SALIDA");
            intent.putExtra("idInventario", inventario.getId_inventario());
            intent.putExtra("idProducto", inventario.getId_producto());
            intent.putExtra("idAlmacen", inventario.getId_almacen());
            intent.putExtra("cantidadActual", inventario.getCantidad_actual());

            v.getContext().startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        // Devuelve la cantidad de registros del inventario
        return listaInventario.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtProducto;
        TextView txtCantidad;
        TextView txtFecha;

        Button btnEntrada;
        Button btnSalida;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Referencias a los controles del layout
            txtProducto = itemView.findViewById(R.id.txtProducto);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            txtFecha = itemView.findViewById(R.id.txtFecha);

            btnEntrada = itemView.findViewById(R.id.btnEntrada);
            btnSalida = itemView.findViewById(R.id.btnSalida);
        }
    }
}