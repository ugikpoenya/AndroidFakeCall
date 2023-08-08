package com.ugikpoenya.fakecall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ugikpoenya.fakecall.facebook.FbVideoCallActivity;
import com.ugikpoenya.fakecall.facebook.FbVoiceCallActivity;
import com.ugikpoenya.fakecall.model.CallApp;
import com.ugikpoenya.fakecall.model.CallType;
import com.ugikpoenya.fakecall.model.KontakModel;
import com.ugikpoenya.fakecall.telegram.TeleVideoCallActivity;
import com.ugikpoenya.fakecall.telegram.TeleVoiceCallActivity;
import com.ugikpoenya.fakecall.whatsapp.WaVideoCallActivity;
import com.ugikpoenya.fakecall.whatsapp.WaVoiceCallActivity;


public class AppReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        KontakModel kontakModel = new FakeCallManager().getKontak();
        Intent intentCal;
        if (kontakModel.getCall_app() == CallApp.FB) {
            if (kontakModel.getCall_type() == CallType.VOICE) {
                intentCal = new Intent(context, FbVoiceCallActivity.class);
            } else {
                intentCal = new Intent(context, FbVideoCallActivity.class);
            }
        } else if (kontakModel.getCall_app() == CallApp.TELE) {
            if (kontakModel.getCall_type() == CallType.VOICE) {
                intentCal = new Intent(context, TeleVoiceCallActivity.class);
            } else {
                intentCal = new Intent(context, TeleVideoCallActivity.class);
            }
        } else {
            if (kontakModel.getCall_type() == CallType.VOICE) {
                intentCal = new Intent(context, WaVoiceCallActivity.class);
            } else {
                intentCal = new Intent(context, WaVideoCallActivity.class);
            }
        }

        intentCal.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentCal);
    }

}