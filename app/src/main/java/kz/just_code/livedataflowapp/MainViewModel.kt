package kz.just_code.livedataflowapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kz.just_code.livedataflowapp.live_event.LiveEvent

class MainViewModel : ViewModel() {
    val liveEvent: LiveEvent<String> = LiveEvent()

    val userLiveData: LiveData<Person> = getUserNames().asLiveData()
    val userData: MutableStateFlow<List<Person>> = MutableStateFlow(listOf())

    fun sendMessage(message: String?) {
        liveEvent.setValue(message)
        userData.value = listOf(Person("", 123))
    }

    fun getUserNames(): Flow<Person> {
        val list = listOf(
            Person("Nikolay", 24),
            Person("Ivan", 22),
            Person("Elena", 31),
            Person("Irina", 54)
        )
        return flow {
            list.forEach {
                emit(it)
                //delay(1800)
            }
        }
        /*return flowOf(
            "Nikolay", "Ivan", "Elena", "Irina"
        )*/

        //return list.asFlow()
    }
}

data class Person(
    var name: String,
    val age: Int
)