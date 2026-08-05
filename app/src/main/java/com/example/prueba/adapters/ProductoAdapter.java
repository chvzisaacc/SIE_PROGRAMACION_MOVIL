package com.example.prueba.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prueba.EditarProductoActivity;
import com.example.prueba.Producto;
import com.example.prueba.R;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {

    // Contexto de la Activity y lista de productos obtenidos desde Supabase
    private Context context;
    private List<Producto> listaProductos;

    public ProductoAdapter(Context context, List<Producto> listaProductos) {
        this.context = context;
        this.listaProductos = listaProductos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Infla el diseño de cada tarjeta del RecyclerView
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto, parent, false);

        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Obtiene el producto correspondiente a la posición actual
        Producto producto = listaProductos.get(position);

        // Muestra la información del producto
        holder.txtNombre.setText(producto.getNombre_producto());
        holder.txtDescripcion.setText(producto.getDescripcion());
        holder.txtPrecio.setText("L. " + producto.getPrecio_unitario());
        holder.txtStock.setText(String.valueOf(producto.getStock_minimo()));

        // Abre la pantalla para editar el producto
        holder.btnEditar.setOnClickListener(v -> {

            Intent intent = new Intent(context, EditarProductoActivity.class);

            // Envía toda la información del producto a la Activity de edición
            intent.putExtra("id_producto", producto.getId_producto());
            intent.putExtra("nombre", producto.getNombre_producto());
            intent.putExtra("descripcion", producto.getDescripcion());
            intent.putExtra("precio", String.valueOf(producto.getPrecio_unitario()));
            intent.putExtra("unidad", producto.getUnidad_medida());
            intent.putExtra("stock", String.valueOf(producto.getStock_minimo()));
            intent.putExtra("categoria", String.valueOf(producto.getId_categoria()));
            intent.putExtra("proveedor", String.valueOf(producto.getId_proveedor()));

            context.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {

        // Devuelve la cantidad de productos que mostrará el RecyclerView
        return listaProductos.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombre;
        TextView txtDescripcion;
        TextView txtPrecio;
        TextView txtStock;

        Button btnEditar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Referencias a los controles de la tarjeta del producto
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
            txtStock = itemView.findViewById(R.id.txtStock);

            btnEditar = itemView.findViewById(R.id.btnEditar);
        }
    }
}