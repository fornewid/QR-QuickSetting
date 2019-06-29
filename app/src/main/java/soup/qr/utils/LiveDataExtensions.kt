package soup.qr.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import soup.qr.ui.EventLiveData
import soup.qr.ui.EventObserver

inline fun <T> LiveData<T>.observeState(
    owner: LifecycleOwner,
    crossinline observer: (T) -> Unit
) = observe(owner, Observer { observer(it) })

inline fun <T> EventLiveData<T>.observeEvent(
    owner: LifecycleOwner,
    crossinline observer: (T) -> Unit
) = observe(owner, EventObserver { observer(it) })
