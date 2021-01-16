package templenet

import com.digi.xbee.api.models.XBeeMessage
import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.lang.Exception
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.Security
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyAgreement
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random

class KeySet {
    companion object {
        init { Security.addProvider(BouncyCastleProvider()) }

        val CURVE = ECNamedCurveTable.getParameterSpec("c2pnb176w1")

        fun generateKeys(): KeyPair {
            val generator = KeyPairGenerator.getInstance("ECDH", "BC")
            generator.initialize(CURVE)

            return generator.generateKeyPair()
        }
    }

    val agreement: KeyAgreement = KeyAgreement.getInstance("ECDH", "BC")
    val keyPair: KeyPair = generateKeys()
    val cipher: Cipher = Cipher.getInstance("AES", "BC")
    init {
        agreement.init(keyPair.private)
    }

    fun update(message: XBeeMessage) {
        val keyBytes = message.data.copyOfRange(1, 72)
        val keySpec = X509EncodedKeySpec(keyBytes)

        val factory = KeyFactory.getInstance("ECDH", "BC")
        agreement.doPhase(factory.generatePublic(keySpec), true)
    }

    fun encryptData(unencrypted: ByteArray): ByteArray {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(agreement.generateSecret(), 0, 16, "AES"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cipher.doFinal(unencrypted)
    }

    fun decryptData(encrypted: ByteArray): ByteArray {
        try {
            cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(agreement.generateSecret(), 0, 16, "AES"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cipher.doFinal(encrypted)
    }
}