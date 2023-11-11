package kz.just_code.livedataflowapp.live_event

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

class LiveEvent<T> {
    private val listeners: MutableMap<Listener<T>, LifecycleOwner> = mutableMapOf()
    private val publishStates: List<Lifecycle.State> =
        listOf(Lifecycle.State.STARTED, Lifecycle.State.RESUMED)
    var data: T? = null
        private set

    fun setValue(value: T?) {
        this.data = value
        listeners.forEach {
            if (it.value.lifecycle.currentState in publishStates) {
                it.key.update(value)
            }
        }
    }

    fun listen(owner: LifecycleOwner, listener: Listener<T>) {
        listeners[listener] = owner
    }
}

fun interface Listener<T> {
    fun update(data: T?)
}