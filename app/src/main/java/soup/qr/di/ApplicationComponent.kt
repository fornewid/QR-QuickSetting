package soup.qr.di

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import soup.qr.BarcodeApplication
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ApplicationModule::class,
        UiModule::class,
        ViewModelModule::class,
        DataModule::class,
        LocalDataModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<BarcodeApplication> {

    @Component.Factory
    interface Factory : AndroidInjector.Factory<BarcodeApplication>
}
