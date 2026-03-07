import com.timeutils.utils.*

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

            saveArrayToFile(mass, "save.txt", ", ")

            // Выполнение расчётов для вывода результатов
            val resRecursive = averageRecursive(mass, mass.size)
            println("Среднее арифметическое через рекурсию: $resRecursive")

            val resIterative = iterationAverage(mass, mass.size)
            println("Среднее арифметическое через сумму: $resIterative")

            // Замер рекурсивного выполнения
            val timeRecursive = measureExecutionTime(
                iterations = mass.size,
                label = "рекурсию",
                operation = {
                    averageRecursive(mass, mass.size)
                }
            )

            // Замер итеративной версии
            val timeIterative = measureExecutionTime(
                iterations = mass.size,
                label = "итерации",
                operation = { iterationAverage(mass, mass.size) }
            )

            println("рекурсия: ${timeRecursive / 1_000_000.0} мс")
            println("итерации: ${timeIterative / 1_000_000.0} мс")
        }

        2 -> {

            // Используем функцию из библиотеки вместо ручного создания массива
            // generateRandomArray(size, min, max, seed?)
            val mass = generateRandomArray(
                size = 16_000,    // размер массива
                min = 10,        // минимальное значение (включительно)
                max = 10_000,      // максимальное значение (исключительно)
                seed = null     // null = случайный seed, можно задать число для воспроизводимости
            )

            saveArrayToFile(mass, "save.txt", ", ", false)

            println("Целые числа: ${mass.joinToString()}")

            val resRecursive = averageRecursive(mass, mass.size)
            println("Среднее арифметическое: $resRecursive")

            val resIterative = iterationAverage(mass, mass.size)
            println("Среднее арифметическое через сумму: $resIterative")

            val sort = sortArrayInPlace(mass)
            println("отсортированный массив (монотонное возрастание): ${sort.joinToString()}")

            // Замер рекурсивного выполнения
            // -Xss - задание в кб размера стека
            // рекурсия будет медленне, потому что она тратит доп. время на вызов самой себя
            val timeRecursive = measureExecutionTime(
                iterations = mass.size,
                label = "рекурсию"
            ) {
                averageRecursive(mass, mass.size)
            }

            // Замер итеративной версии
            val timeIterative = measureExecutionTime(
                iterations = mass.size,
                label = "итерации",

                // передаётся лямбда-функция без аругемнтов
                operation = {
                    iterationAverage(mass, mass.size)
                }
            )

            println("рекурсия: ${timeRecursive / 1_000_000.0} мс")
            println("итерации: ${timeIterative / 1_000_000.0} мс")
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