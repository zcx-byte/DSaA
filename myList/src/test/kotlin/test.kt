import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MyListTest {

    // ===== Тесты для метода add =====

    @Test
    fun `добавление элемента в пустой список`() {
        val list = MyList<Int>()
        list.add(10)
        assertEquals(1, list.size())
        assertEquals("[10]", list.toString())
    }

    @Test
    fun `добавление нескольких элементов в конец`() {
        val list = MyList<Int>()
        list.add(1)
        list.add(2)
        list.add(3)
        assertEquals(3, list.size())
        assertEquals("[1, 2, 3]", list.toString())
    }

    // ===== Тесты для метода push =====

    @Test
    fun `push добавляет элемент в начало списка`() {
        val list = MyList<Int>()
        list.push(3)
        list.push(2)
        list.push(1)
        assertEquals(3, list.size())
        assertEquals("[1, 2, 3]", list.toString())
    }

    @Test
    fun `push после add сохраняет порядок`() {
        val list = MyList<Int>()
        list.add(2)
        list.add(3)
        list.push(1)
        assertEquals("[1, 2, 3]", list.toString())
    }

    // ===== Тесты для метода pop =====

    @Test
    fun `pop удаляет и возвращает первый элемент`() {
        val list = MyList<Int>()
        list.add(10)
        list.add(20)
        val result = list.pop()
        assertEquals(10, result)
        assertEquals(1, list.size())
        assertEquals("[20]", list.toString())
    }

    @Test
    fun `pop из пустого списка выбрасывает исключение`() {
        val list = MyList<Int>()
        assertThrows<NoSuchElementException> {
            list.pop()
        }
    }

    @Test
    fun `pop последнего элемента очищает список`() {
        val list = MyList<Int>()
        list.add(42)
        list.pop()
        assertEquals(0, list.size())
        assertEquals("[]", list.toString())
    }

    // ===== Тесты для метода removeAt =====

    @Test
    fun `removeAt удаляет элемент по индексу в середине`() {
        val list = MyList<Int>()
        list.add(1)
        list.add(2)
        list.add(3)
        list.add(4)
        val removed = list.removeAt(2) // удаляем 3
        assertEquals(3, removed)
        assertEquals(3, list.size())
        assertEquals("[1, 2, 4]", list.toString())
    }

    @Test
    fun `removeAt с индексом 0 эквивалентен pop`() {
        val list = MyList<Int>()
        list.add(100)
        list.add(200)
        val result = list.removeAt(0)
        assertEquals(100, result)
        assertEquals("[200]", list.toString())
    }

    @Test
    fun `removeAt удаляет последний элемент`() {
        val list = MyList<Int>()
        list.add(1)
        list.add(2)
        list.add(3)
        val removed = list.removeAt(2)
        assertEquals(3, removed)
        assertEquals("[1, 2]", list.toString())
    }

    @Test
    fun `removeAt с отрицательным индексом выбрасывает исключение`() {
        val list = MyList<Int>()
        list.add(1)
        assertThrows<IllegalArgumentException> {
            list.removeAt(-1)
        }
    }

    @Test
    fun `removeAt с индексом за границей выбрасывает исключение`() {
        val list = MyList<Int>()
        list.add(1)
        assertThrows<IllegalArgumentException> {
            list.removeAt(1) // индекс 1 недопустим для списка размером 1
        }
    }

    // ===== Тесты для метода removeEl =====

    @Test
    fun `removeEl удаляет первое вхождение значения`() {
        val list = MyList<Int>()
        list.add(10)
        list.add(20)
        list.add(30)
        list.add(20)
        val result = list.removeEl(20)
        assertTrue(result)
        assertEquals(3, list.size())
        assertEquals("[10, 30, 20]", list.toString())
    }

    @Test
    fun `removeEl удаляет элемент в голове списка`() {
        val list = MyList<Int>()
        list.add(1)
        list.add(2)
        list.add(3)
        val result = list.removeEl(1)
        assertTrue(result)
        assertEquals("[2, 3]", list.toString())
    }

    @Test
    fun `removeEl удаляет элемент в конце списка`() {
        val list = MyList<Int>()
        list.add(1)
        list.add(2)
        list.add(3)
        val result = list.removeEl(3)
        assertTrue(result)
        assertEquals("[1, 2]", list.toString())
    }

    @Test
    fun `removeEl возвращает false если элемент не найден`() {
        val list = MyList<Int>()
        list.add(1)
        list.add(2)
        val result = list.removeEl(99)
        assertFalse(result)
        assertEquals(2, list.size())
        assertEquals("[1, 2]", list.toString())
    }

    @Test
    fun `removeEl из пустого списка возвращает false`() {
        val list = MyList<Int>()
        val result = list.removeEl(42)
        assertFalse(result)
        assertEquals(0, list.size())
    }

    @Test
    fun `removeEl не удаляет все вхождения только первое`() {
        val list = MyList<String>()
        list.add("A")
        list.add("B")
        list.add("B")
        list.add("C")
        list.removeEl("B")
        assertEquals("[A, B, C]", list.toString())
    }

    // ===== Тесты для getIdEl ======
    @Test
    fun `getIdEl возвращает элемент`() {
        val list = MyList<String>()
        list.add("A")
        list.add("B")
        list.add("B")
        list.add("C")
        assertEquals("B", list.getIdEl(2))
    }

    @Test
    fun `getIdEl возвращает ошибку`() {
        val list = MyList<String>()
        list.add("A")
        list.add("B")
        list.add("B")
        list.add("C")
        assertEquals("B", list.getIdEl(4))
    }

    // ===== Тесты для toString =====

    @Test
    fun `toString пустого списка возвращает квадратные скобки`() {
        val list = MyList<Int>()
        assertEquals("[]", list.toString())
    }

    @Test
    fun `toString списка с одним элементом`() {
        val list = MyList<String>()
        list.add("Hello")
        assertEquals("[Hello]", list.toString())
    }

    @Test
    fun `toString сохраняет порядок элементов`() {
        val list = MyList<Int>()
        list.add(5)
        list.add(10)
        list.add(15)
        assertEquals("[5, 10, 15]", list.toString())
    }

    // ===== Интеграционные тесты =====

    @Test
    fun `последовательность операций добавления и удаления`() {
        val list = MyList<Int>()
        list.add(1)
        list.push(0)
        list.add(2)
        assertEquals("[0, 1, 2]", list.toString())

        list.removeEl(1)
        assertEquals("[0, 2]", list.toString())

        list.removeAt(0)
        assertEquals("[2]", list.toString())

        list.pop()
        assertEquals("[]", list.toString())
    }

    @Test
    fun `работа со списком строк`() {
        val list = MyList<String>()
        list.add("первый")
        list.add("второй")
        list.push("нулевой")
        assertEquals("[нулевой, первый, второй]", list.toString())

        list.removeEl("первый")
        assertEquals("[нулевой, второй]", list.toString())
    }
}