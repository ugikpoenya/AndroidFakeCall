package com.ugikpoenya.fakecall

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.ugikpoenya.fakecall.facebook.FbVideoCallActivity
import com.ugikpoenya.fakecall.facebook.FbVoiceCallActivity
import com.ugikpoenya.fakecall.model.CallApp
import com.ugikpoenya.fakecall.model.CallType
import com.ugikpoenya.fakecall.model.KontakModel
import com.ugikpoenya.fakecall.telegram.TeleVideoCallActivity
import com.ugikpoenya.fakecall.telegram.TeleVoiceCallActivity
import com.ugikpoenya.fakecall.whatsapp.WaVideoCallActivity
import com.ugikpoenya.fakecall.whatsapp.WaVoiceCallActivity
import java.util.Calendar


var KONTAK_MODEL: KontakModel = KontakModel()
private const val ALARM_REQUEST_CODE = 134

class FakeCallManager {
    fun setKontak(kontakModel: KontakModel) {
        Log.d("LOG", "Set : " + kontakModel.namefake)
        KONTAK_MODEL = kontakModel
    }

    fun getKontak(): KontakModel {
        return KONTAK_MODEL
    }

    fun showFakeCall(context: Context, kontakModel: KontakModel) {
        if (kontakModel.call_app === CallApp.FB) {
            if (kontakModel.call_type === CallType.VOICE) {
                context.startActivity(Intent(context, FbVoiceCallActivity::class.java))
            } else {
                context.startActivity(Intent(context, FbVideoCallActivity::class.java))
            }
        } else if (kontakModel.call_app === CallApp.TELE) {
            if (kontakModel.call_type === CallType.VOICE) {
                context.startActivity(Intent(context, TeleVoiceCallActivity::class.java))
            } else {
                context.startActivity(Intent(context, TeleVideoCallActivity::class.java))
            }
        } else {
            if (kontakModel.call_type === CallType.VOICE) {
                context.startActivity(Intent(context, WaVoiceCallActivity::class.java))
            } else {
                context.startActivity(Intent(context, WaVideoCallActivity::class.java))

            }
        }

    }

    fun showFakeCall(context: Context, delay: Int, message: String) {
        val alarmIntent = Intent(context, AppReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_IMMUTABLE)
        val cal = Calendar.getInstance()
        cal.add(Calendar.SECOND, delay)
        val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager[AlarmManager.RTC_WAKEUP, cal.timeInMillis] = pendingIntent
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
