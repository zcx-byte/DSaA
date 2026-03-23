package com.dsa.algorithms

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File

class DsaALibrTest {

    // ================= measureExecutionTime =================

    /** Тест: время всегда положительное. */
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

    /** Тест: операция действительно вызывается столько раз, сколько задано. */
    @Test
    fun `measureExecutionTime calls operation correct number of times`() {
        var callCount = 0
        measureExecutionTime(
            iterations = 5,
            label = "counter"
        ) {
            callCount++
        }
        assertEquals(5, callCount, "Операция должна вызваться ровно 5 раз")
    }

    // ================= generateRandomArray =================

    /** Тест: массив нужного размера. */
    @Test
    fun `generateRandomArray creates array of specified size`() {
        val array = generateRandomArray(size = 10, min = 0L, max = 100L)
        assertEquals(10, array.size, "Размер массива должен быть 10")
    }

    /** Тест: значения лежат в диапазоне [min, max). */
    @Test
    fun `generateRandomArray values are within range`() {
        val array = generateRandomArray(size = 50, min = 10L, max = 20L)
        for (value in array) {
            assertTrue(value in 10L until 20L, "Значение $value вне диапазона [10, 20)")
        }
    }

    /** Тест: одинаковый seed -> одинаковый массив. */
    @Test
    fun `generateRandomArray with same seed produces same result`() {
        val seed = 12345L
        val array1 = generateRandomArray(size = 10, min = 0L, max = 100L, seed = seed)
        val array2 = generateRandomArray(size = 10, min = 0L, max = 100L, seed = seed)
        assertArrayEquals(array1, array2, "Массивы с одинаковым seed должны совпадать")
    }

    /** Тест: отрицательный размер -> ошибка. */
    @Test
    fun `generateRandomArray throws exception for negative size`() {
        assertThrows<IllegalArgumentException> {
            generateRandomArray(size = -1)
        }
    }

    // ================= fillArrayWithRandom =================

    /** Тест: fillArrayWithRandom меняет элементы массива. */
    @Test
    fun `fillArrayWithRandom modifies existing array`() {
        val array = longArrayOf(0L, 0L, 0L, 0L, 0L)
        fillArrayWithRandom(array, min = 50L, max = 100L)
        assertTrue(array.any { it != 0L }, "Массив должен измениться")
    }

    /** Тест: значения лежат в [min, max). */
    @Test
    fun `fillArrayWithRandom values are within range`() {
        val array = longArrayOf(0L, 0L, 0L)
        fillArrayWithRandom(array, min = 10L, max = 15L)
        for (value in array) {
            assertTrue(value in 10L until 15L, "Значение $value вне диапазона [10, 15)")
        }
    }

    // ================= saveArrayToFile & loadArrayFromFile =================

    /** Тест: save и load работают корректно. */
    @Test
    fun `saveArrayToFile and loadArrayFromFile work correctly`() {
        val testFile = File("test_temp_array.txt")
        val originalArray = longArrayOf(1L, 2L, 3L, 4L, 5L)

        val saved = saveArrayToFile(originalArray, testFile.path)
        assertTrue(saved, "Файл должен сохраниться успешно")

        val loadedArray = loadArrayFromFile(testFile.path)
        assertArrayEquals(originalArray, loadedArray, "Загруженный массив должен совпадать с оригиналом")

        testFile.delete()
    }

    /** Тест: для несуществующего файла вернётся пустой массив. */
    @Test
    fun `loadArrayFromFile returns empty array for missing file`() {
        val loadedArray = loadArrayFromFile("nonexistent_file_12345.txt")
        assertEquals(0, loadedArray.size, "Для несуществующего файла должен вернуться пустой массив")
    }

    /** Тест: saveArrayToFile с кастомным разделителем. */
    @Test
    fun `saveArrayToFile with custom delimiter`() {
        val testFile = File("test_delim.txt")
        val array = longArrayOf(10L, 20L, 30L)

        saveArrayToFile(array, testFile.path, delimiter = ",")
        val content = testFile.readText()

        assertEquals("10,20,30", content.trim(), "Разделитель должен сохраниться")
        testFile.delete()
    }

    // ================= sortArrayAscending (generic) =================

    /** Тест: sortArrayAscending возвращает отсортированную копию для Long. */
    @Test
    fun `sortArrayAscending returns sorted copy for Long`() {
        val original = arrayOf(5L, 2L, 8L, 1L, 9L)
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        val sorted = sortArrayAscending(original, comparator)

        assertArrayEquals(arrayOf(1L, 2L, 5L, 8L, 9L), sorted)
        assertArrayEquals(arrayOf(5L, 2L, 8L, 1L, 9L), original, "Оригинальный массив не должен измениться")
    }

    /** Тест: уже отсортированный массив остаётся тем же. */
    @Test
    fun `sortArrayAscending works with already sorted array`() {
        val original = arrayOf(1L, 2L, 3L)
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        val sorted = sortArrayAscending(original, comparator)
        assertArrayEquals(arrayOf(1L, 2L, 3L), sorted)
    }

    /** Тест: sortArrayAscending работает с String. */
    @Test
    fun `sortArrayAscending works with String`() {
        val original = arrayOf("banana", "apple", "cherry")
        val comparator = Comparator<String> { a, b -> a.compareTo(b) }
        val sorted = sortArrayAscending(original, comparator)
        assertArrayEquals(arrayOf("apple", "banana", "cherry"), sorted)
    }

    // ================= sortArrayInPlace (generic) =================

    /** Тест: sortArrayInPlace меняет исходный массив. */
    @Test
    fun `sortArrayInPlace modifies original array`() {
        val array = arrayOf(5L, 2L, 8L, 1L, 9L)
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        sortArrayInPlace(array, comparator)
        assertArrayEquals(arrayOf(1L, 2L, 5L, 8L, 9L), array)
    }

    /** Тест: sortArrayInPlace возвращает ту же ссылку. */
    @Test
    fun `sortArrayInPlace returns same reference`() {
        val array = arrayOf(3L, 1L, 2L)
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        val result = sortArrayInPlace(array, comparator)
        assertSame(array, result, "Должен вернуть тот же объект массива")
    }

    // ================= isArraySortedAscending (generic) =================

    /** Тест: отсортированный массив -> true. */
    @Test
    fun `isArraySortedAscending returns true for sorted array`() {
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        assertTrue(isArraySortedAscending(arrayOf(1L, 2L, 3L, 4L, 5L), comparator))
    }

    /** Тест: массив НЕ отсортирован в начале -> false. */
    @Test
    fun `isArraySortedAscending returns false when unsorted at start`() {
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        // первые элементы перевёрнуты: 2, 1, 3, 4, 5
        assertFalse(isArraySortedAscending(arrayOf(2L, 1L, 3L, 4L, 5L), comparator))
    }

    /** Тест: массив НЕ отсортирован в конце -> false. */
    @Test
    fun `isArraySortedAscending returns false when unsorted at end`() {
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        // последние элементы вперемешку: 1, 2, 3, 5, 4
        assertFalse(isArraySortedAscending(arrayOf(1L, 2L, 3L, 5L, 4L), comparator))
    }

    /** Тест: неотсортированный массив -> false. */
    @Test
    fun `isArraySortedAscending returns false for unsorted array`() {
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        assertFalse(isArraySortedAscending(arrayOf(1L, 5L, 3L, 4L, 2L), comparator))
    }

    /** Тест: пустой массив -> true. */
    @Test
    fun `isArraySortedAscending returns true for empty array`() {
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        assertTrue(isArraySortedAscending(arrayOf(), comparator))
    }

    /** Тест: массив из одного элемента -> true. */
    @Test
    fun `isArraySortedAscending returns true for single element`() {
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        assertTrue(isArraySortedAscending(arrayOf(42L), comparator))
    }

    /** Тест: массив с дубликатами -> считается отсортированным (неубывание). */
    @Test
    fun `isArraySortedAscending handles duplicates`() {
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        assertTrue(
            isArraySortedAscending(arrayOf(1L, 2L, 2L, 3L), comparator),
            "Массив с дубликатами должен считаться отсортированным (в неубывающем порядке)"
        )
    }

    // ================= findLineElement (generic) =================

    /** Тест: линейный поиск находит элемент в неотсортированном массиве. */
    @Test
    fun `findLineElement finds element in unsorted array`() {
        val array = arrayOf(10L, 3L, 7L, 1L, 9L)
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        assertEquals(2, findLineElement(array, 7L, comparator))
        assertEquals(0, findLineElement(array, 10L, comparator))
    }

    /** Тест: если элемент не найден -> возвращает -1. */
    @Test
    fun `findLineElement returns -1 for missing element`() {
        val array = arrayOf(1L, 2L, 3L, 4L, 5L)
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        assertEquals(-1, findLineElement(array, 10L, comparator))
    }

    /** Тест: пустой массив -> не найдено. */
    @Test
    fun `findLineElement works with empty array`() {
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        assertEquals(-1, findLineElement(arrayOf(), 1L, comparator))
    }

    /** Тест: функция возвращает первое вхождение для дубликатов. */
    @Test
    fun `findLineElement returns first occurrence for duplicates`() {
        val array = arrayOf(5L, 3L, 5L, 7L, 5L)
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        assertEquals(0, findLineElement(array, 5L, comparator))
    }

    // ================= binarySearch (generic) =================

    /** Тест: бинарный поиск находит элемент в отсортированном массиве. */
    @Test
    fun `binarySearch finds element in sorted array`() {
        val array = arrayOf(1L, 3L, 5L, 7L, 9L, 11L, 13L)
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        assertEquals(3, binarySearch(array, 7L, comparator))
        assertEquals(0, binarySearch(array, 1L, comparator))
        assertEquals(6, binarySearch(array, 13L, comparator))
    }

    /** Тест: элемент не найден -> возвращает -1. */
    @Test
    fun `binarySearch returns -1 for missing element`() {
        val array = arrayOf(1L, 3L, 5L, 7L, 9L)
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        assertEquals(-1, binarySearch(array, 4L, comparator))
        assertEquals(-1, binarySearch(array, 10L, comparator))
    }

    /** Тест: пустой массив -> не найдено. */
    @Test
    fun `binarySearch works with empty array`() {
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        assertEquals(-1, binarySearch(arrayOf(), 1L, comparator))
    }

    /** Тест: бинарный поиск корректно работает с дубликатами. */
    @Test
    fun `binarySearch handles duplicates`() {
        val array = arrayOf(1L, 5L, 5L, 5L, 9L)
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        val index = binarySearch(array, 5L, comparator)
        assertTrue(index in 1..3, "Индекс найденного элемента 5 должен быть в диапазоне 1-3")
    }

    /** Тест: бинарный поиск работает и со String. */
    @Test
    fun `binarySearch works with String`() {
        val array = arrayOf("apple", "banana", "cherry", "date")
        val comparator = Comparator<String> { a, b -> a.compareTo(b) }
        assertEquals(2, binarySearch(array, "cherry", comparator))
        assertEquals(-1, binarySearch(array, "fig", comparator))
    }

    // ================= interpolationSearch (generic) =================

    /** Тест: интерполяционный поиск находит элемент в равномерном массиве. */
    @Test
    fun `interpolationSearch finds element in uniform array`() {
        val array = arrayOf(10L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L)
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        val toDouble: (Long) -> Long = { it }

        assertEquals(8, interpolationSearch(array, 90L, comparator, toDouble))
        assertEquals(0, interpolationSearch(array, 10L, comparator, toDouble))
        assertEquals(9, interpolationSearch(array, 100L, comparator, toDouble))
    }

    /** Тест: элемент не найден -> возвращает -1. */
    @Test
    fun `interpolationSearch returns -1 for missing element`() {
        val array = arrayOf(10L, 20L, 30L, 40L, 50L)
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        val toDouble: (Long) -> Long = { it }

        assertEquals(-1, interpolationSearch(array, 25L, comparator, toDouble))
        assertEquals(-1, interpolationSearch(array, 5L, comparator, toDouble))
    }

    /** Тест: работает и с неравномерным массивом (но эффективность падает). */
    @Test
    fun `interpolationSearch works with non-uniform array`() {
        val array = arrayOf(1L, 2L, 5L, 10L, 20L, 50L, 100L, 200L, 500L, 1000L)
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        val toDouble: (Long) -> Long = { it }

        val index = interpolationSearch(array, 100L, comparator, toDouble)
        assertEquals(6, index)
    }

    /** Тест: корректно обрабатывает границы. */
    @Test
    fun `interpolationSearch handles edge values`() {
        val array = arrayOf(5L, 10L, 15L, 20L, 25L)
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        val toDouble: (Long) -> Long = { it }

        assertEquals(0, interpolationSearch(array, 5L, comparator, toDouble))
        assertEquals(4, interpolationSearch(array, 25L, comparator, toDouble))
    }
}

