package info.hauu.highloadsocial

import java.util.zip.GZIPInputStream
import kotlin.text.Charsets.UTF_8

fun ungzip(content: ByteArray): String =
    GZIPInputStream(content.inputStream()).bufferedReader(UTF_8).use { it.readText() }