    package com.example.prueba;

    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.TextView;
    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import java.util.List;

    /**
     * Adaptador para visualizar la lista de usuarios en un RecyclerView.
     * Permite gestionar el estado de activación de cada usuario individualmente.
     */
    public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

        private List<Usuario> listaUsuarios;
        private OnEstadoChangeListener listener;

        // Interfaz para notificar cambios de estado (activar/desactivar) a la Activity
        public interface OnEstadoChangeListener {
            void onEstadoChanged(Usuario usuario, boolean nuevoEstado);
        }

        public UsuarioAdapter(List<Usuario> listaUsuarios, OnEstadoChangeListener listener) {
            this.listaUsuarios = listaUsuarios;
            this.listener = listener;
        }

        @NonNull
        @Override
        public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Infla el diseño personalizado para cada fila de la lista
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario, parent, false);
            return new UsuarioViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
            Usuario usuario = listaUsuarios.get(position);

            // Mapeo de datos del objeto Usuario a los TextViews del item
            holder.txtNombre.setText(usuario.getNombre() + " " + usuario.getApellido());
            holder.txtCorreo.setText(usuario.getCorreo());
            
            // Traducción visual del ID de rol a texto amigable
            holder.txtRol.setText(usuario.getId_rol() == 1 ? "Rol: Administrador" : "Rol: Operario");

            // Configuración del botón según el estado actual
            if (usuario.getEstado()) {
                holder.btnEstado.setText("Desactivar");
                holder.btnEstado.setBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
            } else {
                holder.btnEstado.setText("Activar");
                holder.btnEstado.setBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
            }

            // Al hacer clic, enviamos la acción inversa al estado actual mediante el listener
            holder.btnEstado.setOnClickListener(v -> listener.onEstadoChanged(usuario, !usuario.getEstado()));
        }

        @Override
        public int getItemCount() {
            return listaUsuarios.size();
        }

        public static class UsuarioViewHolder extends RecyclerView.ViewHolder {
            TextView txtNombre, txtCorreo, txtRol;
            Button btnEstado;

            public UsuarioViewHolder(@NonNull View itemView) {
                super(itemView);
                txtNombre = itemView.findViewById(R.id.txtNombreCompleto);
                txtCorreo = itemView.findViewById(R.id.txtCorreoItem);
                txtRol = itemView.findViewById(R.id.txtRol);
                btnEstado = itemView.findViewById(R.id.btnCambiarEstado);
            }
        }
    }
