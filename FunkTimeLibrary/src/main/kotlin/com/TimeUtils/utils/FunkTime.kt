package com.TimeUtils.utils

/**
 * Измеряет время выполнения функции на протяжении заданного количества итераций.
 *
 * @param iterations количество повторений замера
 * @param label текстовая метка для вывода в консоль
 * @param operation лямбда-функция без параметров, выполняющая целевое действие
 * @return суммарное затраченное время в миллисекундах
 */
fun <T> measureExecutionTime(
    iterations: Int,
    label: String,
    operation: () -> T
): Long {
    var totalTime = 0L
    println("время через $label")

    val iterStart = System.nanoTime()

    for (i in 1..iterations) {

        val result = operation()

        val iterEnd = System.nanoTime()
        val iterTime = (iterEnd - iterStart) / 1_000_000 // перевод в мс
        totalTime += iterTime

        println("Итерация $i: ${iterTime}мс, результат = $result")
    }

    return totalTime
}