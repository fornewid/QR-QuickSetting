package soup.qr.di.ui

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import soup.qr.di.scope.FragmentScope
import soup.qr.di.scope.ViewModelKey
import soup.qr.ui.history.BarcodeHistoryFragment
import soup.qr.ui.history.BarcodeHistoryViewModel

@Module
abstract class HistoryUiModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun bindBarcodeHistoryFragment(): BarcodeHistoryFragment

    @Binds
    @IntoMap
    @ViewModelKey(BarcodeHistoryViewModel::class)
    abstract fun bindBarcodeHistoryViewModel(viewModel: BarcodeHistoryViewModel): ViewModel
}
