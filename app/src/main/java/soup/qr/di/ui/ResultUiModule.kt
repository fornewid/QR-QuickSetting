package soup.qr.di.ui

import dagger.Module
import dagger.android.ContributesAndroidInjector
import soup.qr.di.scope.FragmentScope
import soup.qr.ui.result.BarcodeResultFragment

@Module
abstract class ResultUiModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun bindBarcodeResultFragment(): BarcodeResultFragment
}
