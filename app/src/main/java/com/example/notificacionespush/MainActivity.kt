package com.example.notificacionespush

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.notificacionespush.ui.theme.NotificacionesPushTheme
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotificacionesPushTheme {
                // Añadir el pop-up de solicitar permisos
                SolicitarPermisos()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
        // Para probar si funciona
        obtenerToken()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
// Solicitar permisos de notificacion (pop-up).
// Necesario para cuando entras en la app (al menos la primera vez).
fun SolicitarPermisos() {
    // A partir de la versión 33 hay que
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val lanzador = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) {
            habilitado ->
            if(habilitado)
                println("Periso concedido")
            else
                println("Se han denegado los permisos")
            // LauncherEffect -> Hilos para composables
        }
        LaunchedEffect("") {
            lanzador.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

// Hay que obtener un token único por aplicación
private fun obtenerToken() {
    FirebaseMessaging.getInstance().token.addOnCompleteListener {
        if (it.isSuccessful) { // me han dado un token válido
            println("Mi token: ${it.result}")
        } else
            println("Error al recibir el token")
    }
}