package soup.qr.di

import android.content.Context
import dagger.Module
import dagger.Provides
import soup.qr.BarcodeApplication

@Module
class ApplicationModule {

    @Provides
    fun provideContext(
        application: BarcodeApplication
    ): Context = application.applicationContext
}
