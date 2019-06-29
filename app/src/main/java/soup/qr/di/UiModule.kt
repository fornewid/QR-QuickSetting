package soup.qr.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import soup.qr.QrActivity
import soup.qr.di.scope.ActivityScope
import soup.qr.di.ui.DetectUiModule
import soup.qr.di.ui.ResultUiModule

@Module
abstract class UiModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        DetectUiModule::class,
        ResultUiModule::class
    ])
    abstract fun mainActivity(): QrActivity
}
