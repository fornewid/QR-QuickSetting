package soup.qr

import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import soup.qr.di.DaggerApplicationComponent

class BarcodeApplication : DaggerApplication(), CameraXConfig.Provider {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        LogTracker.install(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.factory().create(this)
    }

    override fun getCameraXConfig(): CameraXConfig {
        return Camera2Config.defaultConfig()
    }
}
