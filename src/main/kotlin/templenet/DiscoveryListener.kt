package templenet

import com.digi.xbee.api.DigiMeshDevice
import com.digi.xbee.api.DigiMeshNetwork
import com.digi.xbee.api.RemoteXBeeDevice
import com.digi.xbee.api.listeners.IDiscoveryListener
import templenet.desktop.MainMenu

class DiscoveryListener(val localDevice: DigiMeshDevice, val network: DigiMeshNetwork): IDiscoveryListener {
    override fun deviceDiscovered(device: RemoteXBeeDevice) {
        println("Found $device")
    }

    override fun discoveryError(error: String) {
        System.err.println("Error during discovery: $error")
    }

    override fun discoveryFinished(message: String?) {
        println("Discovery finished. $message")
        MainMenu(localDevice, network)
    }
}