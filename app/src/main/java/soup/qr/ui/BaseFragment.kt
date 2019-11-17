package soup.qr.ui

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected inline fun <reified VM : ViewModel> activityViewModel(): Lazy<VM> =
        activityViewModels { viewModelFactory }

    protected inline fun <reified VM : ViewModel> viewModel(): Lazy<VM> =
        viewModels { viewModelFactory }

    protected inline fun <reified VM : ViewModel> parentViewModel(): Lazy<VM> =
        viewModels({ requireParentFragment() }, { viewModelFactory })
}
