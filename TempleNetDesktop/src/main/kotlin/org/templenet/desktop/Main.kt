package org.templenet.desktop

import com.digi.xbee.api.DigiMeshDevice
import com.digi.xbee.api.DigiMeshNetwork
import org.templenet.Resources

fun main(args: Array<String>) {
    println("Starting TempleNet desktop interface...")

    Resources.device = DigiMeshDevice(args[0], args[1].toInt())
    Resources.device.open()

    Resources.network = Resources.device.network as DigiMeshNetwork


}