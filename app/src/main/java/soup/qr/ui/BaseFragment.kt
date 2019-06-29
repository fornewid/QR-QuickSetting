package soup.qr.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
import soup.qr.utils.activityViewModelProvider
import soup.qr.utils.lazyFast
import soup.qr.utils.parentViewModelProvider
import soup.qr.utils.viewModelProvider
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected inline fun <reified VM : ViewModel> activityViewModel(): Lazy<VM> =
        lazyFast { activityViewModelProvider<VM>(viewModelFactory) }

    protected inline fun <reified VM : ViewModel> viewModel(): Lazy<VM> =
        lazyFast { viewModelProvider<VM>(viewModelFactory) }

    protected inline fun <reified VM : ViewModel> parentViewModel(): Lazy<VM> =
        lazyFast { parentViewModelProvider<VM>(viewModelFactory) }
}
