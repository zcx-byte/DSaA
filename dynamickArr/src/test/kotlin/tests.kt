import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

class myDynamicArrTest {

    @Test
    fun `push добавляет элементы в конец`() {
        val arr = myDynamicArr<Double>()
        arr.push(1.0)
        arr.push(2.0)
        arr.push(3.0)

        assertEquals(3, arr.size())
        assertEquals(1.0, arr[0])
        assertEquals(2.0, arr[1])
        assertEquals(3.0, arr[2])
    }

    @Test
    fun `pop удаляет и возвращает последний элемент`() {
        val arr = myDynamicArr<String>()
        arr.push("A")
        arr.push("B")
        arr.push("C")

        assertEquals("C", arr.pop())
        assertEquals(2, arr.size())
        assertEquals("B", arr.pop())
        assertEquals("A", arr.pop())
        assertTrue(arr.isEmpty())
    }

    @Test
    fun `pop на пустом массиве бросает исключение`() {
        val arr = myDynamicArr<Int>()
        assertThrows<NoSuchElementException> {
            arr.pop()
        }
    }

    @Test
    fun `get возвращает элемент по индексу`() {
        val arr = myDynamicArr<Double>()
        for (i in 0..9) arr.push(i * 1.5)

        assertEquals(0.0, arr[0])
        assertEquals(4.5, arr[3])
        assertEquals(13.5, arr[9])
    }

    @Test
    fun `get с невалидным индексом бросает исключение`() {
        val arr = myDynamicArr<Int>()
        arr.push(1)
        arr.push(2)

        assertThrows<IndexOutOfBoundsException> { arr[-1] }
        assertThrows<IndexOutOfBoundsException> { arr[2] }
    }

    @Test
    fun `set перезаписывает элемент по индексу`() {
        val arr = myDynamicArr<Double>()
        arr.push(1.0)
        arr.push(2.0)
        arr.push(3.0)

        arr[1] = 99.9
        assertEquals(1.0, arr[0])
        assertEquals(99.9, arr[1])
        assertEquals(3.0, arr[2])
    }

    @Test
    fun `set с невалидным индексом бросает исключение`() {
        val arr = myDynamicArr<Int>()
        arr.push(1)

        assertThrows<IndexOutOfBoundsException> { arr[-1] = 5 }
        assertThrows<IndexOutOfBoundsException> { arr[1] = 5 }
    }

    @Test
    fun `removeAt удаляет элемент и сдвигает остальные`() {
        val arr = myDynamicArr<String>()
        arr.push("A")
        arr.push("B")
        arr.push("C")
        arr.push("D")

        val removed = arr.removeAt(1) // удаляем "B"

        assertEquals("B", removed)
        assertEquals(3, arr.size())
        assertEquals("A", arr[0])
        assertEquals("C", arr[1])
        assertEquals("D", arr[2])
    }

    @Test
    fun `removeAt с невалидным индексом бросает исключение`() {
        val arr = myDynamicArr<Int>()
        arr.push(1)

        assertThrows<IndexOutOfBoundsException> { arr.removeAt(-1) }
        assertThrows<IndexOutOfBoundsException> { arr.removeAt(1) }
    }

    @Test
    fun `resize увеличивает вместимость при переполнении`() {
        val arr = myDynamicArr<Int>(initialCapacity = 3)

        arr.push(1)
        arr.push(2)
        arr.push(3)
        assertEquals(3, arr.capacity)

        arr.push(4) // должно вызвать resize
        assertEquals(6, arr.capacity) // 3 * 2
        assertEquals(4, arr.size())
        assertEquals(1, arr[0])
        assertEquals(4, arr[3])
    }

    @Test
    fun `toString возвращает корректное строковое представление`() {
        val arr = myDynamicArr<Double>()

        arr.push(1.5)
        arr.push(2.5)
        arr.push(3.5)
        assertEquals("[1.5, 2.5, 3.5]", arr.toString())
    }

    @Test
    fun `isEmpty корректно определяет пустой массив`() {
        val arr = myDynamicArr<Int>()
        assertTrue(arr.isEmpty())

        arr.push(1)
        assertFalse(arr.isEmpty())

        arr.pop()
        assertTrue(arr.isEmpty())
    }
}