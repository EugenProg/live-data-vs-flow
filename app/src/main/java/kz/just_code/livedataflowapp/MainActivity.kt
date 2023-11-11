package kz.just_code.livedataflowapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kz.just_code.livedataflowapp.databinding.ActivityMainBinding
import kz.just_code.livedataflowapp.live_event.LiveEvent
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.publishBtn.setOnClickListener {
            viewModel.sendMessage(binding.inputView.text.toString())
        }
        binding.getBtn.setOnClickListener {
            viewModel.sendMessage(null)
        }

        binding.collectBtn.setOnClickListener {
            lifecycleScope.launch {
                viewModel.getUserNames().onEach {
                    it.name = "${it.name}, ${it.age}"
                }.collect {
                    Log.e(">>>", it.name)
                }
                //Toast.makeText(this@MainActivity, count.toString(), Toast.LENGTH_SHORT).show()

            }
        }

        listen(viewModel.liveEvent) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        /*viewModel.liveEvent.listen(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }*/

        listenNullable(viewModel.liveEvent) {
            binding.message.text = it
        }

        /*viewModel.liveEvent.listen(this) {
            binding.message.text = it
        }*/

        viewModel.userLiveData.observe(this) {
            Log.e("Collect", ">>> ${it.name}")
        }
    }
}

inline fun <T> LifecycleOwner.listen(liveEvent: LiveEvent<T>, crossinline body: (T) -> Unit) {
    liveEvent.listen(this) {
        try {
            it?.let {
                body.invoke(it)
            }
        } catch (e: Exception) {
            Log.e("Live event listener", e.message ?: "Live event exception")
        }
    }
}

inline fun <T> LifecycleOwner.listenNullable(liveEvent: LiveEvent<T>, crossinline body: (T?) -> Unit) {
    liveEvent.listen(this) {
        try {
            body.invoke(it)
        } catch (e: Exception) {
            Log.e("Live event listener", e.message ?: "Live event exception")
        }
    }
}