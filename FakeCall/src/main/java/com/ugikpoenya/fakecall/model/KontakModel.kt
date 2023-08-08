package com.ugikpoenya.fakecall.model

import java.io.Serializable

class KontakModel : Serializable {
    var namefake: String = ""
    var image_url: String = ""
    var video_url: String = ""
    var voice_url: String = ""
    var call_app: CallApp = CallApp.WA
    var call_type: CallType = CallType.VIDEO


}

enum class CallApp {
    WA, FB, TELE
}

enum class CallType {
    VIDEO, VOICE
}
