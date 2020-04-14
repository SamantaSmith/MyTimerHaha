package com.example.mytimerhaha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SeekBar seekBar;
    private TextView textView;
    private TextView subText;
    private boolean isTimerOn;
    private Button button;
    private CountDownTimer countDownTimer;
    private String musicLag;
    private Boolean musicFlag;

    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

    private androidx.appcompat.widget.Toolbar toolbar;

    private int defaultInterval;
    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = findViewById(R.id.seekbar);
        textView = findViewById(R.id.textview);
        button = findViewById(R.id.button);
        subText = findViewById(R.id.subtext);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        seekBar.setMax(600);
        setIntervalFromSharedPreferences(PreferenceManager.getDefaultSharedPreferences(this));

        isTimerOn = false;
        musicLag = "null";

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.timer_menu);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                long progressInMillis = progress*1000;
                updateTimer(progressInMillis);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    public void Start(View view) {



        if (!isTimerOn) {

            subText.setText("I love you, keep going <3");
            button.setText("Stop");
            seekBar.setEnabled(false);
            seekBar.setVisibility(View.INVISIBLE);
            isTimerOn = true;

            countDownTimer = new CountDownTimer(seekBar.getProgress()*1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                    updateTimer(millisUntilFinished);
                }
                @Override
                public void onFinish() {

                    MusicAdapter();

                    textView.setText("Timer has finished, press Stop for new timer <3");
                    textView.setTextSize(20);
                    subText.setText("");


                }
            };

            countDownTimer.start();
        } else {

            Music("",false, false, false);


            setIntervalFromSharedPreferences(sharedPreferences);
            subText.setText("Change time with SeekBar below");
            countDownTimer.cancel();
            textView.setTextSize(40);
            button.setText("Start");
            seekBar.setEnabled(true);
            seekBar.setVisibility(View.VISIBLE);
            isTimerOn = false;

        }
    }

    private void updateTimer(long millisUntilFinished) {

        int minutes = (int)millisUntilFinished/60/1000;
        int seconds = (int)millisUntilFinished/1000 - (minutes*60);

        String minutesString = "";
        String secondsString = "";

        if (minutes <10) {

            minutesString = "0" + minutes;
        } else {

            minutesString = String.valueOf(minutes);
        }

        if (seconds <10) {

            secondsString = "0" + seconds;
        } else {

            secondsString = String.valueOf(seconds);
        }

        textView.setText(minutesString + ":" + secondsString);
    }





    public void MusicAdapter() {

        boolean internalMusicFlagSound = false;
        boolean internalMusicFlagVibrate = false;
        String musicName;

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if (sharedPreferences.getBoolean("enable_sound", true)) {
                internalMusicFlagSound = true;
            }

        if (sharedPreferences.getBoolean("enable_vibrate", true)) {
            internalMusicFlagVibrate = true;
        }

        musicName = sharedPreferences.getString("timer_melody", "fly_away");

        Music(musicName, internalMusicFlagSound, internalMusicFlagVibrate, true);
        }


    public void Music(String musicChoose, boolean musicFlag, boolean vibrateFlag, boolean isPlaying) {

        if  (musicFlag == true) {

            if (musicChoose.equals("fly_away")) {

            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.fly_away);
            mediaPlayer.start();}
            else if (musicChoose.equals("bikinibottom")) {

                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bikinibottom);
                mediaPlayer.start();
            } else {

                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.velikysup);
                mediaPlayer.start();
            }
        }

        if (vibrateFlag ==  true) {
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            long[] mill = {1000, 1000};
            vibrator.vibrate(mill, 0);
        }

        if (isPlaying == false){
            vibrator.cancel();
            mediaPlayer.reset();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.timer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent openSettings = new Intent(this, SettingsActivity.class);
            startActivity(openSettings);
            return true;
        } else  if (id == R.id.action_about) {
            Intent openAbout = new Intent(this, AboutActivity.class);
            startActivity(openAbout);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setIntervalFromSharedPreferences(SharedPreferences sharedPreferences) {

        defaultInterval = Integer.valueOf(sharedPreferences.getString("default_interval", "30"));
        long dIntervalMillis = defaultInterval*1000;
        updateTimer(dIntervalMillis);
        seekBar.setProgress(defaultInterval);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals("default_interval")) {

            setIntervalFromSharedPreferences(sharedPreferences);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}
