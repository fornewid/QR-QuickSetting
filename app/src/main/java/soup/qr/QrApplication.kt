package soup.qr

import android.app.Application

class QrApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        LogTracker.install(this)
    }
}
