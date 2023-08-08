package com.ugikpoenya.sampleapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.ugikpoenya.fakecall.FakeCallManager;
import com.ugikpoenya.fakecall.model.CallApp;
import com.ugikpoenya.fakecall.model.CallType;
import com.ugikpoenya.fakecall.model.KontakModel;
import com.ugikpoenya.sampleapp.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;


    private static final int MY_REQUEST_CODE = 17326;
    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 5469;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        KontakModel kontakModel = new KontakModel();
        kontakModel.setNamefake("Kim Nam Joon");
        kontakModel.setImage_url("https://ugikpoenya.net/master/uploads/FakeCall/01/Kim%20Nam%20Joon.jpg");
        kontakModel.setVideo_url("https://ugikpoenya.net/master/uploads/FakeCall/01/flower.mov");
        kontakModel.setVoice_url("https://ugikpoenya.net/master/uploads/FakeCall/01/cinta_diam.mp3");
        kontakModel.setCall_type(CallType.VIDEO);

        binding.btnCallWa.setOnClickListener(view -> {
            kontakModel.setCall_app(CallApp.WA);
            new FakeCallManager().setKontak(kontakModel);
            new FakeCallManager().showFakeCall(this, kontakModel);
        });

        binding.btnCallFb.setOnClickListener(view -> {
            kontakModel.setCall_app(CallApp.FB);
            new FakeCallManager().setKontak(kontakModel);
            new FakeCallManager().showFakeCall(this, kontakModel);
        });

        binding.btnCallTele.setOnClickListener(view -> {
            kontakModel.setCall_app(CallApp.TELE);
            new FakeCallManager().setKontak(kontakModel);
            new FakeCallManager().showFakeCall(this, kontakModel);
        });

        binding.btnCallDelay.setOnClickListener(view -> {
            new FakeCallManager().showFakeCall(this, 3, "Wait for 3 second");
            finish();
        });


        checkPermissionOverlay();
        checkPermissionCamera();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!android.provider.Settings.canDrawOverlays(this)) {
                checkPermissionOverlay();
            }
        }
    }

    public void checkPermissionCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
            } else {
            }
        }
    }

    public void checkPermissionOverlay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }
        }
    }


}