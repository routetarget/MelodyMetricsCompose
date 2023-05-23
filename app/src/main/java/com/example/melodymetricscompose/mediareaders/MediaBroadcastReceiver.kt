package com.example.melodymetricscompose.mediareaders

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import org.greenrobot.eventbus.EventBus

class MediaBroadcastReceiver : BroadcastReceiver() {

    private var lastData: String? = null
    private var dataCounter: Int = 0

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("Receiver","Broadcast received")

        if(intent.action == "com.spotify.music.metadatachanged") {
            Log.d("Receiver","intent action received")
            val artist = intent.getStringExtra("artist").toString()
            val title = intent.getStringExtra("track").toString()
            val album = intent.getStringExtra("album").toString()
            val id = intent.getStringExtra("id")?.filter { it.isDigit() }?.toInt() ?: 0
            val app: String = "spotify"



            EventBus.getDefault().post(TrackChangedEvent(id,artist,title,album,app))

        } else if(intent.action == "com.android.music.metachanged") {
            val title = intent.getStringExtra("track").toString()
            val album = intent.getStringExtra("album").toString()
            val artist = intent.getStringExtra("artist").toString()
            lastData = album
            val id = intent.getStringExtra("id")?.filter { it.isDigit() }?.toInt() ?: 0
            var app: String = "androidDefault"

            Log.d("Receiver vlc","Broadcast received, track $title")

            //lastData = album
            if (lastData == album){
                dataCounter++
                Log.d("Receiver vlc","DataCounter = $dataCounter")
            } else {
                dataCounter = 1
            }

            if (dataCounter >= 2){
                Log.d("Receiver vlc","message sent")
                EventBus.getDefault().post(TrackChangedEvent(id,artist,title,album,app))
                dataCounter = 0
            }

        }
    }
}

class TrackChangedEvent(val id: Int, val artist: String, val title: String, val album: String, val app: String)
