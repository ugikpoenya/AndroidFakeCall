package com.ugikpoenya.fakecall.telegram;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.ugikpoenya.fakecall.FakeCallManager;
import com.ugikpoenya.fakecall.R;
import com.ugikpoenya.fakecall.model.KontakModel;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeleVoiceCallActivity extends AppCompatActivity {
    MediaPlayer mp;
    int Seconds, Minutes, MilliSeconds, hours;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    Handler handler;
    CircleImageView gambrH;
    private RelativeLayout cancel, terima, pesan, tolak;
    private LinearLayout atas, bawah;
    private TextView calling, nameuser;

    private KontakModel kontakModel;
    public Runnable runnable = new Runnable() {

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;
            hours = Minutes / 60;
            MilliSeconds = (int) (UpdateTime % 1000);
            calling.setText(String.format("%02d", hours) + ":" + String.format("%02d", Minutes) + ":"
                    + String.format("%02d", Seconds));
            handler.postDelayed(this, 0);
        }

    };
    private ImageView imguser, imguser2, adduser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tele_voice_call);
        atas = findViewById(R.id.atas);
        bawah = findViewById(R.id.bawah);
        calling = findViewById(R.id.txtcall);
        imguser2 = findViewById(R.id.imguser2);
        adduser = findViewById(R.id.adduser);
        adduser.setVisibility(View.INVISIBLE);
        cancel = findViewById(R.id.layclose2);
        tolak = findViewById(R.id.layclose);
        pesan = findViewById(R.id.laypesan);
        pesan.setOnClickListener(v -> {
            mp.stop();
            finish();

        });
        handler = new Handler();
        tolak.setOnClickListener(v -> {
            mp.stop();
            finish();
        });

        cancel.setOnClickListener(v -> {
            mp.stop();
            finish();
        });

        kontakModel = new FakeCallManager().getKontak();

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        mp = MediaPlayer.create(getApplicationContext(), notification);
        mp.start();
        mp.setLooping(true);
        TextView judulH = findViewById(R.id.txtname);
        judulH.setText(kontakModel.getNamefake());
        gambrH = findViewById(R.id.imguser);

        Glide.with(this).load(kontakModel.getImage_url())
                .into(gambrH);

        Glide.with(this).load(kontakModel.getImage_url())
                .into(imguser2);


        terima = findViewById(R.id.layterima);
        terima.setOnClickListener(view -> {
            atas.setVisibility(View.GONE);
            tolak.setVisibility(View.VISIBLE);
            bawah.setVisibility(View.VISIBLE);
            imguser2.setVisibility(View.VISIBLE);
            StartTime = SystemClock.uptimeMillis();
            handler.postDelayed(runnable, 0);


            String url = kontakModel.getVoice_url();
            mp.stop();
            try {

                mp = new MediaPlayer();
                if (url.startsWith("http")) {
                    mp.setDataSource(url);
                    mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                } else {
                    AssetFileDescriptor descriptor;
                    descriptor = getAssets().openFd(url);
                    mp.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                    descriptor.close();
                    mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                }
                mp.prepareAsync();
                mp.setOnPreparedListener(mp -> mp.start());

            } catch (IllegalArgumentException | IllegalStateException | IOException e) {
                e.printStackTrace();
            }
        });


    }

    public void onBackPressed() {
        mp.stop();
        finish();
    }
}