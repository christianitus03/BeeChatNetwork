package templenet

import com.digi.xbee.api.RemoteDigiMeshDevice
import java.io.ByteArrayOutputStream

class DeviceHandler(val device: RemoteDigiMeshDevice, var receiver: IOnTransmissionReceived) {
    val buffer = ByteArrayOutputStream()
    var keySet = KeySet()
}