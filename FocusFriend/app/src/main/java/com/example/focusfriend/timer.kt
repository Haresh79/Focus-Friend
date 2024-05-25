package com.example.focusfriend

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class timer : Fragment() {
    private val CHANNEL_ID="channel id"
    private val NOTIFICATION_ID= 100

    private lateinit var timerTv:TextView
    private lateinit var startBtn:ImageView
    private lateinit var resetBtn:Button

    private var timer: CountDownTimer?=null
    private var hour: Int? =null
    private var minute: Int? =null
    private var second: Int? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        createNotificationChannel()
        val view=inflater.inflate(R.layout.fragment_timer, container, false)
        val hourIn=view.findViewById<EditText>(R.id.hour)
        val minIn=view.findViewById<EditText>(R.id.minute)
        val secIn=view.findViewById<EditText>(R.id.second)
        timerTv=view.findViewById(R.id.timerTxt)
        startBtn=view.findViewById(R.id.ppBtn)
        resetBtn=view.findViewById(R.id.button)

        startBtn.setOnClickListener {
            if (secIn.text.isNotEmpty()){
                second=secIn.text.toString().toInt()
            }else{
                second=0
            }
            if (hourIn.text.isNotEmpty()){
                hour=hourIn.text.toString().toInt()
            }else{
                hour=0
            }
            if (minIn.text.isNotEmpty()){
                minute=minIn.text.toString().toInt()
            }else{
                minute=0
            }


                startTimer(second!!, minute!!, hour!!)
                hourIn.text.clear()
                minIn.text.clear()
                secIn.text.clear()
                sendNotification("Timer is started now.")
        }

        resetBtn.setOnClickListener {
            closeTimer()
        }

        return view
    }

    private fun closeTimer() {
        timer?.cancel()
        timerTv.text="00:00:00"
    }

    private fun startTimer(second: Int=0, minute: Int=0, hour: Int=0) {

        var mills= htoms(hour)+mtoms(minute)+stoms(second)

        timer=object :CountDownTimer(mills.toLong(), 1000){
            override fun onTick(millisUntilFinished: Long) {
                timerTv.text=msToTimeFormat(millisUntilFinished)
            }

            override fun onFinish() {
                timerTv.text="00:00:00"
                var mediaPlayer=MediaPlayer.create(context,R.raw.endmusic)
                mediaPlayer.start()
                sendNotification("Your Time Is Out Now.")
            }

        }.start()
    }

    fun mtoms(x:Int):Int{
        return x*60*1000
    }
    fun stoms(x:Int):Int{
        return x*1000
    }
    fun htoms(x:Int):Int{
        return x*60*60*1000
    }

    fun msToTimeFormat(x:Long):String{
        val h=x/3600000
        val remainMinutes=x%3600000
        val m=remainMinutes/60000
        val s=(remainMinutes%60000)/1000
        return "%02d:%02d:%02d".format(h,m,s)
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val name="Notification Title"
            val descriptionText="Notification Description"
            val importance= NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID,name,importance).apply {
                description=descriptionText
            }
            val notificationManager: NotificationManager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    @SuppressLint("MissingPermission")
    private fun sendNotification(str:String) {
        val builder= NotificationCompat.Builder(requireContext(),CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_hourglass_bottom_24)
            .setContentTitle("Timer")
            .setContentText(str)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        val notificationManager= NotificationManagerCompat.from(requireContext())
        notificationManager.notify(NOTIFICATION_ID,builder)
    }

}