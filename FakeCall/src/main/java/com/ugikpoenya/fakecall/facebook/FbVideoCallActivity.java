package com.ugikpoenya.fakecall.facebook;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;
import com.ugikpoenya.fakecall.FakeCallManager;
import com.ugikpoenya.fakecall.R;
import com.ugikpoenya.fakecall.model.KontakModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class FbVideoCallActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    CircleImageView gambrH;
    ImageView gambrB, imgback;
    MediaPlayer mp;
    RelativeLayout terima, tolak, tolak2;
    LinearLayout atas, bawah;
    Handler handler;
    TextView calling;
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    VideoView videoView;
    private KontakModel kontakModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_video_call);

        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.setVisibility(View.GONE);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setFormat(PixelFormat.OPAQUE);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
        videoView = findViewById(R.id.videoView);
        videoView.setMediaController(null);

        kontakModel = new FakeCallManager().getKontak();
        String uriPath = kontakModel.getVideo_url();
        if (kontakModel.getVideo_url().startsWith("https://")) {
            Uri uri = Uri.parse(uriPath);
            videoView.setVideoURI(uri);
            videoView.requestFocus();
        } else if (kontakModel.getVideo_url().startsWith("http://")) {
            Uri uri = Uri.parse(uriPath);
            videoView.setVideoURI(uri);
            videoView.requestFocus();
        } else {
            videoView.setVideoURI(Uri.parse(kontakModel.getVideo_url()));
            videoView.requestFocus();
        }

        handler = new Handler();
        atas = findViewById(R.id.layutama);
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
            mp.stop();
            surfaceView.setVisibility(View.VISIBLE);
            atas.setVisibility(View.GONE);
            bawah.setVisibility(View.VISIBLE);
            gambrB.setVisibility(View.GONE);
            videoView.start();
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

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camera = Camera.open(1);
        Camera.Parameters parameters;
        parameters = camera.getParameters();
        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
        }

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

}