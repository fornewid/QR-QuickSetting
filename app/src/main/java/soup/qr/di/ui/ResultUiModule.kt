package soup.qr.di.ui

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import soup.qr.di.scope.FragmentScope
import soup.qr.di.scope.ViewModelKey
import soup.qr.ui.result.BarcodeResultFragment
import soup.qr.ui.result.BarcodeResultViewModel

@Module
abstract class ResultUiModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun bindBarcodeResultFragment(): BarcodeResultFragment

    @Binds
    @IntoMap
    @ViewModelKey(BarcodeResultViewModel::class)
    abstract fun bindBarcodeResultViewModel(viewModel: BarcodeResultViewModel): ViewModel
}
