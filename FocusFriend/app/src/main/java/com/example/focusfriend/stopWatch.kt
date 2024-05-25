package com.example.focusfriend

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class  stopWatch : Fragment() {
    private lateinit var watch:TextView
    private lateinit var playPause:ImageView
    private lateinit var reset:ImageView
    private lateinit var lapBtn:Button
    private lateinit var lapHis:TextView

    private var lapArrayList= mutableListOf<String>()
    private var currentTime:Long=0L
    private var startTime:Long=0L
    private var pauseTime:Long=0L
    private var isRunning:Boolean=false

    private val handler=Handler(Looper.getMainLooper())
    private val runnable=object : Runnable{
        override fun run() {
            if (isRunning){
                currentTime=(System.currentTimeMillis()-startTime)+pauseTime
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
        val formattedTime = String.format("%02d:%02d:%02d.%03d", h, m, s, currentTime % 1000)
        watch.text=formattedTime
    }
   private fun lapHisUpdate(){
       val elapsedTime=currentTime/1000
       val s=elapsedTime%60
       val m=(elapsedTime/60)%60
       val h=elapsedTime/3600
       val formattedTime=String.format("%02d:%02d:%02d.%03d",h,m,s,currentTime%1000)
       lapArrayList.add("->"+formattedTime+" \n")
       lapHis.text= lapArrayList.toString()
   }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_stop_watch, container, false)
        watch=view.findViewById(R.id.watch)
        playPause=view.findViewById(R.id.playpause)
        reset=view.findViewById(R.id.reset)
        lapBtn=view.findViewById(R.id.lapBtn)
        lapHis=view.findViewById(R.id.lapHistory)

        var play=true
        playPause.setOnClickListener {
            if (play){
                play=false
                playPause.setImageResource(R.drawable.baseline_pause_24)
                if (!isRunning){
                    isRunning=true
                    startTime=System.currentTimeMillis()
                    handler.post(runnable)
                    updateUi()
                }
            }else{
                play=true
                playPause.setImageResource(R.drawable.baseline_play_arrow_24)
                isRunning=false
                pauseTime=currentTime
                handler.removeCallbacks(runnable)

            }
            lapBtn.setOnClickListener {
                lapHisUpdate()
                var mediaPlayer=MediaPlayer.create(context,R.raw.shortmusic)
                mediaPlayer.start()
            }
        }
        reset.setOnClickListener {
            isRunning=false
            currentTime=0L
            startTime=0L
            pauseTime=0L
            updateUi()
            handler.removeCallbacks(runnable)
            playPause.setImageResource(R.drawable.baseline_play_arrow_24)
            play=true
        }

        return view
    }

}