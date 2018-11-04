package org.wit.hillfort.helpers

/*
Courtesy of https://www.masinamichele.it/2018/02/13/implementing-rsa-cryptography-in-kotlin/
 */

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

const val CRYPTO_METHOD = "RSA"
const val CRYPTO_BITS = 2048
const val CRYPTO_TRANSFORM = "RSA/ECB/OAEPWithSHA1AndMGF1Padding"

var publicKey = ""
var privateKey = "" // both used throughout for encryption/decryption

// Generates a private and public key pair
fun generateKeyPair() {
    val kp: KeyPair
    val kpg: KeyPairGenerator = KeyPairGenerator.getInstance(CRYPTO_METHOD)

    kpg.initialize(CRYPTO_BITS)
    kp = kpg.genKeyPair()

    publicKey = kp.public.key()
    privateKey = kp.private.key()
}

// Converts a string to a PublicKey object - helper
fun String.toPublicKey(): PublicKey {
    val keyBytes: ByteArray = Base64.decode(this, Base64.DEFAULT)
    val spec = X509EncodedKeySpec(keyBytes)
    val keyFactory = KeyFactory.getInstance(CRYPTO_METHOD)

    return keyFactory.generatePublic(spec)
}

// Converts a string to a PrivateKey object - helper
fun String.toPrivateKey(): PrivateKey {
    val keyBytes: ByteArray = Base64.decode(this, Base64.DEFAULT)
    val spec = PKCS8EncodedKeySpec(keyBytes)
    val keyFactory = KeyFactory.getInstance(CRYPTO_METHOD)

    return keyFactory.generatePrivate(spec)
}

// Encrypts a string
fun encrypt(message: String, key: String): String {
    val encryptedBytes: ByteArray
    val pubKey: PublicKey? = key.toPublicKey()
    val cipher: Cipher = Cipher.getInstance(CRYPTO_TRANSFORM)

    cipher.init(Cipher.ENCRYPT_MODE, pubKey)
    encryptedBytes = cipher.doFinal(message.toByteArray(StandardCharsets.UTF_8))

    return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
}

// Decrypts a message
fun decrypt(message: String): String {
    val decryptedBytes: ByteArray
    val key: PrivateKey? = privateKey.toPrivateKey()
    val cipher: Cipher = Cipher.getInstance(CRYPTO_TRANSFORM)

    cipher.init(Cipher.DECRYPT_MODE, key)
    decryptedBytes = cipher.doFinal(Base64.decode(message, Base64.DEFAULT))

    return String(decryptedBytes)
}

fun PublicKey.key() = Base64.encodeToString(this.encoded, Base64.DEFAULT)!!
fun PrivateKey.key() = Base64.encodeToString(this.encoded, Base64.DEFAULT)!!