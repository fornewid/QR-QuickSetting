package soup.qr

import android.app.Application
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig

class BarcodeApplication : Application(), CameraXConfig.Provider {

    override fun onCreate() {
        super.onCreate()
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
