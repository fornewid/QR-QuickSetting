package soup.qr

import android.app.Application
import timber.log.Timber

class QrApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        LogTracker.install(this)
    }
}
