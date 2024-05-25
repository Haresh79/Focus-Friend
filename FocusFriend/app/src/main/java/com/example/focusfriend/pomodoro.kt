package com.example.focusfriend

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService

class pomodoro : Fragment() {
    private val CHANNEL_ID="channel id"
    private val NOTIFICATION_ID= 101

    private lateinit var display:TextView
    private lateinit var activeBtn:Button
    private lateinit var stopBtn:Button
    private lateinit var mode:ImageView

    private var startTime:Long=0L
    private var currentTime:Long=0L
    private var isRun:Boolean=false

    private val handler=Handler(Looper.getMainLooper())
    private val runnable= object :Runnable{
        override fun run() {
            if (isRun){
                if (currentTime==0L || currentTime<=1800001){
                    mode.setImageResource(R.drawable.learning)
                    currentTime=System.currentTimeMillis()-startTime
                    if (currentTime>=1500000){
                        mode.setImageResource(R.drawable.coffeebreak)
                    }
                }
                updateUi()
            }
            handler.postDelayed(this,1)
        }

    }

    private fun updateUi() {
        val elapsedTime = currentTime/1000
        val s= elapsedTime%60
        val m= (elapsedTime/60)%60
        val h= elapsedTime/(60*60)
        val formattedTime= String.format("%02d:%02d:%02d", h,m,s)
        display.text=formattedTime
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        createNotificationChannel()
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_pomodoro, container, false)
        display=view.findViewById(R.id.display)
        activeBtn=view.findViewById(R.id.button3)
        stopBtn=view.findViewById(R.id.stopBtn)
        mode=view.findViewById(R.id.imageView)

        activeBtn.setOnClickListener {
            if (!isRun){
                isRun=true
                startTime=System.currentTimeMillis()
                handler.post(runnable)
                updateUi()
                sendNotification("Focus Mode On. Complete your job in 25 minutes.")
                if (currentTime==1499999L){
                    sendNotification("Rest Mode On. Take a 5 minutes Break.")
                    var mediaPlayer=MediaPlayer.create(context,R.raw.shortmusic)
                    mediaPlayer.start()
                }
                if (currentTime==1800000L){
                    sendNotification("Pomodoro Mode Is Over Now...")
                    var mediaPlayer=MediaPlayer.create(context, R.raw.endmusic)
                    mediaPlayer.start()
                }
            }
        }
        stopBtn.setOnClickListener {
            isRun=false
            currentTime=0L
            startTime=0L
            mode.setImageResource(R.drawable.basicimage)
            updateUi()
            handler.removeCallbacks(runnable)
        }


        return view
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val name="Notification Title"
            val descriptionText="Notification Description"
            val importance=NotificationManager.IMPORTANCE_HIGH
            val channel =NotificationChannel(CHANNEL_ID,name,importance).apply {
                description=descriptionText
            }
            val notificationManager:NotificationManager= activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    @SuppressLint("MissingPermission")
    private fun sendNotification(str:String) {
        val builder=NotificationCompat.Builder(requireContext(),CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_rocket_24)
            .setContentTitle("Pomodoro")
            .setContentText(str)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        val notificationManager= NotificationManagerCompat.from(requireContext())
        notificationManager.notify(NOTIFICATION_ID,builder)
    }

}