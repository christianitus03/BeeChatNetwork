import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.jce.provider.BouncyCastleProvider
import templenet.KeySet
import java.awt.event.KeyAdapter
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.Security
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyAgreement
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

fun main(args: Array<String>) {
    Security.addProvider(BouncyCastleProvider())

    val unencrypted = "Penis"

    val aGenerator = KeyPairGenerator.getInstance("ECDH", "BC")
    aGenerator.initialize(ECNamedCurveTable.getParameterSpec("c2pnb176w1"))
    val aKeyPair = aGenerator.generateKeyPair()

    val bGenerator = KeyPairGenerator.getInstance("ECDH", "BC")
    bGenerator.initialize(ECNamedCurveTable.getParameterSpec("c2pnb176w1"))
    val bKeyPair = bGenerator.generateKeyPair()

    val aFactory = KeyFactory.getInstance("ECDH", "BC")
    val bFactory = KeyFactory.getInstance("ECDH", "BC")

    val aAgreement = KeyAgreement.getInstance("ECDH", "BC")
    aAgreement.init(aKeyPair.private)
    aAgreement.doPhase(aFactory.generatePublic(X509EncodedKeySpec(bKeyPair.public.encoded)), true)

    val bAgreement = KeyAgreement.getInstance("ECDH", "BC")
    bAgreement.init(bKeyPair.private)
    bAgreement.doPhase(bFactory.generatePublic(X509EncodedKeySpec(aKeyPair.public.encoded)), true)

    val aKey = SecretKeySpec(aAgreement.generateSecret(), "AES/CBC/PKCS7Padding")
    val bKey = SecretKeySpec(bAgreement.generateSecret(), "AES/CBC/PKCS7Padding")

    assert(aKey.encoded.contentEquals(bKey.encoded))


    val aCipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC")


}