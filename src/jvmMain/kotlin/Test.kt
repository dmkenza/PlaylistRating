import java.net.NetworkInterface
import java.net.SocketException

object Test {
    @Throws(SocketException::class)
    fun test1() {
        println("Full list of Network Interfaces:")
        val en = NetworkInterface.getNetworkInterfaces()
        while (en.hasMoreElements()) {
            val intf = en.nextElement()
            println("    " + intf.name + " " + intf.displayName)
            val enumIpAddr = intf.inetAddresses
            while (enumIpAddr.hasMoreElements()) {
                println("        " + enumIpAddr.nextElement().toString())
            }
        }
    }
}