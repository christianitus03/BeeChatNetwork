package templenet

import com.digi.xbee.api.DigiMeshDevice
import com.digi.xbee.api.DigiMeshNetwork
import com.digi.xbee.api.RemoteDigiMeshDevice
import com.digi.xbee.api.RemoteXBeeDevice

fun main(args: Array<String>) {
    val device = DigiMeshDevice(args[0], args[1].toInt())
    device.open()

    val network = device.network as DigiMeshNetwork
    network.addDiscoveryListener(DiscoveryListener(device, network))
    network.startDiscoveryProcess()

}

fun toDigiMesh(devices: List<RemoteXBeeDevice>): List<RemoteDigiMeshDevice> {
    val deviceList = ArrayList<RemoteDigiMeshDevice>()

    for (device in devices) {
        deviceList.add(device as RemoteDigiMeshDevice)
    }

    return deviceList.toList()
}