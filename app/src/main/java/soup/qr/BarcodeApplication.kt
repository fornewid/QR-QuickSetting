package soup.qr

import android.app.Application
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import com.jakewharton.threetenabp.AndroidThreeTen

class BarcodeApplication : Application(), CameraXConfig.Provider {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        LogTracker.install(this)
        dependency = Dependency(this)
    }

    override fun getCameraXConfig(): CameraXConfig {
        return Camera2Config.defaultConfig()
    }

    companion object {

        internal lateinit var dependency: Dependency
    }
}
