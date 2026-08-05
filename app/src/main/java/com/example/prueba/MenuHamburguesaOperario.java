package com.example.prueba;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MenuHamburguesaOperario extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView contenido;
    WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_operarios);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        contenido = findViewById(R.id.txtContenido);

        // Instanciamos el WebView
        webView = findViewById(R.id.webViewVideo);

        if (webView != null) {
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);

            // Permite la reproducción fluida
            webSettings.setMediaPlaybackRequiresUserGesture(false);

            webView.setWebChromeClient(new WebChromeClient());

            // Cadena HTML del video en iframe
            String htmlVideo = "<!DOCTYPE html><html><head>" +
                    "<style>body{margin:0;padding:0;background-color:#000;}</style>" +
                    "</head><body>" +
                    "<iframe width=\"100%\" height=\"100%\" " +
                    "src=\"https://www.youtube.com/embed/iM7gUJ75_8w?enablejsapi=1\" " +
                    "title=\"YouTube video player\" frameborder=\"0\" " +
                    "allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" " +
                    "allowfullscreen></iframe>" +
                    "</body></html>";

            webView.loadDataWithBaseURL("https://www.youtube.com", htmlVideo, "text/html", "utf-8", null);
        }

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toolbar.setNavigationOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_productos) {
                Intent intent = new Intent(MenuHamburguesaOperario.this, ProductosActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_inventario) {
                Intent intent = new Intent(MenuHamburguesaOperario.this, InventarioActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_categorias) {
                contenido.setText("Categorías");
            } else if (id == R.id.nav_reportes) {
                contenido.setText("Reportes");
            } else if (id == R.id.nav_cerrar_sesion) {
                ManejarSesion.cerrarSesion(MenuHamburguesaOperario.this);

                Intent intent = new Intent(MenuHamburguesaOperario.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            drawerLayout.closeDrawers();
            return true;
        });
    }

    // Libera la memoria del WebView
    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}