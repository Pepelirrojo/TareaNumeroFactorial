package com.example.factorialjosemateo


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext


class MyTask(val actividad: MainActivity, val nombre: String, val tiempo: Double) {

    suspend fun execute(numero : Int) = withContext(Dispatchers.IO){
        try {
            for (num in 1..numero) {
                Thread.sleep((2000 * tiempo).toLong())
                actividad.actualizacion(num)
            }
        }
        catch (e: java.util.concurrent.CancellationException){
            actividad.finTarea(e.message!!)
        }
        finally {
            if (isActive){
                actividad.finTarea("Tarea finalizada")
            }
        }
    }


}