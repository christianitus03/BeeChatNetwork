/*
 * This file is part of TempleNet.
 *
 *     TempleNet is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     TempleNet is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with TempleNet.  If not, see <https://www.gnu.org/licenses/>.
 */



package templenet

import com.digi.xbee.api.DigiMeshDevice
import com.digi.xbee.api.DigiMeshNetwork
import com.digi.xbee.api.RemoteDigiMeshDevice
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

object Resources {

    fun initialize(port: String, baud: Int = 230400) {
        device = DigiMeshDevice(port, baud)
        device.open()

        network = device.network as DigiMeshNetwork
    }

    init {
        Security.addProvider(BouncyCastleProvider())
    }

    lateinit var device: DigiMeshDevice
    lateinit var network: DigiMeshNetwork

    val conversations = HashMap<RemoteDigiMeshDevice, Conversation>()

    fun updateConversations() {
        conversations.clear()
        for (device in network.devices) {
            conversations[device as RemoteDigiMeshDevice] = Conversation(device as RemoteDigiMeshDevice)
        }
    }

}