package soup.qr.di.ui

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import soup.qr.di.scope.FragmentScope
import soup.qr.di.scope.ViewModelKey
import soup.qr.ui.detect.BarcodeDetectFragment
import soup.qr.ui.detect.BarcodeDetectViewModel

@Module
abstract class DetectUiModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun bindBarcodeDetectFragment(): BarcodeDetectFragment

    @Binds
    @IntoMap
    @ViewModelKey(BarcodeDetectViewModel::class)
    abstract fun bindBarcodeDetectViewModel(viewModel: BarcodeDetectViewModel): ViewModel
}
