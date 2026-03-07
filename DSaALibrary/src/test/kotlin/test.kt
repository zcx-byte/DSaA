package com.dsa.algorithms

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File

class DsaALibrTest {

    // ================= measureExecutionTime =================

    // measureExecutionTime возвращает положительное значение
    @Test
    fun `measureExecutionTime returns positive value`() {
        val timeNs = measureExecutionTime(
            iterations = 10,
            label = "test"
        ) {
            Thread.sleep(1)
        }
        assertTrue(timeNs > 0, "Время должно быть больше 0")
    }

    // measureExecutionTime вызывает операцию правильное количество раз
    @Test
    fun `measureExecutionTime calls operation correct number of times`() {
        var callCount = 0
        measureExecutionTime(
            iterations = 5,
            label = "counter"
        ) {
            callCount++
        }
        assertEquals(5, callCount, "Функция должна вызваться 5 раз")
    }

    // ================= generateRandomArray =================

    // generateRandomArray создаёт массив указанного размера
    @Test
    fun `generateRandomArray creates array of specified size`() {
        val array = generateRandomArray(size = 10, min = 0, max = 100)
        assertEquals(10, array.size, "Размер массива должен быть 10")
    }

    // generateRandomArray значения находятся в пределах диапазона
    @Test
    fun `generateRandomArray values are within range`() {
        val array = generateRandomArray(size = 50, min = 10, max = 20)
        for (value in array) {
            assertTrue(value in 10 until 20, "Значение $value вне диапазона [10, 20)")
        }
    }

    // generateRandomArray с одинаковым seed даёт одинаковый результат
    @Test
    fun `generateRandomArray with same seed produces same result`() {
        val seed = 12345L
        val array1 = generateRandomArray(size = 10, min = 0, max = 100, seed = seed)
        val array2 = generateRandomArray(size = 10, min = 0, max = 100, seed = seed)
        assertArrayEquals(array1, array2, "Массивы с одинаковым seed должны совпадать")
    }

    // generateRandomArray выбрасывает исключение для отрицательного размера
    @Test
    fun `generateRandomArray throws exception for negative size`() {
        assertThrows<IllegalArgumentException> {
            generateRandomArray(size = -1)
        }
    }

    // ================= fillArrayWithRandom =================

    // fillArrayWithRandom изменяет существующий массив
    @Test
    fun `fillArrayWithRandom modifies existing array`() {
        val array = intArrayOf(0, 0, 0, 0, 0)
        fillArrayWithRandom(array, min = 50, max = 100)
        assertTrue(array.any { it != 0 }, "Массив должен измениться")
    }

    // fillArrayWithRandom значения находятся в пределах диапазона
    @Test
    fun `fillArrayWithRandom values are within range`() {
        val array = intArrayOf(0, 0, 0)
        fillArrayWithRandom(array, min = 10, max = 15)
        for (value in array) {
            assertTrue(value in 10 until 15, "Значение $value вне диапазона")
        }
    }

    // ================= saveArrayToFile & loadArrayFromFile =================

    // saveArrayToFile и loadArrayFromFile работают корректно
    @Test
    fun `saveArrayToFile and loadArrayFromFile work correctly`() {
        val testFile = File("test_temp_array.txt")
        val originalArray = intArrayOf(1, 2, 3, 4, 5)

        val saved = saveArrayToFile(originalArray, testFile.path)
        assertTrue(saved, "Файл должен сохраниться успешно")

        val loadedArray = loadArrayFromFile(testFile.path)
        assertArrayEquals(originalArray, loadedArray, "Загруженный массив должен совпадать с оригиналом")

        testFile.delete()
    }

    // loadArrayFromFile возвращает пустой массив для несуществующего файла
    @Test
    fun `loadArrayFromFile returns empty array for missing file`() {
        val loadedArray = loadArrayFromFile("nonexistent_file_12345.txt")
        assertEquals(0, loadedArray.size, "Для несуществующего файла должен вернуться пустой массив")
    }

    // saveArrayToFile с пользовательским разделителем
    @Test
    fun `saveArrayToFile with custom delimiter`() {
        val testFile = File("test_delim.txt")
        val array = intArrayOf(10, 20, 30)

        saveArrayToFile(array, testFile.path, delimiter = ",")
        val content = testFile.readText()

        assertEquals("10,20,30", content.trim(), "Разделитель должен сохраниться")
        testFile.delete()
    }

    // ================= sortArrayAscending =================

    // sortArrayAscending возвращает отсортированную копию
    @Test
    fun `sortArrayAscending returns sorted copy`() {
        val original = intArrayOf(5, 2, 8, 1, 9)
        val sorted = sortArrayAscending(original)

        assertArrayEquals(intArrayOf(1, 2, 5, 8, 9), sorted)
        assertArrayEquals(intArrayOf(5, 2, 8, 1, 9), original, "Оригинальный массив не должен измениться")
    }

    // sortArrayAscending работает с уже отсортированным массивом
    @Test
    fun `sortArrayAscending works with already sorted array`() {
        val original = intArrayOf(1, 2, 3)
        val sorted = sortArrayAscending(original)
        assertArrayEquals(intArrayOf(1, 2, 3), sorted)
    }

    // ================= sortArrayInPlace =================

    // sortArrayInPlace изменяет исходный массив
    @Test
    fun `sortArrayInPlace modifies original array`() {
        val array = intArrayOf(5, 2, 8, 1, 9)
        sortArrayInPlace(array)
        assertArrayEquals(intArrayOf(1, 2, 5, 8, 9), array)
    }

    // sortArrayInPlace возвращает ту же ссылку
    @Test
    fun `sortArrayInPlace returns same reference`() {
        val array = intArrayOf(3, 1, 2)
        val result = sortArrayInPlace(array)
        assertSame(array, result, "Должен возвращать тот же объект массива")
    }

    // ================= isArraySortedAscending =================

    // isArraySortedAscending возвращает true для отсортированного массива
    @Test
    fun `isArraySortedAscending returns true for sorted array`() {
        assertTrue(isArraySortedAscending(intArrayOf(1, 2, 3, 4, 5)))
    }

    // isArraySortedAscending возвращает false для несортированного массива
    @Test
    fun `isArraySortedAscending returns false for unsorted array`() {
        assertFalse(isArraySortedAscending(intArrayOf(1, 5, 3, 4, 2)))
    }

    // isArraySortedAscending возвращает true для пустого массива
    @Test
    fun `isArraySortedAscending returns true for empty array`() {
        assertTrue(isArraySortedAscending(intArrayOf()))
    }

    // isArraySortedAscending возвращает true для массива с одним элементом
    @Test
    fun `isArraySortedAscending returns true for single element`() {
        assertTrue(isArraySortedAscending(intArrayOf(42)))
    }

    // isArraySortedAscending обрабатывает дубликаты
    @Test
    fun `isArraySortedAscending handles duplicates`() {
        assertTrue(isArraySortedAscending(intArrayOf(1, 2, 2, 3)), "Массив с дубликатами должен считаться отсортированным")
    }

    // ================= findLineElement =================

    // findLineElement находит элемент в несортированном массиве
    @Test
    fun `findLineElement finds element in unsorted array`() {
        val array = intArrayOf(10, 3, 7, 1, 9)
        assertEquals(2, findLineElement(array, 7))
        assertEquals(0, findLineElement(array, 10))
    }

    // findLineElement возвращает -1 для отсутствующего элемента
    @Test
    fun `findLineElement returns -1 for missing element`() {
        val array = intArrayOf(1, 2, 3, 4, 5)
        assertEquals(-1, findLineElement(array, 10))
    }

    // findLineElement работает с пустым массивом
    @Test
    fun `findLineElement works with empty array`() {
        assertEquals(-1, findLineElement(intArrayOf(), 1))
    }

    // findLineElement возвращает первое вхождение для дубликатов
    @Test
    fun `findLineElement returns first occurrence for duplicates`() {
        val array = intArrayOf(5, 3, 5, 7, 5)
        assertEquals(0, findLineElement(array, 5))
    }

    // ================= binarySearch =================

    // binarySearch находит элемент в отсортированном массиве
    @Test
    fun `binarySearch finds element in sorted array`() {
        val array = intArrayOf(1, 3, 5, 7, 9, 11, 13)
        assertEquals(3, binarySearch(array, 7))
        assertEquals(0, binarySearch(array, 1))
        assertEquals(6, binarySearch(array, 13))
    }

    // binarySearch возвращает -1 для отсутствующего элемента
    @Test
    fun `binarySearch returns -1 for missing element`() {
        val array = intArrayOf(1, 3, 5, 7, 9)
        assertEquals(-1, binarySearch(array, 4))
        assertEquals(-1, binarySearch(array, 10))
    }

    // binarySearch работает с пустым массивом
    @Test
    fun `binarySearch works with empty array`() {
        assertEquals(-1, binarySearch(intArrayOf(), 1))
    }

    // binarySearch обрабатывает дубликаты
    @Test
    fun `binarySearch handles duplicates`() {
        val array = intArrayOf(1, 5, 5, 5, 9)
        val index = binarySearch(array, 5)
        assertTrue(index in 1..3, "Индекс найденного элемента 5 должен быть в диапазоне 1-3")
    }

    // ================= interpolationSearch =================

    // interpolationSearch находит элемент в равномерном массиве
    @Test
    fun `interpolationSearch finds element in uniform array`() {
        val array = intArrayOf(10, 20, 30, 40, 50, 60, 70, 80, 90, 100)
        assertEquals(8, interpolationSearch(array, 90))
        assertEquals(0, interpolationSearch(array, 10))
        assertEquals(9, interpolationSearch(array, 100))
    }

    // interpolationSearch возвращает -1 для отсутствующего элемента
    @Test
    fun `interpolationSearch returns -1 for missing element`() {
        val array = intArrayOf(10, 20, 30, 40, 50)
        assertEquals(-1, interpolationSearch(array, 25))
        assertEquals(-1, interpolationSearch(array, 5))
    }

    // interpolationSearch работает с неравномерным массивом
    @Test
    fun `interpolationSearch works with non-uniform array`() {
        val array = intArrayOf(1, 2, 5, 10, 20, 50, 100, 200, 500, 1000)
        val index = interpolationSearch(array, 100)
        assertEquals(6, index)
    }

    // interpolationSearch обрабатывает граничные значения
    @Test
    fun `interpolationSearch handles edge values`() {
        val array = intArrayOf(5, 10, 15, 20, 25)
        assertEquals(0, interpolationSearch(array, 5))
        assertEquals(4, interpolationSearch(array, 25))
    }

    // interpolationSearch с одинаковыми значениями
    @Test
    fun `interpolationSearch with all same values`() {
        val array = intArrayOf(7, 7, 7, 7, 7)
        assertTrue(interpolationSearch(array, 7) in 0..4)
        assertEquals(-1, interpolationSearch(array, 10))
    }

    // interpolationSearch работает с пустым массивом
    @Test
    fun `interpolationSearch works with empty array`() {
        assertEquals(-1, interpolationSearch(intArrayOf(), 1))
    }

    // ================= findByPredicate =================

    // findByPredicate находит первый подходящий элемент в Array<T>
    @Test
    fun `findByPredicate finds first matching element in Array T`() {
        val array = arrayOf("apple", "banana", "cherry", "date")

        val result = findByPredicate(array) { it.startsWith("c") }
        assertEquals("cherry", result)
    }

    // findByPredicate возвращает null, если совпадений нет в Array<T>
    @Test
    fun `findByPredicate returns null when no match in Array T`() {
        val array = arrayOf("cat", "dog", "fox")

        val result = findByPredicate(array) { it.startsWith("z") }
        assertNull(result)
    }

    // findByPredicate работает со сложным условием в Array<T>
    @Test
    fun `findByPredicate works with complex condition in ArrayT`() {
        val array = arrayOf("a", "bb", "ccc", "dddd")

        val result = findByPredicate(array) { it.length == 3 }
        assertEquals("ccc", result)
    }

    // findByPredicate работает с пустым Array<T>
    @Test
    fun `findByPredicate works with empty Array T`() {
        val array = arrayOf<String>()
        assertNull(findByPredicate(array) { true })
    }
}