package soup.qr.di.ui

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import soup.qr.di.scope.FragmentScope
import soup.qr.di.scope.ViewModelKey
import soup.qr.ui.magnified.BarcodeMagnifiedFragment
import soup.qr.ui.magnified.BarcodeMagnifiedViewModel

@Module
abstract class MagnifiedUiModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun bindBarcodeMagnifiedFragment(): BarcodeMagnifiedFragment

    @Binds
    @IntoMap
    @ViewModelKey(BarcodeMagnifiedViewModel::class)
    abstract fun bindBarcodeMagnifiedViewModel(viewModel: BarcodeMagnifiedViewModel): ViewModel
}
