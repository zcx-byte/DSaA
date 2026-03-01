import kotlin.random.Random
import com.TimeUtils.utils.*

fun main() {
    val menuText = """
        Выберите, как желаете заполнить массив?
        1. Ручной ввод
        2. Автоматическое заполнение
        3. Измерить глубину рекурсии
    """.trimIndent()

    println(menuText)
    val choice = readLine()?.toIntOrNull()

    when (choice) {
        1 -> {
            val mass = manualArr()
            println("Целые числа: ${mass.joinToString()}")

            // Выполнение расчётов для вывода результатов
            val resRecursive = averageRecursive(mass, mass.size)
            println("Среднее арифметическое через рекурсию: $resRecursive")

            val resIterative = iterationAverage(mass, mass.size)
            println("Среднее арифметическое через сумму: $resIterative")

            println()

            // Замер рекурсивного выполнения
            val timeRecursive = measureExecutionTime(
                iterations = mass.size,
                label = "рекурсию"
            ) {
                averageRecursive(mass, mass.size)
            }

            println()

            // Замер итеративной версии
            val timeIterative = measureExecutionTime(
                iterations = mass.size,
                label = "итерации"
            ) {
                iterationAverage(mass, mass.size)
            }

            println()
            println("рекурсия: $timeRecursive мс")
            println("итерации: $timeIterative мс")
        }

        2 -> {

            // Используем функцию из библиотеки вместо ручного создания массива
            // generateRandomArray(size, min, max, seed?)
            val mass = generateRandomArray(
                size = 1000,    // размер массива
                min = 0,        // минимальное значение (включительно)
                max = 100,      // максимальное значение (исключительно)
                seed = null     // null = случайный seed, можно задать число для воспроизводимости
            )

            println("Целые числа: ${mass.joinToString()}")

            val resRecursive = averageRecursive(mass, mass.size)
            println("Среднее арифметическое: $resRecursive")

            val resIterative = iterationAverage(mass, mass.size)
            println("Среднее арифметическое через сумму: $resIterative")

            println()

            // Замер рекурсивного выполнения
            val timeRecursive = measureExecutionTime(
                iterations = mass.size,
                label = "рекурсию"
            ) {
                averageRecursive(mass, mass.size)
            }

            println()

            // Замер итеративной версии
            val timeIterative = measureExecutionTime(
                iterations = mass.size,
                label = "итерации"
            ) {
                iterationAverage(mass, mass.size)
            }

            println()
            println("рекурсия: $timeRecursive мс")
            println("итерации: $timeIterative мс")
        }

        3 -> {

            // Для замера глубины рекурсии тоже используем библиотеку
            val mass = generateRandomArray(size = 20, min = 0, max = 100)
            println("Целые числа: ${mass.joinToString()}")

            val depth = measureRecursionDepth(mass, mass.size)
            println("Глубина рекурсии: $depth")
        }

        else -> println("Неверный выбор")
    }
}