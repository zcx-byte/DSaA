import kotlin.random.Random

fun main() {

    // подразумевается через коммандную строку
    // померить глубину запросов

    val menuText = """
        Выберете, как желаете заполнить массив?
        1. Ручной ввод
        2. Автоматическое заполнение
        3. Измерить глубину рекурсии
    """.trimIndent()

    println(menuText)

    val qest = readLine()?.toInt()

    // ручной ввод
    if (qest == 1){

        val mass = manualArr()

        println("Целые числа: ${mass.joinToString()}")

        val res = averageRecursive(mass, mass.size)
        println("Среднее арифметическое через рекурсию: $res")

        val resAverage = iterationAverage(mass, mass.size)
        println("Среднее арифметическое через сумму: $resAverage")

        // возвращает текущее врнемя системы в наносекундах
        val startTime = System.nanoTime()

        println()

        var sumR = 0L
        println("время через рекурсию")
        for (i in 1..mass.size){
            val endTime = System.nanoTime()
            val result = averageRecursive(mass, mass.size)
            val recursTime = (endTime - startTime) / 1_000_000     // мс
            sumR += recursTime
            println("Итерация $i: ${recursTime}мс, среднее арифметическое =${result}")
        }

        var sumI = 0L

        println()

        println("время через итерации")
        for (i in 1..mass.size){
            val endTime = System.nanoTime()
            val result = iterationAverage(mass, mass.size)
            val iterTime = (endTime - startTime) / 1_000_000     // мс
            sumI += iterTime
            println("Итерация $i: ${iterTime}мс, среднее арифметическое =${result}")
        }

        println()

        println("рекурсия: $sumR мс")
        println("итерации: $sumI мс")

    } else if (qest == 2){

        val mass = IntArray(1000) { Random.nextInt(100) }
        println("Целые числа: ${mass.joinToString()}")

        val res = averageRecursive(mass, mass.size)
        println("Среднее арифметическое: $res")

        val resAverage = iterationAverage(mass, mass.size)
        println("Среднее арифметическое через сумму: $resAverage")

        println()

        // возвращает текущее врнемя системы в наносекундах
        val startTime = System.nanoTime()

        var sumR = 0L
        // блок вычисления времени выполнения программы разными методами
        println("время через рекурсию")
        for (i in 1..mass.size){
            val endTime = System.nanoTime()
            val result = averageRecursive(mass, mass.size)
            val recursTime = (endTime - startTime) / 1_000_000     // мс
            sumR += recursTime
            println("Итерация $i: ${recursTime}мс, среднее арифметическое =${result}")
        }

        println()

        var sumI = 0L

        println("время через итерации")
        for (i in 1..mass.size){
            val endTime = System.nanoTime()
            val result = iterationAverage(mass, mass.size)
            val iterTime = (endTime - startTime) / 1_000_000     // мс
            sumI += iterTime
            println("Итерация $i: ${iterTime}мс, среднее арифметическое =${result}")
        }

        println()

        println("рекурсия: $sumR мс")
        println("итерации: $sumI мс")
    } else if (qest == 3){

        val mass = IntArray(20) { Random.nextInt(100) }
        println("Целые числа: ${mass.joinToString()}")

        // Измерение глубины рекурсии
        val depth = measureRecursionDepth(mass, mass.size)
        println("Глубина рекурсии: $depth")

    }


}