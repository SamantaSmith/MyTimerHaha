package com.example.mytimerhaha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private TextView textView;
    private TextView subText;
    private boolean isTimerOn;
    private Button button;
    private CountDownTimer countDownTimer;
    private boolean musicFlag;

    private MediaPlayer mediaPlayer;

    private Vibrator vibrator;



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
        musicFlag = false;



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

                    textView.setText("Timer has finished, press Stop for new timer <3");
                    textView.setTextSize(20);
                    subText.setText("");
                    Music(true);
                    musicFlag = true;


                }
            };

            countDownTimer.start();
        } else {

            subText.setText("Change time with SeekBar below");

            if (musicFlag){
            Music(false);}


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



    public void Music(boolean musicFlag) {


        if (musicFlag) {
            long[] mill = {1000, 1000};
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(mill, 0);
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.fly_away);
        mediaPlayer.start();

        } else {

            vibrator.cancel();
            mediaPlayer.reset();
        }

    }
}
