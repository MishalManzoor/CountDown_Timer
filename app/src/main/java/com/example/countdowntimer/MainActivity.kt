package com.example.countdowntimer

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.countdowntimer.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var hours = 0
    private var minutes = 0
    private var seconds = 0
    private var countdown: CountDownTimer? = null
    private var isCount = false
    lateinit var alarmManager: AlarmManager
    lateinit var pendingIntent: PendingIntent

    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()

        // for hours
        binding.plusBtn.setOnClickListener {

            hours = increaseTime(hours)
            updateUi()
        }

        binding.minBtn.setOnClickListener {
            hours = decreaseTime(hours)
            updateUi()
        }

        // for minutes
        binding.plusBtn2.setOnClickListener {
            minutes = increaseTime(minutes)
            updateUi()
        }

        binding.minBtn2.setOnClickListener {
            minutes = decreaseTime(minutes)
            updateUi()
        }

        // for seconds
        binding.plusBtn3.setOnClickListener {

            seconds = increaseTime(seconds)
            updateUi()
        }

        binding.secBtn3.setOnClickListener {
            seconds = decreaseTime(seconds)
            updateUi()
        }

        // buttons
        binding.startButton.setOnClickListener {

            if (hours == 0 && minutes == 0 && seconds == 0) {
                return@setOnClickListener
            }
            if (isCount) {
                stopCount() // pause
            } else {
                startCount()
            }
        }

        binding.cancelButton.setOnClickListener {

            resetCount()
        }
    }

    private fun increaseTime(time: Int): Int {
        // create a temporary variable
        var temp = time

        // increase it by 1
        temp += 1

        // maximum is 99
        if (temp >= 99) {
            temp = 99
        }
        // return the temporary time
        return temp
    }

    private fun decreaseTime(time: Int): Int {
        // create a temporary variable
        var temp = time

        // decrease it by 1
        temp -= 1

        // minimum is 0
        if (temp <= 0) {
            temp = 0
        }
        // return the temporary time
        return temp
    }

    // update the UI
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun updateUi() {

        // create instance of calender
        val c = Calendar.getInstance()

        c.clear()

        // set the hours, minutes , seconds
        c.set(Calendar.HOUR_OF_DAY, hours)
        c.set(Calendar.MINUTE, minutes)
        c.set(Calendar.SECOND, seconds)

        // format the hours, min , seconds
        val dateFormat = SimpleDateFormat("HH:mm:ss").format(c.time)
        val dates = dateFormat.split(":")

        // set them in textViews
        binding.hoursTxt.text = dates[0]
        binding.minTxt.text = dates[1]
        binding.secTxt.text = dates[2]

        // countdown is running
        if (isCount) {
            binding.startButton.text = "Pause"
        }
        // if count pause or reset
        else {
            binding.startButton.text = "Start"
        }
    }

    private fun updateTimerWithSeconds(s: Int) {
        // create instance of calender
        val c = Calendar.getInstance()

        c.clear()

        // set the current seconds
        c.set(Calendar.SECOND, s)

        // get the current hours,minute, seconds
        hours = c.get(Calendar.HOUR_OF_DAY)
        minutes = c.get(Calendar.MINUTE)
        seconds = c.get(Calendar.SECOND)
    }

    private fun startCount() {
        // if countdown is 0
        if (countdown != null) {
            return
        }

        // get the total seconds
        val totalSecond = seconds + (60 * minutes) + (3600 * hours)

        // set the countdownTimer
        countdown = object : CountDownTimer(
            (totalSecond.toLong() * 1000), // to millisecond
            1000
        ) {
            override fun onTick(p0: Long) {

                updateTimerWithSeconds(
                    // millisecond to second
                    // onTick will be toggle immediately, so we need + 1
                    // to make it looks like real watch
                    (p0 * 0.001).toInt()
                )
                // to update UI
                updateUi()
            }

            @SuppressLint("UnspecifiedImmutableFlag")
            override fun onFinish() {

                // on finish set the notification
                val c = Calendar.getInstance()

                c.clear()
                // hours , minute ,
                // seconds are already finished
                c.set(Calendar.HOUR_OF_DAY, 0)
                c.set(Calendar.MINUTE, 0)
                c.set(Calendar.SECOND, 0)
                c.set(Calendar.MILLISECOND, 0)

                // set the alarm Manager
                alarmManager =
                    getSystemService(Context.ALARM_SERVICE)
                            as AlarmManager

                // set intent from
                // this activity to BroadCastReceiver class
                val intent =
                    Intent(
                        this@MainActivity, ReceiverClass::class.java
                    )

                // set pending intent
                pendingIntent = PendingIntent
                    .getBroadcast(
                        this@MainActivity,
                        0, intent, 0
                    )

                // set alarm
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    c.timeInMillis,
                    pendingIntent
                )

                // to reset
                resetCount()
            }
        }

        // if counter timer start again
        countdown?.start()
        //   set the isCount true
        isCount = true
    }

    private fun stopCount() {

        // pause the countdown
        countdown?.cancel()
        countdown = null
        isCount = false

        // update the UI
        updateUi()
    }

    private fun resetCount() {

        stopCount()

        hours = 0
        minutes = 0
        seconds = 0

        updateUi()
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.O
        ) {
            val name = "Notification channel"
            val description = "Channel for me"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("CONST_CHANNEL", name, importance)
            channel.description = description

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}