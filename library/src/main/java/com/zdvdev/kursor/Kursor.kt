@file:Suppress("unused")

package com.zdvdev.kursor

import android.database.Cursor
import android.database.Cursor.FIELD_TYPE_BLOB
import android.database.Cursor.FIELD_TYPE_FLOAT
import android.database.Cursor.FIELD_TYPE_INTEGER
import android.database.Cursor.FIELD_TYPE_STRING

fun Cursor.rows() = moveToPosition(-1).let {
    object : Iterator<KursorRow> {
        private var index = 0
        override fun next() = this@rows[index++]
        override fun hasNext() = index < rowSize
    }
}

inline fun Cursor.forEach(action: (List<Any?>) -> Unit) {
    for (index in 0 until rowSize) action(this[index])
}

inline fun Cursor.forEachIndexed(action: (Int, List<Any?>) -> Unit) {
    for (index in 0 until rowSize) action(index, this[index])
}

val Cursor.rowSize: Int
    get() = count

val Cursor.columnSize: Int
    get() = columnCount

operator fun Cursor.get(index: Int) = when {
    moveToPosition(index) -> KursorRow(
            (0..columnCount - 1).map {
                when (getType(it)) {
                    FIELD_TYPE_INTEGER -> getInt(it)
                    FIELD_TYPE_FLOAT -> getFloat(it)
                    FIELD_TYPE_STRING -> getString(it)
                    FIELD_TYPE_BLOB -> getBlob(it)
                    else -> null
                }
            }
    )
    else -> throw IndexOutOfBoundsException(outOfBoundsMsg(this, index))
}

fun Cursor.toFormattedString(includeColumnNames: Boolean = true,
                             rowsSeparator: Boolean = false,
                             columnWidth: Int = -1) = StringBuilder().apply {
    val width: Int = (if (columnWidth <= 0) {
        val list = arrayListOf(*columnNames)
        this@toFormattedString.forEach { row -> row.forEach { list.add(it?.toString()) } }
        list.max()?.length
    } else null) ?: columnWidth
    val format = "${"| %-${width}s ".repeat(columnSize)}|%n"
    val separator = String.format("${"+-${"-".repeat(width)}-".repeat(columnSize)}+%n")

    append(separator)
    if (includeColumnNames) {
        append(String.format(format, *columnNames))
        append(separator)
    }

    this@toFormattedString.forEach {
        append(String.format(format, *it.toTypedArray()))
        if (rowsSeparator) append(separator)
    }

    if (!rowsSeparator) append(separator)
}.toString()

private fun outOfBoundsMsg(cursor: Cursor, index: Int): String {
    return "Index: $index, Size: ${cursor.count}"
}

data class KursorRow(val map: List<Any?>) : List<Any?> by map {
    override fun toString() = StringBuilder().apply {
        map.reduce { acc, s -> "$acc $s" }.apply { append(this) }
    }.toString()

    @Suppress("UNCHECKED_CAST")
    infix fun <T> getAs(index: Int) = get(index) as T

    infix fun stringAt(index: Int) = get(index) as String
    infix fun floatAt(index: Int) = get(index) as Float
    infix fun intAt(index: Int) = get(index) as Int
    infix fun blobAt(index: Int) = get(index) as ByteArray

    infix fun nullableStringAt(index: Int) = get(index) as String?
    infix fun nullableFloatAt(index: Int) = get(index) as Float?
    infix fun nullableIntAt(index: Int) = get(index) as Int?
    infix fun nullableBlobAt(index: Int) = get(index) as ByteArray?
}