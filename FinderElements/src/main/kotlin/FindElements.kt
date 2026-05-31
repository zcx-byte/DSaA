/**
 * в папке с библиотекой - package com.dsa.algorithms
 * сам файл лежит по пути - src/main/kotlin/com/dsa/algorithms/DSaALibr.kt
 * поэтому .algorithms
 */

import com.dsa.algorithms.*     // подключаем мою библиотеку

/**
 * Возвращает случайный элемент из массива целых чисел типа Long.
 *
 * @param array массив, из которого извлекается элемент
 * @return случайное значение из массива
 * @throws IllegalArgumentException если массив пустой
 */
fun randomElement(array: LongArray): Long {
    require(array.isNotEmpty()) { "Нельзя получить элемент из пустого массива" }
    return array[kotlin.random.Random.nextInt(0, array.size)]
}

fun main() {

    val array = generateRandomArray(
        size = 100,
        min = 1L,
        max = 30_00L
    )



    println("исходный не отсортированный массив: ${array.joinToString()}")

    // Создаём компаратор для типа Long (необходим для обобщённых функций)
    // a, b - переопределение класса Compare -> a.compareTo(b) - возвращение
    // передаём а, b и гворим, что нам а нужно сравнить с b и верунть Int
    // почему Int - потому что Int самый минимальный и достаточный тмип для сравнения чисел
    //
    val longComparator = Comparator<Long> { a, b -> a.compareTo(b) }

    val arrayNew = arrayOf(8L, 33L, 17L, -2L, 200L, 21L, 11L)

    val arrayNewSort = BubleSortArrayInPlace(arrayNew, longComparator)

    println("-------------------------")
    println(arrayNewSort.joinToString())
    println("-------------------------")

    val targetT = 9L

    val binarSerch = binarySearch(arrayNew, targetT, longComparator)

    println("-------------------------")
    println(binarSerch)
    println("-------------------------")

    // Сортируем массив (по возрастанию, для бинарного поиска)
    // sortArrayAscending теперь принимает Array<T> и Comparator
    val sortArr = sortArrayAscending(array.toTypedArray(), longComparator)

    println("отсортированная копия массива: ${sortArr.joinToString()}")

    val target = randomElement(array)

    /**
     * Линейный поиск
     * ## Сложность:
     * - Лучший случай: **O(1)**
     * - Средний случай: **O(n)**
     * - Худший случай: **O(n)**
     * - Требования: нет
     */
    // findLineElement теперь принимает Array<T>, target: T и Comparator
    val resL = findLineElement(array.toTypedArray(), target, longComparator)

    if (resL != -1L) {
        println("Линейный: цель ($target) найдена на позиции $resL")
    } else {
        println("Такого числа ($target) в массиве нет")
    }

    /**
     * Бинарный поиск
     * ## Сложность:
     * - Лучший случай: **O(1)**
     * - Средний случай: **O(log n)**
     * - Худший случай: **O(log n)**
     * - Требования: отсортированный массив
     */
    val result = binarySearch(sortArr, target, longComparator)

    if (result != -1L) {
        println("Бинарный: цель ($target) найдена на позиции $result")
    } else {
        println("Такого числа ($target) в массиве нет")
    }

    /**
     * Интерполяционный поиск
     * ## Сложность:
     * - Лучший случай: **O(1)**
     * - Средний случай: **O(log log n)** (при равномерном распределении)
     * - Худший случай: **O(n)**
     * - Требования: отсортированный массив + равномерное распределение
     */
    // Функция преобразования Long -> Double для расчёта интерполяции
    val toDouble: (Long) -> Double = { it.toDouble() }
    val resultInterp = interpolationSearch(sortArr, target, longComparator, toDouble)

    if (resultInterp != -1L) {
        println("Интерполяционный: цель ($target) найдена на позиции $resultInterp")
    } else {
        println("Такого числа ($target) в массиве нет")
    }

    // Преобразуем LongArray в Array<Long> для работы с findByPredicate
    val boxedArray = array.toTypedArray()

    // it — неявно указанный параметр, обозначает "этот элемент"
    // Обратите внимание: 0L вместо 0, так как элементы имеют тип Long
    val resultPred = findByPredicate(boxedArray) { it % 2 == 0L }
    println("Первое чётное число в не отсортированном массиве: $resultPred")

    println()
    println("===== время поиска =======")

    val timeL = measureExecutionTime(
        iterations = array.size,
        label = "Линейный поиск",
        operation = { findLineElement(array.toTypedArray(), target, longComparator) }
    )

    val timeB = measureExecutionTime(
        iterations = array.size,
        label = "Бинарный поиск",
        operation = { binarySearch(sortArr, target, longComparator) }
    )

    val timeInterp = measureExecutionTime(
        iterations = array.size,
        label = "Интерполяционный поиск",
        operation = { interpolationSearch(sortArr, target, longComparator, toDouble) }
    )

    val timePred = measureExecutionTime(
        iterations = array.size,
        label = "Предикатный поиск",
        operation = { findByPredicate(boxedArray) { it % 2 == 0L } }
    )

    /**
     * Вывод:
     * - Бинарный поиск всегда быстрее линейного, потому что после первой итерации
     *   он отбрасывает сразу половину значений.
     * - Интерполяционный поиск может быть быстрее бинарного, когда:
     *   1. Данные отсортированы
     *   2. Распределены равномерно (10, 20, 30 и т.д.)
     * - Интерполяционный способ лучше использовать, когда в массиве огромное
     *   количество элементов и выполнено условие равномерности.
     */

    println("Линейный: ${timeL / 1_000_000.0} мс")
    println("Бинарный: ${timeB / 1_000_000.0} мс")
    println("Интерполяционный: ${timeInterp / 1_000_000.0} мс")
    println("Предикатный: ${timePred / 1_000_000.0} мс")
}