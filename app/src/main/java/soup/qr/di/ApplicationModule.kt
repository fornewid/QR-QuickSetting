package soup.qr.di

import android.content.Context
import dagger.Module
import dagger.Provides
import soup.qr.QrApplication

@Module
class ApplicationModule {

    @Provides
    fun provideContext(
        application: QrApplication
    ): Context = application.applicationContext
}
