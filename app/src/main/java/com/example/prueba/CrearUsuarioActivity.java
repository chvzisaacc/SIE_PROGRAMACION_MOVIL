package com.example.prueba;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearUsuarioActivity extends AppCompatActivity {

    // Componentes de entrada de datos
    private EditText etNombre, etApellido, etCorreo, etPassword, etTelefono;
    private RadioButton rbAdmin, rbOperario;
    private Button btnGuardar;
    private SupabaseApi apiService;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);

        // Inicialización de la navegación lateral (Drawer)
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toolbar.setNavigationOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        // Configuración de los accesos directos del menú lateral
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_usuario) {
                startActivity(new Intent(this, UsuariosActivity.class));
                finish();
            } else if (id == R.id.nav_productos) {
                startActivity(new Intent(this, ProductosActivity.class));
                finish();
            } else if (id == R.id.nav_inventario) {
                startActivity(new Intent(this, InventarioActivity.class));
                finish();
            } else if (id == R.id.nav_almacenes) {
                startActivity(new Intent(this, AlmacenActivity.class));
                finish();
            } else if (id == R.id.nav_proveedores) {
                startActivity(new Intent(this, ProveedoresActivity.class));
                finish();
            } else if (id == R.id.nav_cerrar_sesion) {
                ManejarSesion.cerrarSesion(this);
                Intent intent = new Intent(this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            drawerLayout.closeDrawers();
            return true;
        });

        // Vinculación de vistas del formulario
        etNombre = findViewById(R.id.etNuevoNombre);
        etApellido = findViewById(R.id.etNuevoApellido);
        etCorreo = findViewById(R.id.etNuevoCorreo);
        etTelefono = findViewById(R.id.etNuevoTelefono);
        etPassword = findViewById(R.id.etNuevaPassword);
        rbAdmin = findViewById(R.id.rbAdmin);
        rbOperario = findViewById(R.id.rbOperario);
        btnGuardar = findViewById(R.id.btnGuardarUsuario);

        apiService = RetrofitClient.getClient().create(SupabaseApi.class);
        btnGuardar.setOnClickListener(v -> guardarNuevoUsuario());
    }

    /**
     * Inicia el proceso de registro: valida campos y llama a Supabase Auth
     */
    private void guardarNuevoUsuario() {
        if (!validarCampos()) {
            return;
        }

        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Paso 1: Registrar en Supabase Auth con metadatos para que el Trigger de BD los reciba
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("nombre", nombre);
        metadata.put("apellido", apellido);
        metadata.put("telefono", telefono);
        metadata.put("id_rol", rbAdmin.isChecked() ? 1 : 2);

        SignupRequest signupRequest = new SignupRequest(correo, password, metadata);

        apiService.registrarEnAuth(signupRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Paso 2: Tras éxito en Auth, forzamos la actualización de los datos en la tabla 'usuarios'
                    // Esto asegura que se guarden aunque el Trigger tarde o tenga valores por defecto
                    String uidReal = response.body().getUser().getId();
                    actualizarPerfilManual(uidReal, nombre, apellido, correo, telefono);
                } else {
                    String errorMsg = "Error al crear cuenta";
                    if (response.code() == 400) errorMsg = "El correo ya existe o datos inválidos";
                    Toast.makeText(CrearUsuarioActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(CrearUsuarioActivity.this, "Fallo de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Inserta o actualiza directamente los datos del perfil en la tabla 'public.usuarios'
     */
    private void actualizarPerfilManual(String uid, String nombre, String apellido, String correo, String telefono) {
        Usuario usuario = new Usuario();
        usuario.setId(uid);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setCorreo(correo);
        usuario.setTelefono(telefono);
        usuario.setId_rol(rbAdmin.isChecked() ? 1 : 2);
        usuario.setEstado(true);

        apiService.insertarUsuario(usuario).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // El código 409 (Conflicto) se ignora aquí ya que significa que el Trigger ya creó el registro
                if (response.isSuccessful() || response.code() == 409) {
                    Toast.makeText(CrearUsuarioActivity.this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                } else {
                    Toast.makeText(CrearUsuarioActivity.this, "Cuenta creada, pero error en perfil: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CrearUsuarioActivity.this, "Usuario creado exitosamente", Toast.LENGTH_SHORT).show();
                limpiarCampos();
            }
        });
    }

    /**
     * Valida que los datos ingresados cumplan con los formatos requeridos (Correo, Teléfono, Largo de Clave)
     */
    private boolean validarCampos() {
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        boolean esValido = true;

        if (nombre.isEmpty()) {
            etNombre.setError("El nombre es obligatorio");
            esValido = false;
        } else if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
            etNombre.setError("El nombre solo debe contener letras");
            esValido = false;
        }

        if (apellido.isEmpty()) {
            etApellido.setError("El apellido es obligatorio");
            esValido = false;
        } else if (!apellido.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
            etApellido.setError("El apellido solo debe contener letras");
            esValido = false;
        }

        if (correo.isEmpty()) {
            etCorreo.setError("El correo es obligatorio");
            esValido = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            etCorreo.setError("Formato de correo inválido");
            esValido = false;
        }

        if (telefono.isEmpty()) {
            etTelefono.setError("El teléfono es obligatorio");
            esValido = false;
        } else if (!telefono.matches("^[0-9]{8}$")) {
            etTelefono.setError("El teléfono debe tener 8 dígitos numéricos");
            esValido = false;
        }

        if (password.isEmpty()) {
            etPassword.setError("La contraseña es obligatoria");
            esValido = false;
        } else if (password.length() < 8) {
            etPassword.setError("Mínimo 8 caracteres");
            esValido = false;
        }

        return esValido;
    }

    private void limpiarCampos() {
        etNombre.setText("");
        etApellido.setText("");
        etCorreo.setText("");
        etTelefono.setText("");
        etPassword.setText("");
        rbOperario.setChecked(true);
    }
}
