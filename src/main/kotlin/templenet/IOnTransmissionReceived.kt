package templenet

import com.github.cliftonlabs.json_simple.JsonObject

interface IOnTransmissionReceived {
    fun onReceived(json: JsonObject)
}