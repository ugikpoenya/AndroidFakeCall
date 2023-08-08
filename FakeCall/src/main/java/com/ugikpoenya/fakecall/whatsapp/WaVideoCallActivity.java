package com.ugikpoenya.fakecall.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
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

//import com.squareup.picasso.Picasso;
import com.bumptech.glide.Glide;
import com.ugikpoenya.fakecall.FakeCallManager;
import com.ugikpoenya.fakecall.R;
import com.ugikpoenya.fakecall.model.KontakModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class WaVideoCallActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    MediaPlayer mp;
    Camera camera;
    SurfaceView surfaceView, surfaceView2;
    SurfaceHolder surfaceHolder, surfaceHolder2;
    VideoView videoView;
    Handler handler;
    private TextView calling, nameuser;
    private ImageView adduser;
    private CircleImageView imguser;
    private RelativeLayout cancel, terima, pesan, tolak;
    private LinearLayout atas, bawah;

    private KontakModel kontakModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wa_video_call);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        mp = MediaPlayer.create(getApplicationContext(), notification);
        mp.start();
        mp.setLooping(true);

        atas = findViewById(R.id.atas);
        bawah = findViewById(R.id.bawah);
        videoView = findViewById(R.id.videoView);

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
        surfaceView = findViewById(R.id.surfaceView);
        surfaceView2 = findViewById(R.id.surfaceView2);
        surfaceView2.setVisibility(View.GONE);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setFormat(PixelFormat.OPAQUE);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
        surfaceHolder2 = surfaceView2.getHolder();
        surfaceHolder2.addCallback(this);
        surfaceHolder2.setFormat(PixelFormat.OPAQUE);
        surfaceHolder2.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);

        handler = new Handler();

        calling = findViewById(R.id.txtcall);
        nameuser = findViewById(R.id.txtname);
        imguser = findViewById(R.id.imguser);
        adduser = findViewById(R.id.adduser);
        adduser.setVisibility(View.INVISIBLE);
        cancel = findViewById(R.id.layclose2);
        cancel.setOnClickListener(v -> {
            mp.stop();
            finish();
        });

        pesan = findViewById(R.id.laypesan);
        pesan.setOnClickListener(v -> {
            mp.stop();
            finish();
        });

        tolak = findViewById(R.id.layclose);
        tolak.setOnClickListener(v -> {
            mp.stop();
            finish();

        });

        terima = findViewById(R.id.layterima);
        terima.setOnClickListener(v -> {
            mp.stop();
            calling.setVisibility(View.GONE);
            nameuser.setVisibility(View.GONE);
            imguser.setVisibility(View.GONE);
            adduser.setVisibility(View.VISIBLE);
            surfaceView.setVisibility(View.GONE);
            surfaceView2.setVisibility(View.VISIBLE);
            videoView.start();
            atas.setVisibility(View.GONE);
            bawah.setVisibility(View.VISIBLE);
            tolak.setVisibility(View.VISIBLE);
        });

        imguser = findViewById(R.id.imguser);
        Glide.with(this).load(kontakModel.getImage_url())
                .into(imguser);

        nameuser.setText(kontakModel.getNamefake());
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

    public void onBackPressed() {
        mp.stop();
        finish();
    }
}