package com.poterion.tcp2udp

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

/**
 * @author Jan Kubovy [jan@kubovy.eu]
 */
class UdpClient {
	private var address: InetAddress = InetAddress.getByName("255.255.255.255")
	private var port = 4445
	private var socket: DatagramSocket? = null

	var isRunning: Boolean = false
		private set

	fun start(address: String? = null, port: Int = 4445) {
		address?.let { InetAddress.getByName(it) }?.also { this.address = it }
		this.port = port
		println("[UDP] START ${this.address.hostAddress}:${this.port}")
		socket = DatagramSocket()
		socket?.broadcast = true
		isRunning = true
	}

	fun broadcast(message: String) {
		val buffer = message.toByteArray()
		val packet = DatagramPacket(buffer, buffer.size, address, port)
		socket?.send(packet)
		println("[UDP] >> ${message}")
	}

	fun stop() {
		isRunning = false
		socket?.close()
		println("[UDP] STOP")
	}
}
