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

    // имеет функционалльный тип данных (подойдёт, которая возвращает тип Т)
    operation: () -> T

): Long {

    // sumOf проходит по диапазону, выполняет operation и суммирует время каждой итерации
    return (1..iterations).sumOf {
        val start = System.nanoTime()     // замер до
        operation()                     // выполнение кода
        System.nanoTime() - start       // замер после и вычисление разницы
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
    // ?: - или иначе
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
    delimiter: String = " ",    // Значение по умолчанию: если не передать аргумент, будет пробел
    append: Boolean = false     // Значение по умолчанию: если не передать, файл будет перезаписан
): Boolean {

    // Блок try позволяет "поймать" ошибку, если она возникнет при работе с файлом,
    // и предотвратить аварийное завершение программы.
    return try {

        // 1. Создаем поток для записи байтов в файл (FileOutputStream).
        // Параметры: путь к файлу и флаг 'append' (режим дозаписи).
        // .use { ... } — гарантирует, что файл будет автоматически и корректно закрыт
        // сразу после выполнения кода внутри фигурных скобок, даже если случится ошибка.
        // Это защищает от утечки ресурсов.
        FileOutputStream(filePath, append).use { fos ->

            // 2. Оборачиваем байтовый поток в писатель символов (OutputStreamWriter).
            // Это нужно, чтобы записывать не просто байты, а текст (строки).
            // Charsets.UTF_8 — указываем кодировку, чтобы кириллица и спецсимволы
            // отображались корректно на любом устройстве.
            // Вложенный .use гарантирует закрытие и этого ресурса.
            OutputStreamWriter(fos, Charsets.UTF_8).use { writer ->

                // 3. Подготавливаем данные и записываем их в файл.
                // array.joinToString(delimiter) — превращает массив [1, 2, 3] в строку "1 2 3"
                // writer.write(...) — физически записывает полученную строку в файл.
                writer.write(array.joinToString(delimiter))

            }
        }

        // Если код выполнился без ошибок, возвращаем true.
        true

    } catch (e: Exception) {

        // Блок catch срабатывает, если в блоке try произошла ЛЮБАЯ ошибка
        // (например, нет прав на запись, диск переполнен, путь неверный).
        // В данном примере мы просто "глотаем" ошибку и возвращаем false,
        // чтобы программа могла продолжить работу.
        // Для отладки сюда можно добавить e.printStackTrace()
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
 * Сортирует массив по возрастанию на месте.
 *
 * @param array массив для сортировки
 * @return тот же массив, отсортированный по возрастанию
 */
fun sortArrayInPlace(array: IntArray): IntArray {
    val n = array.size

    // Внешний цикл — количество проходов
    for (i in 0 until n - 1) {

        // Внутренний цикл — сравнение соседних элементов
        for (j in 0 until n - 1 - i) {

            // Если текущий элемент больше следующего — меняем их местами
            if (array[j] > array[j + 1]) {

                // Обмен значений
                val temp = array[j]
                array[j] = array[j + 1]
                array[j + 1] = temp
            }
        }
    }
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

/**
 * Линейный поиск элемента в массиве.
 * Последовательно проверяет каждый элемент до нахождения совпадения. Сложность O(n).
 *
 * Использовать, когда: массив не отсортирован, очень маленький (< 20 элементов) или поиск однократный.
 * Не использовать, когда: массив большой и отсортирован (лучше [binarySearch] или [interpolationSearch]).
 *
 * @param array массив для поиска (может быть неотсортированным)
 * @param target искомое значение
 * @return индекс элемента или -1 если не найден
 */
fun findLineElement(array: IntArray, target: Int ): Int{

    for (i in array.indices){

        if (target == array[i]) {

            println("Цель ($target) найдена, индекс числа в массие: $i")

            return i
        }
    }

    println("Данного числа ($target) в массиве нет")
    return -1
}

/**
 * Бинарный поиск элемента в отсортированном массиве.
 * На каждом шаге отбрасывает половину диапазона, обеспечивая сложность O(log n).
 *
 * Использовать, когда: массив отсортирован, распределение данных неизвестно или неравномерное.
 * Не использовать, когда: массив не отсортирован (лучше [findLineElement]) или данные равномерно распределены (лучше [interpolationSearch]).
 *
 * @param array отсортированный по возрастанию массив
 * @param target искомое значение
 * @return индекс элемента или -1 если не найден
 */
fun binarySearch(array: IntArray, target: Int): Int {

    var left = 0
    var right = array.size - 1

    while (left <= right) {

        // Вычисляем середину
        val mid = left + (right - left) / 2

        when {
            array[mid] == target -> return mid
            array[mid] < target -> left = mid + 1   // Ищем справа
            else -> right = mid - 1                 // Ищем слева
        }
    }
    return -1
}

/**
 * Интерполяционный поиск элемента в отсортированном массиве.
 * Работает эффективно только при равномерном распределении данных.
 *
 * @param array отсортированный массив
 * @param target искомое значение
 * @return индекс элемента или -1 если не найден
 */
fun interpolationSearch(array: IntArray, target: Int): Int {
    var left = 0
    var right = array.size - 1

    while (left <= right && target >= array[left] && target <= array[right]) {

        // Защита от деления на ноль
        if (array[right] == array[left]) {
            if (array[left] == target) return left
            break
        }

        // Формула интерполяции
        val pos = left + ((target - array[left]) * (right - left)) /
                (array[right] - array[left])

        // Проверка границ (на случай выхода за пределы)
        if (pos < left || pos > right) break

        when {
            array[pos] == target -> return pos
            array[pos] < target -> left = pos + 1
            else -> right = pos - 1
        }
    }

    return -1
}

/**
 * Линейный поиск первого элемента, удовлетворяющего условию (предикату).
 *
 * Последовательно проверяет каждый элемент массива, передавая его в функцию [predicate].
 * Возвращает первый элемент, для которого предикат вернул true.
 *
 * ## Сложность:
 * - Время: **O(n)** — в худшем случае проверяются все элементы
 * - Память: **O(1)** — не требует дополнительной памяти
 *
 * ## Когда использовать:
 * - стоит, коглда нужен поиск по **сложному условию** (не просто сравнение с значением)
 * - когда Массив **не отсортирован**
 * - стоит, когда нужно найти **первый подходящий** элемент
 * - Не стоит для простого поиска по значению — используйте [binarySearch] или [findLineElement]
 *
 * @param T тип элементов массива
 * @param array массив для поиска
 * @param predicate функция-условие, принимающая элемент и возвращающая true/false
 * @return первый элемент, удовлетворяющий условию, или null если ничего не найдено
 *
 * @sample
 * val numbers = intArrayOf(1, 5, 12, 8, 20)
 * val firstEven = findByPredicate(numbers) { it % 2 == 0 }  // 12
 * val firstLarge = findByPredicate(numbers) { it > 15 }     // 20
 */
fun <T> findByPredicate(array: Array<T>, predicate: (T) -> Boolean): T? {
    for (element in array) {
        if (predicate(element)) {
            return element
        }
    }
    return null
}