package com.poterion.tcp2udp

import java.io.BufferedReader
import java.io.BufferedWriter
import java.net.Socket


/**
 * @author Jan Kubovy [jan@kubovy.eu]
 */
class TcpClient(private val onRead: (String) -> Unit) {

	private var clientSocket: Socket? = null
	private var writer: BufferedWriter? = null
	private var reader: BufferedReader? = null
	private var readerThread: Thread? = null

	var isRunning: Boolean = false
		private set

	private val readerRunnable = {
		while (isRunning) {
			while (reader?.ready() == true) try {
				reader?.readLine()
						?.also { println("[TCP] << ${it}") }
						?.also(onRead)
			} catch (e: InterruptedException) {
				e.printStackTrace()
			}
			Thread.sleep(500L)
		}
	}

	fun start(address: String, port: Int) {
		println("[TCP] START ${address}:${port}")
		clientSocket = Socket(address, port)
		writer = clientSocket?.getOutputStream()?.writer()?.buffered()
		reader = clientSocket?.getInputStream()?.reader()?.buffered()
		readerThread = Thread(readerRunnable)
		readerThread?.start()
		isRunning = true
	}

	fun send(message: String) {
		writer?.write(message)
		println("[TCP] >> ${message}")
	}

	fun stop() {
		isRunning = false
		readerThread?.interrupt()
		reader?.close()
		writer?.close()
		clientSocket?.close()
		println("[TCP] STOP")
	}
}
