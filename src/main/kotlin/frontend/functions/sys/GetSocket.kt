package org.poach3r.frontend.functions.sys;

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import java.net.Socket
import java.net.UnixDomainSocketAddress
import java.nio.channels.SocketChannel

class GetSocket : PCallable {
    override val arity: Int = 1

    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        val socketPath = arguments[0].toString()
        return try {
            // For Unix domain sockets (Java 16+)
            val address = UnixDomainSocketAddress.of(socketPath)
            val socketChannel = SocketChannel.open()
            socketChannel.connect(address)

            socketChannel
        } catch (e: Exception) {
            throw RuntimeException("Error creating socket: ${e.message}")
        }
    }
}