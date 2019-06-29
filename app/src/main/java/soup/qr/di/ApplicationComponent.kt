package soup.qr.di

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import soup.qr.QrApplication
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ApplicationModule::class,
        UiModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<QrApplication> {

    @Component.Factory
    interface Factory : AndroidInjector.Factory<QrApplication>
}
