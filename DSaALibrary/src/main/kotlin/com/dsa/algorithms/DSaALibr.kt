// Автор: Прасков Д.Е. Студент 2-го курса ЗабГУ ИВТ-24

package com.dsa.algorithms

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
    return (1..iterations).sumOf {
        val start = System.nanoTime()
        operation()
        System.nanoTime() - start
    }
}

// ================= ЧИСЛОВЫЕ ФУНКЦИИ (LongArray) =================
// Эти функции требуют арифметических операций или специфичного парсинга,
// поэтому оставлены для типа LongArray.

/**
 * Заполняет массив случайными числами в заданном диапазоне.
 *
 * @param size размер массива
 * @param min минимальное значение (включительно)
 * @param max максимальное значение (исключительно)
 * @param seed опциональное зерно для генератора (для воспроизводимости)
 * @return новый массив LongArray со случайными значениями
 */
fun generateRandomArray(
    size: Int,
    min: Long = 0,
    max: Long = 1000,
    seed: Long? = null
): LongArray {
    require(size >= 0) { "Размер массива не может быть отрицательным" }
    require(min < max) { "min должен быть меньше max" }

    val random = seed?.let { Random(it) } ?: Random
    return LongArray(size) { random.nextLong(min, max) }
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
    array: LongArray,
    min: Long = 0,
    max: Long = 1000,
    seed: Long? = null
): LongArray {
    require(min < max) { "min должен быть меньше max" }
    val random = seed?.let { Random(it) } ?: Random
    for (i in array.indices) {
        array[i] = random.nextLong(min, max)
    }
    return array
}

/**
 * Сохраняет массив целых чисел типа Long в текстовый файл.
 *
 * @param array массив для сохранения
 * @param filePath путь к файлу
 * @param delimiter разделитель (по умолчанию пробел)
 * @param append false — перезапись, true — дозапись
 * @return true если успешно, false при ошибке
 */
fun saveArrayToFile(
    array: LongArray,
    filePath: String,
    delimiter: String = " ",
    append: Boolean = false
): Boolean {
    return try {
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
 * Загружает массив целых чисел типа Long из текстового файла.
 *
 * @param filePath путь к файлу
 * @param delimiter разделитель (должен совпадать с тем, что при сохранении)
 * @return массив с данными типа LongArray или пустой массив при ошибке/отсутствии данных
 */
fun loadArrayFromFile(
    filePath: String,
    delimiter: String = " "
): LongArray {
    return try {
        val content = File(filePath).readText(Charsets.UTF_8).trim()
        if (content.isEmpty()) return LongArray(0)
        content.split(delimiter)
            .filter { it.isNotBlank() }
            .map { it.toLong() }
            .toLongArray()
    } catch (e: Exception) {
        LongArray(0)
    }
}

/**
 * Возвращает отсортированную копию массива (по возрастанию согласно компаратору).
 * Исходный массив не изменяется.
 *
 * @param T тип элементов, поддерживающий сравнение через компаратор
 * @param array исходный массив
 * @param comparator объект, определяющий порядок сравнения элементов
 * @return новая отсортированная копия массива
 */
fun <T> sortArrayAscending(array: Array<T>, comparator: Comparator<T>): Array<T> {
    return array.copyOf().apply { sortWith(comparator) }
}

/**
 * Сортирует массив по возрастанию на месте согласно компаратору.
 *
 * ## Сложность:
 * - Время: **O(n^2)** — реализован алгоритм пузырьковой сортировки (для учебных целей)
 * - Память: **O(1)** — сортировка на месте без выделения дополнительной памяти
 *
 * @param T тип элементов, поддерживающий сравнение через компаратор
 * @param array массив для сортировки
 * @param comparator объект, определяющий порядок сравнения элементов
 * @return тот же массив, отсортированный по возрастанию
 */
fun <T> sortArrayInPlace(array: Array<T>, comparator: Comparator<T>): Array<T> {
    val n = array.size
    for (i in 0 until n - 1) {
        for (j in 0 until n - 1 - i) {
            if (comparator.compare(array[j], array[j + 1]) > 0) {
                val temp = array[j]
                array[j] = array[j + 1]
                array[j + 1] = temp
            }
        }
    }
    return array
}

/**
 * Проверяет, отсортирован ли массив по возрастанию согласно компаратору.
 *
 * ## Сложность:
 * - Время: **O(n)** — в худшем случае проверяются все соседние пары
 * - Память: **O(1)** — константная дополнительная память
 *
 * @param T тип элементов, поддерживающий сравнение через компаратор
 * @param array массив для проверки
 * @param comparator объект, определяющий порядок сравнения элементов
 * @return true если массив отсортирован по неубыванию
 */
fun <T> isArraySortedAscending(array: Array<T>, comparator: Comparator<T>): Boolean {
    for (i in 0 until array.lastIndex) {
        if (comparator.compare(array[i], array[i + 1]) > 0) return false
    }
    return true
}

/**
 * Линейный поиск элемента в массиве с использованием компаратора.
 * Последовательно проверяет каждый элемент до нахождения совпадения.
 *
 * ## Сложность:
 * - Время:
 *   - Лучший случай: **O(1)** — искомый элемент на первой позиции
 *   - Средний случай: **O(n)** — элемент находится примерно в середине
 *   - Худший случай: **O(n)** — элемент в конце массива или отсутствует
 * - Память: **O(1)** — используется константное количество дополнительной памяти
 *
 * Использовать, когда: массив не отсортирован, очень маленький (< 20 элементов) или поиск однократный.
 * Не использовать, когда: массив большой и отсортирован (лучше [binarySearch]).
 *
 * @param T тип элементов, поддерживающий сравнение через компаратор
 * @param array массив для поиска (может быть неотсортированным)
 * @param target искомое значение
 * @param comparator объект, определяющий порядок сравнения элементов
 * @return индекс элемента или -1 если не найден
 */
fun <T> findLineElement(array: Array<T>, target: T, comparator: Comparator<T>): Long {
    for (i in array.indices) {

        if (comparator.compare(array[i], target) == 0) {

            return i.toLong()
        }
    }
    return -1
}

/**
 * Бинарный поиск элемента в отсортированном массиве с использованием компаратора.
 * На каждом шаге отбрасывает половину диапазона, обеспечивая логарифмическую сложность.
 *
 * ## Сложность:
 * - Время:
 *   - Лучший случай: **O(1)** — искомый элемент сразу в середине
 *   - Средний случай: **O(log n)**
 *   - Худший случай: **O(log n)** — требуется максимальное число делений пополам
 * - Память: **O(1)** — итеративная реализация без рекурсии
 *
 * Использовать, когда: массив отсортирован согласно компаратору, распределение данных неизвестно.
 * Не использовать, когда: массив не отсортирован или данные равномерно распределены (лучше [interpolationSearch] для чисел).
 *
 * @param T тип элементов, поддерживающий сравнение через компаратор
 * @param array отсортированный по возрастанию (согласно [comparator]) массив
 * @param target искомое значение
 * @param comparator объект, определяющий порядок сравнения элементов
 * @return индекс элемента или -1 если не найден
 */
fun <T> binarySearch(
    array: Array<T>,
    target: T,
    comparator: Comparator<T>
): Long {
    var left = 0
    var right = array.size - 1

    while (left <= right) {
        val mid = left + (right - left) / 2
        val cmp = comparator.compare(array[mid], target)

        when {
            cmp == 0 -> return mid.toLong()
            cmp < 0 -> left = mid + 1
            else -> right = mid - 1
        }
    }
    return -1
}

/**
 * Интерполяционный поиск элемента в отсортированном массиве (обобщённая версия).
 * Оценивает позицию искомого элемента на основе его числового значения относительно границ диапазона.
 *
 * ## Сложность:
 * - Время:
 *   - Лучший случай: **O(1)** — угадали позицию с первой попытки
 *   - Средний случай: **O(log log n)** — *только при равномерном распределении данных*
 *   - Худший случай: **O(n)** — при неравномерном распределении
 * - Память: **O(1)** — константная дополнительная память
 *
 * ## Важные условия:
 * - Массив **должен быть отсортирован** согласно [comparator]
 * - Функция [toDouble] должна возвращать значения, сохраняющие порядок элементов
 * - Данные должны быть **равномерно распределёнными** для эффективности
 *
 * @param T тип элементов массива
 * @param array отсортированный массив
 * @param target искомое значение
 * @param comparator компаратор для сравнения элементов
 * @param toLong функция преобразования элемента в Long для расчёта интерполяции
 * @return индекс элемента или -1 если не найден
 */
fun <T> interpolationSearch(
    array: Array<T>,
    target: T,
    comparator: Comparator<T>,
    toLong: (T) -> Long
): Long {
    var left = 0
    var right = array.size - 1

    while (left <= right) {
        val targetVal = toLong(target)

        // значения
        val leftVal = toLong(array[left])
        val rightVal = toLong(array[right])

        // Защита от деления на ноль (все элементы в диапазоне одинаковы)
        if (rightVal == leftVal) {
            if (comparator.compare(array[left], target) == 0) return left.toLong()
            break
        }

        // Формула интерполяции
        val pos = left + ((targetVal - leftVal) * (right - left) / (rightVal - leftVal)).toInt()

        // Проверка границ (на случай выхода зца пределы из-за округления)
        if (pos < left || pos > right) break

        when {
            comparator.compare(array[pos], target) == 0 -> return pos.toLong()
            comparator.compare(array[pos], target) < 0 -> left = pos + 1
            else -> right = pos - 1
        }
    }

    return -1
}

/**
 * Линейный поиск первого элемента, удовлетворяющего условию (предикату).
 *
 * Последовательно проверяет каждый элемент массива, передавая его в функцию [predicate].
 * Возвращает первый элемент, для которого предикат вернул `true`.
 *
 * ## Сложность:
 * - Время:
 *   - Лучший случай: **O(1)** — первый элемент удовлетворяет условию
 *   - Средний случай: **O(n)** — в зависимости от распределения подходящих элементов
 *   - Худший случай: **O(n)** — ни один элемент не подходит или подходящий последний
 * - Память: **O(1)** — не использует дополнительную память, кроме хранения замыкания
 *
 * ## Когда использовать:
 * - когда нужен поиск по **сложному условию** (не просто сравнение с значением)
 * - когда массив **не отсортирован**
 * - когда нужно найти **первый подходящий** элемент
 * - Не стоит для простого поиска по значению — используйте [binarySearch] или [findLineElement]
 *
 * @param T тип элементов массива
 * @param array массив для поиска
 * @param predicate функция-условие, принимающая элемент и возвращающая Boolean
 * @return первый элемент, удовлетворяющий условию, или `null` если ничего не найдено
 *
 * @sample
 * val numbers = arrayOf("1", "5", "12", "8", "20")
 * val firstEven = findByPredicate(numbers) { it.toInt() % 2 == 0 }  // "12"
 */
fun <T> findByPredicate(array: Array<T>, predicate: (T) -> Boolean): T? {
    for (element in array) {
        if (predicate(element)) {
            return element
        }
    }
    return null
}