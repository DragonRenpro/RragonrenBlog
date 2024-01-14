package com.dragonren.blog.runner

import com.alibaba.fastjson.JSONObject
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket

@Service
class SensorConnection {
    private var currentData: SensorData = SensorData(0f, 0f, 0f)
    private val serverSocket = ServerSocket(9001)

    init {
        Thread { waitConnect() }.start()
    }

    private fun verifyMessage(socket: Socket): Boolean {
        try {
            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            val message = reader.readLine()
            return message?.contains("sensor_client") ?: false
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun loop(socket: Socket) {
        try {
            val outputStream: OutputStream = socket.getOutputStream()
            val inputStream: BufferedReader = BufferedReader(InputStreamReader(socket.getInputStream()))

            while (!serverSocket.isClosed) {
                outputStream.write("get_full_data;\n".toByteArray())

                val message = inputStream.readLine()

                if (message != null && message.contains(";")) {
                    val json = JSONObject.parseObject(message.substring(0, message.indexOf(";")))
                    synchronized(this) {
                        currentData = SensorData(
                            json.getFloat("thermal"),
                            json.getFloat("light"),
                            json.getFloat("pressure")
                        )
                    }
                }
                Thread.sleep(1000)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun waitConnect() {
        while (!serverSocket.isClosed) {
            try {
                val socket = serverSocket.accept()
                if (verifyMessage(socket)) {
                    socket.getOutputStream().write("sensor_server;\n".toByteArray())
                    println("Sensor connected")
                    loop(socket)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getSensorData(): SensorData {
        return currentData
    }

    fun close() {
        serverSocket.close()
    }

    data class SensorData(var thermal: Float, var light: Float, var pressure: Float) {
        override fun toString(): String {
            return "temperature: $thermal, light: $light, pressure: $pressure"
        }
    }
}
