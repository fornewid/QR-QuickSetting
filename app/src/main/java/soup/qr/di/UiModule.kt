package soup.qr.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import soup.qr.BarcodeActivity
import soup.qr.di.scope.ActivityScope
import soup.qr.di.ui.DetectUiModule
import soup.qr.di.ui.HistoryUiModule
import soup.qr.di.ui.MagnifiedUiModule
import soup.qr.di.ui.ResultUiModule

@Module
abstract class UiModule {

    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            HistoryUiModule::class,
            DetectUiModule::class,
            ResultUiModule::class,
            MagnifiedUiModule::class
        ]
    )
    abstract fun bindBarcodeActivity(): BarcodeActivity
}
