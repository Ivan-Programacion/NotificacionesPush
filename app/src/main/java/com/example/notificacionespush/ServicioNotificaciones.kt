package com.example.notificacionespush

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class ServicioNotificaciones : FirebaseMessagingService() {

    // Se ejecuta cuando recibimos un mensaje REMOTO
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        // .data para acceder a los datos
        // Necesita titulo y cuerpo la notificacion
        val titulo = message.data["title"]
        val cuerpo = message.data["body"]
        // Si no es nulo el titulo y body recogido de Firebase, ejecutamos la función
        if (titulo != null && cuerpo != null) {
            mostrarNotificacion(titulo, cuerpo)
        }
    }

    private fun mostrarNotificacion(titulo: String, cuerpo: String) {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val idCanal = "MiCanal"

        // Intento para que al pulsar la notificación, abra la app (MainActivity)
        val intent = Intent(this, MainActivity::class.java)
        // Hay que hacer un pendingIntent que guarde un contenedor con el intento
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Para construir la notificación
        val notificacion = NotificationCompat.Builder(this, idCanal)
            .setContentTitle(titulo)
            .setContentText(cuerpo)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            // setContentIntent --> si le das a la notificación, te abre la app
            .setContentIntent(pendingIntent)
            .build()
        // A partir de la versión 27, necesitamos crear un canal de notificación
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Se crea un canal por donde va a ir tu notificación.
            // Cada notificación del móvil va por un canal.
            // Importancia se pone por defecto, puesto que solo tenemos un servicio por este canal
            val miCanal = NotificationChannel(
                idCanal,
                "FirebaseNotificacion",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            // Se crea el canal
            manager.createNotificationChannel(miCanal)
        }
        // Se manda/lanza la notificación
        manager.notify(0, notificacion)
    }

}