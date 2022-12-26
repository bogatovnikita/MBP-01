package jamycake.lifecycle_aware

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

fun <T : Any> Fragment.lifecycleAware(
    onCleared: ViewModel.() -> Unit = {},
    creator: ViewModel.() -> T) : Lazy<T>{
    return LifeCycleAwareContainer(onCleared, creator, this)
}

fun <T : Any> AppCompatActivity.lifecycleAware(
    onCleared: ViewModel.() -> Unit = {},
    creator: ViewModel.() -> T) : Lazy<T>{
    return LifeCycleAwareContainer(onCleared, creator, this)
}

private class LifeCycleAwareContainer <T : Any>(
    private val onCleared: ViewModel.() -> Unit,
    private val creator: ViewModel.() -> T,
    private val viewModelStoreOwner: ViewModelStoreOwner
) : Lazy<T>{

    private var cached: T? = null
    @Suppress("unchecked_cast")
    override val value: T
        get() {
            if (cached == null){
                cached = ViewModelProvider(viewModelStoreOwner,
                    LifecycleAware.Factory(onCleared, creator)
                )[creator::class.java.name, LifecycleAware::class.java]
                .created as T
            }

            return cached!!
        }

    override fun isInitialized(): Boolean = cached != null
}

@Suppress("unchecked_cast")
private class LifecycleAware(
    private val onCleared: ViewModel.() -> Unit,
    creator: ViewModel.() -> Any
) : ViewModel() {

    val created by lazy { creator.invoke(this) }

    override fun onCleared() {
        this.onCleared.invoke(this)
    }

    class Factory<R : Any>(
        private val onCleared: ViewModel.() -> Unit,
        private val creator: ViewModel.() -> R
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LifecycleAware(onCleared, creator) as T
        }
    }

}