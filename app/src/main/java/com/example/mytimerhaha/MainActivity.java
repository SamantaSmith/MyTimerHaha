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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = findViewById(R.id.seekbar);
        textView = findViewById(R.id.textview);
        button = findViewById(R.id.button);
        subText = findViewById(R.id.subtext);


        seekBar.setMax(600);
        seekBar.setProgress(30);

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

                    SharedPreferences sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                     if (sharedPreferences.getBoolean("enable_sound", true)
                             && sharedPreferences.getBoolean("enable_vibrate", true)) {

                                Music("vibrate_and_sound", true);
                     } /*else if (sharedPreferences.getBoolean("enable_sound", true)
                             && sharedPreferences.getBoolean("enable_vibrate", false)) {

                         Music("sound_without_vibrate", true);
                     } else if (sharedPreferences.getBoolean("enable_sound", false)
                             && sharedPreferences.getBoolean("enable_vibrate", true)) {

                         Music("vibrate_without_sound", true);
                     } */else  {

                         Music("no_vibrate_no_sound", true);
                     }

                    textView.setText("Timer has finished, press Stop for new timer <3");
                    textView.setTextSize(20);
                    subText.setText("");
                }
            };

            countDownTimer.start();
        } else {

            Music("", false);
            subText.setText("Change time with SeekBar below");
            countDownTimer.cancel();
            textView.setText("00:30");
            textView.setTextSize(40);
            button.setText("Start");
            seekBar.setEnabled(true);
            seekBar.setVisibility(View.VISIBLE);
            seekBar.setProgress(30);
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



    public void Music(String musicLag, boolean musicFlag) {

        if  (musicFlag == true) {

            long[] mill = {1000, 1000};
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.fly_away);

            switch (musicLag) {

                case "vibrate_and_sound":
                    vibrator.vibrate(mill, 0);
                    mediaPlayer.start();
                    break;

                case "sound_without_vibrate":
                    mediaPlayer.start();
                    break;

                case "vibrate_without_sound":
                    vibrator.vibrate(mill, 0);
                    break;

                case "no_vibrate_no_sound":
                    break;
            }
        }
        else {

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
}
