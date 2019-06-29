package soup.qr.di.ui

import dagger.Module
import dagger.android.ContributesAndroidInjector
import soup.qr.di.scope.FragmentScope
import soup.qr.ui.result.QrResultFragment

@Module
abstract class ResultUiModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideQrResultFragment(): QrResultFragment
}
