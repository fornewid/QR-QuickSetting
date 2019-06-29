package soup.qr.di.ui

import dagger.Module
import dagger.android.ContributesAndroidInjector
import soup.qr.di.scope.FragmentScope
import soup.qr.ui.detect.BarcodeDetectFragment

@Module
abstract class DetectUiModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun bindBarcodeDetectFragment(): BarcodeDetectFragment
}
