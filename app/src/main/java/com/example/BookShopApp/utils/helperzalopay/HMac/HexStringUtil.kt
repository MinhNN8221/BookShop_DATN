package com.example.BookShopApp.utils.helperzalopay.HMac

import java.util.*

class HexStringUtil {
    private val HEX_CHAR_TABLE = charArrayOf(
        '0', '1', '2', '3',
        '4', '5', '6', '7',
        '8', '9', 'a', 'b',
        'c', 'd', 'e', 'f'
    )

    /**
     * Convert a byte array to a hexadecimal string
     *
     * @param raw A raw byte array
     * @return Hexadecimal string
     */
    fun byteArrayToHexString(raw: ByteArray): String {
        val hex = CharArray(2 * raw.size)
        var index = 0

        for (b in raw) {
            val v = b.toInt() and 0xFF
            hex[index++] = HEX_CHAR_TABLE[v ushr 4]
            hex[index++] = HEX_CHAR_TABLE[v and 0x0F]
        }
        return String(hex)
    }

    /**
     * Convert a hexadecimal string to a byte array
     *
     * @param hex A hexadecimal string
     * @return The byte array
     */
    fun hexStringToByteArray(hex: String): ByteArray {
        val hexStandard = hex.toLowerCase(Locale.ENGLISH)
        val sz = hexStandard.length / 2
        val bytesResult = ByteArray(sz)

        var idx = 0
        var i = 0
        while (i < sz) {
            bytesResult[i] = hexStandard[idx].toByte()
            idx++
            val tmp = hexStandard[idx].toByte()
            idx++

            if (bytesResult[i] > HEX_CHAR_TABLE[9].toByte()) {
                bytesResult[i] = (bytesResult[i] - ('a'.toByte()) + 10).toByte()
            } else {
                bytesResult[i] = (bytesResult[i] - '0'.toByte()).toByte()
            }
            if (tmp > HEX_CHAR_TABLE[9].toByte()) {
                bytesResult[i] = (bytesResult[i] + 10).toByte()
            } else {
                bytesResult[i] = (bytesResult[i] + '0'.toByte()).toByte()
            }

            i++
        }
        return bytesResult
    }

}