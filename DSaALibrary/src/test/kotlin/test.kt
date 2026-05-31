package com.dsa.algorithms

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File

class DsaALibrTest {

    // ================= measureExecutionTime =================

    /** Тест: время всегда положительное. */
    @Test
    fun `measureExecutionTime возвращает положительное значение`() {
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
    fun `measureExecutionTime вызывает операцию нужное количество раз`() {
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
    fun `generateRandomArray создаёт массив заданного размера`() {
        val array = generateRandomArray(size = 10, min = 0L, max = 100L)
        assertEquals(10, array.size, "Размер массива должен быть 10")
    }

    /** Тест: значения лежат в диапазоне [min, max). */
    @Test
    fun `generateRandomArray значения находятся в диапазоне`() {
        val array = generateRandomArray(size = 50, min = 10L, max = 20L)
        for (value in array) {
            assertTrue(value in 10L until 20L, "Значение $value вне диапазона [10, 20)")
        }
    }

    /** Тест: одинаковый seed -> одинаковый массив. */
    @Test
    fun `generateRandomArray с одинаковым seed даёт одинаковый результат`() {
        val seed = 12345L
        val array1 = generateRandomArray(size = 10, min = 0L, max = 100L, seed = seed)
        val array2 = generateRandomArray(size = 10, min = 0L, max = 100L, seed = seed)
        assertArrayEquals(array1, array2, "Массивы с одинаковым seed должны совпадать")
    }

    /** Тест: отрицательный размер -> ошибка. */
    @Test
    fun `generateRandomArray выбрасывает исключение для отрицательного размера`() {
        assertThrows<IllegalArgumentException> {
            generateRandomArray(size = -1)
        }
    }

    // ================= fillArrayWithRandom =================

    /** Тест: fillArrayWithRandom меняет элементы массива. */
    @Test
    fun `fillArrayWithRandom изменяет существующий массив`() {
        val array = longArrayOf(0L, 0L, 0L, 0L, 0L)
        fillArrayWithRandom(array, min = 50L, max = 100L)
        assertTrue(array.any { it != 0L }, "Массив должен измениться")
    }

    /** Тест: значения лежат в [min, max). */
    @Test
    fun `fillArrayWithRandom значения находятся в диапазоне`() {
        val array = longArrayOf(0L, 0L, 0L)
        fillArrayWithRandom(array, min = 10L, max = 15L)
        for (value in array) {
            assertTrue(value in 10L until 15L, "Значение $value вне диапазона [10, 15)")
        }
    }

    // ================= saveArrayToFile & loadArrayFromFile =================

    /** Тест: save и load работают корректно. */
    @Test
    fun `saveArrayToFile и loadArrayFromFile работают корректно`() {
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
    fun `loadArrayFromFile возвращает пустой массив для отсутствующего файла`() {
        val loadedArray = loadArrayFromFile("nonexistent_file_12345.txt")
        assertEquals(0, loadedArray.size, "Для несуществующего файла должен вернуться пустой массив")
    }

    /** Тест: saveArrayToFile с кастомным разделителем. */
    @Test
    fun `saveArrayToFile с пользовательским разделителем`() {
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
    fun `sortArrayAscending возвращает отсортированную копию для Long`() {
        val original = arrayOf(5L, 2L, 8L, 1L, 9L)
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        val sorted = sortArrayAscending(original, comparator)

        assertArrayEquals(arrayOf(1L, 2L, 5L, 8L, 9L), sorted)
        assertArrayEquals(arrayOf(5L, 2L, 8L, 1L, 9L), original, "Оригинальный массив не должен измениться")
    }

    /** Тест: уже отсортированный массив остаётся тем же. */
    @Test
    fun `sortArrayAscending работает с уже отсортированным массивом`() {
        val original = arrayOf(1L, 2L, 3L)
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        val sorted = sortArrayAscending(original, comparator)
        assertArrayEquals(arrayOf(1L, 2L, 3L), sorted)
    }

    /** Тест: sortArrayAscending работает с String. */
    @Test
    fun `sortArrayAscending работает с String`() {
        val original = arrayOf("banana", "apple", "cherry")
        val comparator = Comparator<String> { a, b -> a.compareTo(b) }
        val sorted = sortArrayAscending(original, comparator)
        assertArrayEquals(arrayOf("apple", "banana", "cherry"), sorted)
    }

    // ================= BubleSortArrayInPlace (generic) =================

    /** Тест: BubleSortArrayInPlace меняет исходный массив. */
    @Test
    fun `BubleSortArrayInPlace изменяет исходный массив`() {
        val array = arrayOf(5L, 2L, 8L, 1L, 9L)
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        BubleSortArrayInPlace(array, comparator)
        assertArrayEquals(arrayOf(1L, 2L, 5L, 8L, 9L), array)
    }

    /** Тест: BubleSortArrayInPlace сортирует массив, в котором первый элемент - самый большой  */
    @Test
    fun `BubleSortArrayInPlace работает с другим массивом`() {
        val array = arrayOf(10L, 9L, 8L, 7L, 6L, 5L, 3L, 1L)
        val comparator = Comparator<Long> {a, b -> a.compareTo(b) }
        val result = arrayOf(1L, 3L, 5L, 6L, 7L, 8L, 9L, 10L)
        BubleSortArrayInPlace(array, comparator)
        assertArrayEquals(result, array)

    }

    @Test
    fun `BubleSortArrayInPlace c массивом по возрастанию`(){
        val array = arrayOf(1L, 2L, 3L, 4L, 5L, 6L)
        val comparator = Comparator<Long> {a, b -> a.compareTo(b)}
        BubleSortArrayInPlace(array, comparator)
        assertArrayEquals(array, array)

    }

    @Test
    fun `BubleSortArrayInPlace с повторениями`(){
        val array = arrayOf(1L, 2L, 2L, 3L, 4L, 5L, 6L, 3L)
        val comparator = Comparator<Long> {a, b -> a.compareTo(b)}
        val result = arrayOf(1L, 2L, 2L, 3L, 3L, 4L, 5L, 6L)
        BubleSortArrayInPlace(array, comparator)
        assertArrayEquals(array, result)
    }

    @Test
    fun `BubleSortArrayInPlace с отрицательными числами`(){
        val array = arrayOf(1L, 2L, -2L, -3L, 4L, 5L, 6L, 3L)
        val comparator = Comparator<Long> {a, b -> a.compareTo(b)}
        val result = arrayOf(-3L, -2L, 1L, 2L, 3L, 4L, 5L, 6L)
        BubleSortArrayInPlace(array, comparator)
        assertArrayEquals(array, result)
    }

    // ================= quickSort (generic) =================
    @Test
    fun `quickSort работает`(){
        val array = arrayOf(1, 2, 7, 4, 1341)
        val res = arrayOf(1, 2, 4, 7, 1341)

        quickSort(array, 0, array.lastIndex)
        assertArrayEquals(array, res)
    }

    @Test
    fun `quickSort работает с повторениями`(){
        val array = arrayOf(1, 2, 7, 4, 1341, 1, 2)
        val res = arrayOf(1, 1, 2, 2, 4, 7, 1341)

        quickSort(array, 0, array.lastIndex)
        assertArrayEquals(array, res)
    }

    @Test
    fun `quickSort работает c отрицательными числами`(){
        val array = arrayOf(1, 2, 7, 4, 1341, -1, -2)
        val res = arrayOf(-2, -1, 1, 2, 4, 7, 1341)

        quickSort(array, 0, array.lastIndex)
        assertArrayEquals(array, res)
    }

    @Test
    fun `quickSort работает c отсортированным массивом`(){
        val array = arrayOf(1, 2, 4, 7, 1341)
        val res = arrayOf(1, 2, 4, 7, 1341)

        quickSort(array, 0, array.lastIndex)
        assertArrayEquals(array, res)
    }

    // ================= mergeSort (generic) =================
    @Test
    fun `mergeSort работает` (){
        val array = arrayOf(1, 2, 7, 4, 1341)
        val res = arrayOf(1, 2, 4, 7, 1341)
        val sorted = mergeSort(array)
        assertArrayEquals(res, sorted)
    }

    @Test
    fun `mergeSort работает с повторениями`(){
        val array = arrayOf(1, 2, 7, 4, 1341, 1, 2)
        val res = arrayOf(1, 1, 2, 2, 4, 7, 1341)

        val sorted = mergeSort(array)
        assertArrayEquals(res, sorted)
    }

    @Test
    fun `mergeSort работает c отрицательными числами`(){
        val array = arrayOf(1, 2, 7, 4, 1341, -1, -2)
        val res = arrayOf(-2, -1, 1, 2, 4, 7, 1341)

        val sorted = mergeSort(array)
        assertArrayEquals(res, sorted)
    }

    @Test
    fun `mergeSort работает c отсортированным массивом`(){
        val array = arrayOf(1, 2, 4, 7, 1341)
        val res = arrayOf(1, 2, 4, 7, 1341)

        val sorted = mergeSort(array)
        assertArrayEquals(res, sorted)
    }

    @Test
    fun `mergeSort работает c массивом в обратном порядке`(){
        val array = arrayOf(1341, 7, 4, 2, 1)
        val res = arrayOf(1, 2, 4, 7, 1341)

        val sorted = mergeSort(array)
        assertArrayEquals(res, sorted)
    }

    @Test
    fun `mergeSort работает с пустым массивом`(){
        val array = arrayOf<Int>()
        val res = arrayOf<Int>()

        val sorted = mergeSort(array)
        assertArrayEquals(res, sorted)
    }

    @Test
    fun `mergeSort работает с одним элементом`(){
        val array = arrayOf(42)
        val res = arrayOf(42)

        val sorted = mergeSort(array)
        assertArrayEquals(res, sorted)
    }

    // ================= shellSort (generic) =================
    @Test
    fun `shellSort работает`() {
        val actual = arrayOf(64, 34, 25, 12, 22, 11, 90, 25, 5)
        val expected = arrayOf(5, 11, 12, 22, 25, 25, 34, 64, 90)

        shellSort(actual) // сортировка in-place

        assertArrayEquals(expected, actual)
    }

    @Test
    fun `shellSort работает с пустым массивом`(){
        val actual = arrayOf<Int>()
        val res = arrayOf<Int>()
        shellSort(actual)

        assertArrayEquals(res, actual)
    }

    @Test
    fun `shellSort работает c уже отсортированным массивом`() {
        val actual = arrayOf(5, 11, 12, 22, 25, 25, 34, 64, 90)
        val expected = arrayOf(5, 11, 12, 22, 25, 25, 34, 64, 90)

        shellSort(actual) // сортировка in-place

        assertArrayEquals(expected, actual)
    }

    @Test
    fun `shellSort работает с отсортированном в начале массивом`() {
        val actual = arrayOf(5, 11, 12, 22, 22, 90, 25, 5)
        val expected = arrayOf(5, 5, 11, 12, 22, 22, 25, 90)

        shellSort(actual) // сортировка in-place

        assertArrayEquals(expected, actual)
    }

    @Test
    fun `shellSort работает c отрицательными числами`() {
        val actual = arrayOf(64, 34, 25, -12, 22, -11, 90, 25, -5)
        val expected = arrayOf(-12, -11, -5, 22, 25, 25, 34, 64, 90)

        shellSort(actual) // сортировка in-place

        assertArrayEquals(expected, actual)
    }

    // ================= isArraySortedAscending (generic) =================

    /** Тест: отсортированный массив -> true. */
    @Test
    fun `isArraySortedAscending возвращает true для отсортированного массива`() {
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        assertTrue(isArraySortedAscending(arrayOf(1L, 2L, 3L, 4L, 5L), comparator))
    }

    /** Тест: массив НЕ отсортирован в начале -> false. */
    @Test
    fun `isArraySortedAscending возвращает false если массив не отсортирован в начале`() {
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        // первые элементы перевёрнуты: 2, 1, 3, 4, 5
        assertFalse(isArraySortedAscending(arrayOf(2L, 1L, 3L, 4L, 5L), comparator))
    }

    /** Тест: массив НЕ отсортирован в конце -> false. */
    @Test
    fun `isArraySortedAscending возвращает false если массив не отсортирован в конце`() {
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        // последние элементы вперемешку: 1, 2, 3, 5, 4
        assertFalse(isArraySortedAscending(arrayOf(1L, 2L, 3L, 5L, 4L), comparator))
    }

    /** Тест: неотсортированный массив -> false. */
    @Test
    fun `isArraySortedAscending возвращает false для неотсортированного массива`() {
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        assertFalse(isArraySortedAscending(arrayOf(1L, 5L, 3L, 4L, 2L), comparator))
    }

    /** Тест: пустой массив -> true. */
    @Test
    fun `isArraySortedAscending возвращает true для пустого массива`() {
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        assertTrue(isArraySortedAscending(arrayOf(), comparator))
    }

    /** Тест: массив из одного элемента -> true. */
    @Test
    fun `isArraySortedAscending возвращает true для массива из одного элемента`() {
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        assertTrue(isArraySortedAscending(arrayOf(42L), comparator))
    }

    /** Тест: массив с дубликатами -> считается отсортированным (неубывание). */
    @Test
    fun `isArraySortedAscending обрабатывает дубликаты`() {
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        assertTrue(
            isArraySortedAscending(arrayOf(1L, 2L, 2L, 3L), comparator),
            "Массив с дубликатами должен считаться отсортированным (в неубывающем порядке)"
        )
    }

    // ================= findLineElement (generic) =================

    /** Тест: линейный поиск находит элемент в неотсортированном массиве. */
    @Test
    fun `findLineElement находит элемент в неотсортированном массиве`() {
        val array = arrayOf(10L, 3L, 7L, 1L, 9L)
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        assertEquals(2, findLineElement(array, 7L, comparator))
        assertEquals(0, findLineElement(array, 10L, comparator))
    }

    /** Тест: если элемент не найден -> возвращает -1. */
    @Test
    fun `findLineElement возвращает минус один для отсутствующего элемента`() {
        val array = arrayOf(1L, 2L, 3L, 4L, 5L)
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        assertEquals(-1, findLineElement(array, 10L, comparator))
    }

    /** Тест: пустой массив -> не найдено. */
    @Test
    fun `findLineElement работает с пустым массивом`() {
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        assertEquals(-1, findLineElement(arrayOf(), 1L, comparator))
    }

    /** Тест: функция возвращает первое вхождение для дубликатов. */
    @Test
    fun `findLineElement возвращает первое вхождение для дубликатов`() {
        val array = arrayOf(5L, 3L, 5L, 7L, 5L)
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        assertEquals(0, findLineElement(array, 5L, comparator))
    }

    // ================= binarySearch (generic) =================

    /** Тест: бинарный поиск находит элемент в отсортированном массиве. */
    @Test
    fun `binarySearch находит элемент в отсортированном массиве`() {
        val array = arrayOf(1L, 3L, 5L, 7L, 9L, 11L, 13L)

        assertEquals(3, binarySearch(array, 7L))
        assertEquals(0, binarySearch(array, 1L))
        assertEquals(6, binarySearch(array, 13L))
    }

    /** Тест: элемент не найден -> возвращает -1. */
    @Test
    fun `binarySearch возвращает минус один для отсутствующего элемента`() {
        val array = arrayOf(1L, 3L, 5L, 7L, 9L)

        assertEquals(-1, binarySearch(array, 4L))
        assertEquals(-1, binarySearch(array, 10L))
    }

    /** Тест: пустой массив -> не найдено. */
    @Test
    fun `binarySearch работает с пустым массивом`() {

        assertEquals(-1, binarySearch(arrayOf(), 1L))
    }

    /** Тест: бинарный поиск корректно работает с дубликатами. */
    @Test
    fun `binarySearch обрабатывает дубликаты`() {
        val array = arrayOf(1L, 5L, 5L, 5L, 9L)
        val index = binarySearch(array, 5L)

        assertTrue(index in 1..3, "Индекс найденного элемента 5 должен быть в диапазоне 1-3")
    }

    /** Тест: бинарный поиск работает и со String. */
    @Test
    fun `binarySearch работает с String`() {
        val array = arrayOf("apple", "banana", "cherry", "date")

        assertEquals(2, binarySearch(array, "cherry"))
        assertEquals(-1, binarySearch(array, "fig"))
    }

// ================= interpolationSearch (generic) =================

    /** Тест: интерполяционный поиск находит элемент в равномерном массиве. */
    @Test
    fun `interpolationSearch находит элемент в равномерном массиве`() {
        val array = arrayOf(10L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L)
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }

        val toDouble: (Long) -> Double = { it.toDouble() }

        assertEquals(8L, interpolationSearch(array, 90L, comparator, toDouble))
        assertEquals(0L, interpolationSearch(array, 10L, comparator, toDouble))
        assertEquals(9L, interpolationSearch(array, 100L, comparator, toDouble))
    }

    /** Тест: элемент не найден -> возвращает -1. */
    @Test
    fun `interpolationSearch возвращает минус один для отсутствующего элемента`() {
        val array = arrayOf(10L, 20L, 30L, 40L, 50L)
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        val toDouble: (Long) -> Double = { it.toDouble() }

        assertEquals(-1L, interpolationSearch(array, 25L, comparator, toDouble))
        assertEquals(-1L, interpolationSearch(array, 5L, comparator, toDouble))
    }

    /** Тест: работает и с неравномерным массивом (но эффективность падает). */
    @Test
    fun `interpolationSearch работает с неравномерным массивом`() {
        val array = arrayOf(1L, 2L, 5L, 10L, 20L, 50L, 100L, 200L, 500L, 1000L)
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        val toDouble: (Long) -> Double = { it.toDouble() }

        val index = interpolationSearch(array, 100L, comparator, toDouble)
        assertEquals(6L, index)
    }

    /** Тест: корректно обрабатывает границы. */
    @Test
    fun `interpolationSearch обрабатывает граничные значения`() {
        val array = arrayOf(5L, 10L, 15L, 20L, 25L)
        val comparator = Comparator<Long> { a, b -> a.compareTo(b) }
        val toDouble: (Long) -> Double = { it.toDouble() }

        assertEquals(0L, interpolationSearch(array, 5L, comparator, toDouble))
        assertEquals(4L, interpolationSearch(array, 25L, comparator, toDouble))
    }
}