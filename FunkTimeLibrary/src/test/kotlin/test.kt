package com.timeutils.utils

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.io.File

class TimeUtilsTest {

    // ================= measureExecutionTime =================

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

    @Test
    fun `generateRandomArray creates array of specified size`() {
        val array = generateRandomArray(size = 10, min = 0, max = 100)

        assertEquals(10, array.size, "Размер массива должен быть 10")
    }

    @Test
    fun `generateRandomArray values are within range`() {
        val array = generateRandomArray(size = 50, min = 10, max = 20)

        for (value in array) {
            assertTrue(value >= 10 && value < 20, "Значение $value вне диапазона [10, 20)")
        }
    }

    @Test
    fun `generateRandomArray with same seed produces same result`() {
        val seed = 12345L

        val array1 = generateRandomArray(size = 10, min = 0, max = 100, seed = seed)
        val array2 = generateRandomArray(size = 10, min = 0, max = 100, seed = seed)

        assertArrayEquals(array1, array2, "Массивы с одинаковым seed должны совпадать")
    }

    @Test
    fun `generateRandomArray throws exception for negative size`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            generateRandomArray(size = -1)
        }

        assertTrue(exception.message?.contains("отрицательным") == true)
    }

    // ================= fillArrayWithRandom =================

    @Test
    fun `fillArrayWithRandom modifies existing array`() {
        val array = intArrayOf(0, 0, 0, 0, 0)

        fillArrayWithRandom(array, min = 50, max = 100)

        // Проверяем, что массив изменился (хотя бы один элемент не 0)
        var changed = false
        for (value in array) {
            if (value != 0) {
                changed = true
                break
            }
        }
        assertTrue(changed, "Массив должен измениться")
    }

    // ================= saveArrayToFile & loadArrayFromFile =================

    @Test
    fun `saveArrayToFile and loadArrayFromFile work correctly`() {
        val testFile = File("test_temp_array.txt")
        val originalArray = intArrayOf(1, 2, 3, 4, 5)

        // Сохраняем
        val saved = saveArrayToFile(originalArray, testFile.path)
        assertTrue(saved, "Файл должен сохраниться успешно")

        // Загружаем
        val loadedArray = loadArrayFromFile(testFile.path)

        // Проверяем равенство массивов
        assertArrayEquals(originalArray, loadedArray, "Загруженный массив должен совпадать с оригиналом")

        // Удаляем тестовый файл
        testFile.delete()
    }

    @Test
    fun `loadArrayFromFile returns empty array for missing file`() {
        val loadedArray = loadArrayFromFile("nonexistent_file.txt")

        assertEquals(0, loadedArray.size, "Для несуществующего файла должен вернуться пустой массив")
    }

    // ================= sortArrayAscending =================

    @Test
    fun `sortArrayAscending returns sorted copy`() {
        val original = intArrayOf(5, 2, 8, 1, 9)

        val sorted = sortArrayAscending(original)

        assertArrayEquals(intArrayOf(1, 2, 5, 8, 9), sorted, "Массив должен быть отсортирован")
        assertArrayEquals(intArrayOf(5, 2, 8, 1, 9), original, "Оригинальный массив не должен измениться")
    }

    // ================= sortArrayInPlace =================

    @Test
    fun `sortArrayInPlace modifies original array`() {
        val array = intArrayOf(5, 2, 8, 1, 9)

        sortArrayInPlace(array)

        assertArrayEquals(intArrayOf(1, 2, 5, 8, 9), array, "Массив должен быть отсортирован")
    }

    // ================= isArraySortedAscending =================

    @Test
    fun `isArraySortedAscending returns true for sorted array`() {
        val sortedArray = intArrayOf(1, 2, 3, 4, 5)

        assertTrue(isArraySortedAscending(sortedArray), "Отсортированный массив должен вернуть true")
    }

    @Test
    fun `isArraySortedAscending returns false for unsorted array`() {
        val unsortedArray = intArrayOf(1, 5, 3, 4, 2)

        assertFalse(isArraySortedAscending(unsortedArray), "Несортированный массив должен вернуть false")
    }

    @Test
    fun `isArraySortedAscending returns true for empty array`() {
        val emptyArray = intArrayOf()

        assertTrue(isArraySortedAscending(emptyArray), "Пустой массив считается отсортированным")
    }
}