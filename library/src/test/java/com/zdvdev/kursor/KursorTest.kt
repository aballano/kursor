package com.zdvdev.kursor

import android.database.Cursor
import android.database.MatrixCursor
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.ArrayDeque

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class KursorTest {

    val columnNames = arrayOf("ID", "NAME", "SURNAME")
    val row1 = arrayListOf("1", "Daenerys", "Targaryen")
    val row2 = arrayListOf("2", "John", "Snow")

    lateinit var kursor: Cursor
    lateinit var deque: ArrayDeque<String>

    @Before fun setUp() {
        kursor = MatrixCursor(columnNames).apply {
            addRow(row1)
            addRow(row2)
        }

        deque = ArrayDeque<String>(row1 + row2)
    }

    @Test fun `rows foreach should iterate all the rows`() {
        kursor.rows().forEach { row ->
            row.forEach {
                assertThat(it).isEqualTo(deque.pop())
            }
        }
    }

    @Test fun `foreach should iterate all the rows`() {
        kursor.forEach { row ->
            row.forEach {
                assertThat(it).isEqualTo(deque.pop())
            }
        }
    }

    @Test fun `foreachIndexed should iterate all the rows`() {
        kursor.forEachIndexed { _, row ->
            row.forEach {
                assertThat(it).isEqualTo(deque.pop())
            }
        }
    }

    @Test fun `indexed access should iterate all the elements`() {
        for (rowIndex in 0 until kursor.rowSize) {
            for (columnIndex in 0 until kursor.columnSize) {
                assertThat(kursor[rowIndex][columnIndex]).isEqualTo(deque.pop())
            }
        }
    }

    @Test fun `rowSize should be cursor row count`() {
        assertThat(kursor.rowSize).isEqualTo(kursor.count)
    }

    @Test fun `columnSize should be cursor column count`() {
        assertThat(kursor.columnSize).isEqualTo(kursor.columnCount)
    }

    @Test fun `should properly format with default parameters`() {
        assertThat(kursor.toFormattedString()).isEqualTo("""
+-----------+-----------+-----------+
| ID        | NAME      | SURNAME   |
+-----------+-----------+-----------+
| 1         | Daenerys  | Targaryen |
| 2         | John      | Snow      |
+-----------+-----------+-----------+
""".trimStart())
    }

    @Test fun `should properly format without column names`() {
        assertThat(kursor.toFormattedString(includeColumnNames = false)).isEqualTo("""
+-----------+-----------+-----------+
| 1         | Daenerys  | Targaryen |
| 2         | John      | Snow      |
+-----------+-----------+-----------+
""".trimStart())
    }

    @Test fun `should properly format without column names and row separator`() {
        assertThat(kursor.toFormattedString(includeColumnNames = false, rowsSeparator = true)).isEqualTo("""
+-----------+-----------+-----------+
| 1         | Daenerys  | Targaryen |
+-----------+-----------+-----------+
| 2         | John      | Snow      |
+-----------+-----------+-----------+
""".trimStart())
    }

    @Test fun `should properly format with row separator`() {
        assertThat(kursor.toFormattedString(rowsSeparator = true)).isEqualTo("""
+-----------+-----------+-----------+
| ID        | NAME      | SURNAME   |
+-----------+-----------+-----------+
| 1         | Daenerys  | Targaryen |
+-----------+-----------+-----------+
| 2         | John      | Snow      |
+-----------+-----------+-----------+
""".trimStart())
    }

    @Test fun `should properly implement row toString`() {
        assertThat(kursor[0].toString()).isEqualTo("1 Daenerys Targaryen")
    }
}

