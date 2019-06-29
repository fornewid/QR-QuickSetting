package soup.qr

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import soup.qr.di.DaggerApplicationComponent

class BarcodeApplication : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        LogTracker.install(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.factory().create(this)
    }
}
