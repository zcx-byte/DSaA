package com.timeutils.utils

import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import kotlin.random.Random

/**
 * Измеряет суммарное время выполнения [operation] за [iterations] повторов.
 * Возвращает время в наносекундах (для перевода в мс: делите на 1_000_000.0).
 *
 * @param iterations количество повторений замера
 * @param label метка для удобства (не используется в расчётах)
 * @param operation лямбда-функция для измерения
 * @return суммарное время всех итераций в наносекундах
 */
fun <T> measureExecutionTime(
    iterations: Int,
    label: String,
    operation: () -> T
): Long {

    // sumOf проходит по диапазону, выполняет operation и суммирует время каждой итерации
    return (1..iterations).sumOf {
        val start = System.nanoTime()     // замер "до"
        operation()                     // выполнение кода
        System.nanoTime() - start       // замер "после" и вычисление разницы
    }
}

/**
 * Заполняет массив случайными числами в заданном диапазоне.
 *
 * @param size размер массива
 * @param min минимальное значение (включительно)
 * @param max максимальное значение (исключительно)
 * @param seed опциональное зерно для генератора (для воспроизводимости)
 * @return новый массив IntArray со случайными значениями
 */
fun generateRandomArray(
    size: Int,
    min: Int = 0,
    max: Int = 1000,
    seed: Long? = null
): IntArray {
    require(size >= 0) { "Размер массива не может быть отрицательным" }
    require(min < max) { "min должен быть меньше max" }

    // Если seed передан — используем его для воспроизводимости, иначе — случайный
    val random = seed?.let { Random(it) } ?: Random

    // Конструктор IntArray с лямбдой создаёт и заполняет массив за один проход
    return IntArray(size) { random.nextInt(min, max) }
}

/**
 * Заполняет существующий массив случайными числами (изменяет исходный массив).
 *
 * @param array массив для заполнения
 * @param min минимальное значение (включительно)
 * @param max максимальное значение (исключительно)
 * @param seed опциональное зерно для генератора
 * @return тот же массив (для поддержки цепочки вызовов)
 */
fun fillArrayWithRandom(
    array: IntArray,
    min: Int = 0,
    max: Int = 1000,
    seed: Long? = null
): IntArray {
    require(min < max) { "min должен быть меньше max" }

    val random = seed?.let { Random(it) } ?: Random

    // Заполняем массив по индексу
    for (i in array.indices) {
        array[i] = random.nextInt(min, max)
    }
    return array
}

/**
 * Сохраняет массив целых чисел в текстовый файл.
 *
 * @param array массив для сохранения
 * @param filePath путь к файлу
 * @param delimiter разделитель (по умолчанию пробел)
 * @param append false — перезапись, true — дозапись
 * @return true если успешно, false при ошибке
 */
fun saveArrayToFile(
    array: IntArray,
    filePath: String,
    delimiter: String = " ",
    append: Boolean = false
): Boolean {
    return try {
        // FileOutputStream с параметром append поддерживает режим дозаписи
        // OutputStreamWriter с UTF-8 обеспечивает кроссплатформенность
        FileOutputStream(filePath, append).use { fos ->
            OutputStreamWriter(fos, Charsets.UTF_8).use { writer ->
                writer.write(array.joinToString(delimiter))
            }
        }
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * Загружает массив целых чисел из текстового файла.
 *
 * @param filePath путь к файлу
 * @param delimiter разделитель (должен совпадать с тем, что при сохранении)
 * @return массив с данными или пустой массив при ошибке/отсутствии данных
 */
fun loadArrayFromFile(
    filePath: String,
    delimiter: String = " "
): IntArray {
    return try {
        val content = File(filePath).readText(Charsets.UTF_8).trim()

        if (content.isEmpty()) return IntArray(0)

        // Цепочка: разбиение строки -> фильтрация пустот -> парсинг в Int -> массив
        content.split(delimiter)
            .filter { it.isNotBlank() }
            .map { it.toInt() }
            .toIntArray()

    } catch (e: Exception) {
        IntArray(0)
    }
}

/**
 * Возвращает отсортированную копию массива (по возрастанию).
 * Исходный массив не изменяется.
 *
 * @param array исходный массив
 * @return новая отсортированная копия массива
 */
fun sortArrayAscending(array: IntArray): IntArray {
    // copyOf() создаёт копию, apply { sort() } сортирует её на месте и возвращает
    return array.copyOf().apply { sort() }
}

/**
 * Сортирует массив по возрастанию на месте (изменяет исходный массив).
 *
 * @param array массив для сортировки
 * @return тот же массив, отсортированный по возрастанию
 */
fun sortArrayInPlace(array: IntArray): IntArray {
    array.sort()
    return array
}

/**
 * Проверяет, отсортирован ли массив по возрастанию (монотонно).
 *
 * @param array массив для проверки
 * @return true если массив отсортирован по неубыванию
 */
fun isArraySortedAscending(array: IntArray): Boolean {
    // Проверяем каждый элемент со следующим
    // Если хотя бы один элемент больше следующего — массив не отсортирован
    for (i in 0 until array.lastIndex) {
        if (array[i] > array[i + 1]) return false
    }
    return true
}