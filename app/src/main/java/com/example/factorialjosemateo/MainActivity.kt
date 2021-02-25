package com.example.factorialjosemateo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import com.example.factorialjosemateo.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var stringBuilder= StringBuilder()
    var parametroTarea1 = 0
    var resultado = 1
    private lateinit var task1:MyTask
    private lateinit var job1:Job


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)

        binding.statusText?.movementMethod= ScrollingMovementMethod()
        stringBuilder = StringBuilder("Empezando actividad\n")
        stringBuilder.append("Introduce un Numero\n")
        binding.statusText.text = "${stringBuilder.toString()}"
        task1 = MyTask(this, "Tarea 1", 1.0)
        binding.btnAsync.setOnClickListener {
            if (!binding.txtNumber.text.isEmpty()) {
                parametroTarea1 = binding.txtNumber.text.toString().toInt()
                binding.btnAsync.isEnabled = false
                startTasks()
            }
        }

    }

    fun startTasks(){
        resultado = 1
        job1 = MainScope().launch {
            task1.execute(parametroTarea1)
        }
        binding.btnAsync.isEnabled = true
    }


    suspend fun actualizacion(valor:Int) = withContext(Dispatchers.Main){
        var numAux = resultado
        var result = valor * numAux
        stringBuilder.append("${valor} * ${numAux} = ${result}\n")
        resultado = result
        binding.statusText.text = "${stringBuilder.toString()}"
    }

    suspend fun finTarea(mensaje:String) = withContext(Dispatchers.Main){
        stringBuilder.append("${mensaje}\n")
        binding.statusText.text = "${stringBuilder.toString()}"
    }


    suspend fun executeDesdeActividad(numero : Int, tiempo:Double) = withContext(Dispatchers.IO){
        try {
            for (num in 1..numero) {
                Thread.sleep((2000 * tiempo).toLong())
                actualizacion(num)
            }
        }
        catch (e:CancellationException){
            finTarea(e.message!!)
        }
        finally {
            if (isActive){
                finTarea("Tarea finalizada")
            }
        }
    }
}