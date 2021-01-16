package templenet

import com.digi.xbee.api.DigiMeshDevice
import com.digi.xbee.api.DigiMeshNetwork
import com.digi.xbee.api.RemoteDigiMeshDevice
import com.digi.xbee.api.listeners.IDataReceiveListener
import com.digi.xbee.api.models.XBeeMessage
import com.github.cliftonlabs.json_simple.JsonObject
import com.github.cliftonlabs.json_simple.Jsoner
import java.io.ByteArrayInputStream
import java.lang.Exception
import java.util.*
import java.util.logging.Logger
import kotlin.collections.HashMap

object DeviceManager {
    val logger = Logger.getLogger("Device Manager")

    lateinit var localDevice: DigiMeshDevice
    lateinit var network: DigiMeshNetwork

    @JvmStatic
    val deviceHandlerHashMap = HashMap<RemoteDigiMeshDevice, DeviceHandler>()

    @JvmStatic
    fun initialize(localDevice: DigiMeshDevice, network: DigiMeshNetwork) {
        logger.info("Initializing device manager...")
        this.localDevice = localDevice
        this.network = network

        localDevice.addDataListener { message ->
            val handler = deviceHandlerHashMap[message.device as RemoteDigiMeshDevice]

            when (message.data[0].toInt()) {
                0 -> {
                    logger.info("Received public key and request from ${message.device}, handling...")
                    handler?.keySet = KeySet()
                    handler?.keySet?.update(message)
                    val messageBytes = byteArrayOf(1) + handler?.keySet?.keyPair?.public?.encoded!!
                    logger.info("Done generating keys and shared secret. Sending public key to ${message.device}...")
                    localDevice.sendData(handler.device, messageBytes)
                }
                2 -> {
                    logger.info("Received message part from ${message.device}, writing to buffer...")
                    handler?.buffer?.write(message.data, 1, 72)
                }

                3 -> {
                    logger.info("Received all message parts from ${message.device} (${handler?.buffer?.size()}. Processing...")
                    try {
                        handler?.buffer?.write(message.data, 1, message.data.size -1)
                        val unencryptedText = handler?.keySet?.decryptData((handler.buffer.toByteArray()))?.decodeToString()
                        handler?.receiver?.onReceived(Jsoner.deserialize(unencryptedText) as JsonObject)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    handler?.buffer?.reset()
                }

            }
        }
    }

    @JvmStatic
    fun sendData(remoteDevice: RemoteDigiMeshDevice, json: JsonObject) {
        logger.info("Attempting to send data to $remoteDevice...")
        val keySet = KeySet()

        val message = byteArrayOf(0) + keySet.keyPair.public.encoded
        localDevice.sendData(remoteDevice, message)
        logger.info("Sent public key, awaiting response...")

        localDevice.addDataListener(object : IDataReceiveListener {
            override fun dataReceived(message: XBeeMessage) {
                if (message.device as RemoteDigiMeshDevice == remoteDevice) {
                    if (message.data[0].toInt() == 1) {
                        logger.info("Received public key from ${message.device}! Generating shared secret...")
                        keySet.update(message)
                        val encryptedBytes = ByteArrayInputStream(keySet.encryptData(json.toJson().encodeToByteArray()))
                        logger.info("Encrypted data. Sending to ${message.device}...")
                        while (encryptedBytes.available() > 0) {
                            if (encryptedBytes.available() >= 71) {
                                logger.info("Sending message part to ${message.device}...")
                                localDevice.sendData(remoteDevice,byteArrayOf(2) + encryptedBytes.readNBytes(71))
                            } else {
                                logger.info("Sending final message to ${message.device}!")
                                localDevice.sendData(remoteDevice, byteArrayOf(3) + encryptedBytes.readNBytes(71))
                            }
                        }
                    } else {
                        logger.warning("Received unexpected message of type ${message.data[0]} from ${message.device}!" +
                                "(Expecting type 01)")
                    }
                    logger.info("Finished transmission to $remoteDevice.")
                    localDevice.removeDataListener(this)
                }
            }
        })
    }

}