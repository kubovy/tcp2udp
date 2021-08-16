package com.poterion.tcp2udp

/**
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun main() {
	val udpClient = UdpClient()
	val tcpClient = TcpClient { udpClient.broadcast(it) }
	udpClient.start()
	tcpClient.start("127.0.0.1", 3000)

	while (tcpClient.isRunning || udpClient.isRunning) Thread.sleep(100L)
}
