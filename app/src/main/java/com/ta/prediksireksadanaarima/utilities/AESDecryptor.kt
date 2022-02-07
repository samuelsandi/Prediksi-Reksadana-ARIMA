package com.ta.prediksireksadanaarima.utilities

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AESDecryptor(private var dataString: String) {

    fun decrypt(): String {
        val cipher = Cipher.getInstance("AES_256/CBC/PKCS5PADDING")
        val ivSpec = IvParameterSpec(dataString.take(32).decodeHex())
        val key = SecretKeySpec(dataString.takeLast(32).toByteArray(), "AES")
        val data = dataString.drop(32).dropLast(32).decodeHex()
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec)

        return cipher.doFinal(data).decodeToString()
    }

    private fun String.decodeHex(): ByteArray {
        check(length % 2 == 0) { "Must have an even length" }

        return chunked(2)
            .map { it.toInt(16).toByte() }
            .toByteArray()
    }
}