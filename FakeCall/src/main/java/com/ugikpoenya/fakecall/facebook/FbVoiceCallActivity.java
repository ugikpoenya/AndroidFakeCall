package com.ugikpoenya.fakecall.facebook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
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


public class FbVoiceCallActivity extends AppCompatActivity {
    CircleImageView gambrH;
    ImageView gambrB, imgback;
    MediaPlayer mp;
    RelativeLayout terima, tolak, tolak2;
    LinearLayout atas, bawah;
    int Seconds, Minutes, MilliSeconds, hours;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    Handler handler;
    TextView calling;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(1280);
        getWindow().setStatusBarColor(1140850688);
        int currentApiVersion = Build.VERSION.SDK_INT;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }
        setContentView(R.layout.activity_fb_voice_call);
        kontakModel = new FakeCallManager().getKontak();

        handler = new Handler();
        atas = findViewById(R.id.laybawah1);
        bawah = findViewById(R.id.laybawah2);
        calling = findViewById(R.id.txtwaktu);

        mp = MediaPlayer.create(this, R.raw.facebook);
        mp.start();
        mp.setLooping(true);

        tolak = findViewById(R.id.laytolak);
        tolak.setOnClickListener(v -> {
            mp.stop();
            finish();
        });

        imgback = findViewById(R.id.imgback2);
        imgback.setOnClickListener(v -> {
            mp.stop();
            finish();
        });
        tolak2 = findViewById(R.id.laytolak2);
        tolak2.setOnClickListener(v -> {
            mp.stop();
            finish();
        });

        terima = findViewById(R.id.layterima);
        terima.setOnClickListener(v -> {
            StartTime = SystemClock.uptimeMillis();
            handler.postDelayed(runnable, 0);
            atas.setVisibility(View.GONE);
            bawah.setVisibility(View.VISIBLE);
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

        TextView judulH = findViewById(R.id.txtfbname);
        judulH.setText(kontakModel.getNamefake());

        gambrH = findViewById(R.id.fbimguser);
        gambrB = findViewById(R.id.imgback);

        Glide.with(this).load(kontakModel.getImage_url())
                .into(gambrH);

        Glide.with(this).load(kontakModel.getImage_url())
                .into(gambrB);
    }


    public void onBackPressed() {
        mp.stop();
        finish();
    }
}