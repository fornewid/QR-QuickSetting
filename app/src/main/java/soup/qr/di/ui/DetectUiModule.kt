package soup.qr.di.ui

import dagger.Module
import dagger.android.ContributesAndroidInjector
import soup.qr.di.scope.FragmentScope
import soup.qr.ui.detect.QrDetectFragment

@Module
abstract class DetectUiModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideQrDetectFragment(): QrDetectFragment
}