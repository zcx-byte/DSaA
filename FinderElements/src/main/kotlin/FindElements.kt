/**
 * в папке с библиотекой - package com.dsa.algorithms
 * сам файл лежит по пути - src/main/kotlin/com/dsa/algorithms/DSaALibr.kt
 * поэтому .algorithms
 */

import com.dsa.algorithms.*     // подключаем мою библиотеку

/**
 * Возвращает случайный элемент из массива целых чисел.
 *
 * @param array массив, из которого извлекается элемент
 * @return случайное значение из массива
 * @throws IllegalArgumentException если массив пустой
 */
fun randomElement(array: IntArray): Int {
    require(array.isNotEmpty()) { "Нельзя получить элемент из пустого массива" }
    return array[kotlin.random.Random.nextInt(0, array.size)]
}

fun main() {

    val array = generateRandomArray(
        size = 30,
        min = 0,
        max = 30
    )

    println("исходный не отсортированный массив: ${array.joinToString()}")

    // сортируем массив (по возрастанию, для бинарного поиска)
    val sortArr = sortArrayAscending(array)

    println("отсортироанная копия массива: ${sortArr.joinToString()}")

    val target = randomElement(array)

    /**
     * Лучший случай O(1)
     * Средний случай O(n)
     * Худший случай O(n)
     * Требования нет
     */
    findLineElement(array, target)

    /**
     * Лучший случай O(1)
     * Средний случай O(log n)
     * Худший случай O(log n)
     * Требования Отсортированный массив
     */
    val result = binarySearch(sortArr, target)

    if (result != -1){
        println("Бинарный: Цель ($target) найдена на позиции $result")
    } else{
        println("Такого числа ($target) в массиве нету")
    }

    /**
     * Интерполяционный
     * O(1) - Лучший случай
     * O(log log n) - средний случай
     * O(n) - худший случай
     */
    val resultInterp = interpolationSearch(sortArr, target)

    if (resultInterp != -1){
        println("Интерполяционный: Цель ($target) найдена на позиции $resultInterp")
    } else{
        println("Такого числа ($target) в массиве нету")
    }


    val boxedArray = array.toTypedArray()

    val resultPred = findByPredicate(boxedArray) { it % 2 == 0 }
    println("Первое чётное число в не остортированном массиве: $resultPred")

    println()
    println("=====время поиска=======")

    val timeL = measureExecutionTime(
        iterations = array.size,
        label = "Линейный поиск",
        operation = { findLineElement(array, target) }
    )

    val timeB = measureExecutionTime(
        iterations = array.size,
        label = "Бинарный поиск",
        operation = { binarySearch(array, target) }

    )

    val timeinter = measureExecutionTime(
        iterations = array.size,
        label = "интерполяционный поиск",
        operation = { interpolationSearch(sortArr, target) }

    )

    val timePred = measureExecutionTime(
        iterations = array.size,
        label = "интерполяционный поиск",
        operation = { findByPredicate(boxedArray) { it % 2 == 0 } }

    )

    /**
     * вывод: Бинарный поиск всегда быстрее линейного, потомучто после певрой "интерации" он отбрасывет сразу половину значенйи
     *        Интерполяционный поиск может быть быстрее бинарного, когда:
     *        Данные отсортированы И распределены равномерно (10, 20, 30 и т.д.)
     *        Интерполяционный споссю лучше использовать, когда в массиве огромное кол-во элементов
     */

    println("Линейный: ${timeL / 1_000_000.0} мс")
    println("Бинарный: ${timeB / 1_000_000.0} мс")
    println("Интерполяционный: ${timeinter / 1_000_000.0} мс")
    println("Предикатный: ${timePred / 1_000_000.0} мс")

}