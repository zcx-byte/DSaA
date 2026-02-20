import kotlin.random.Random

fun recursSum(numbers: IntArray, n: Int): Double {

    // if (n <= 0) - терминальная ветвь
    if (n <= 0) return 0.0

    // рекурсиваная ветвь
    return numbers[n - 1].toDouble() + recursSum(numbers, n - 1)
}

fun averageRecursive(numbers: IntArray, n: Int): Double {
    require(n > 0) { "n должно быть больше 0" }
    require(n <= numbers.size) { "n не может превышать размер массива" }
    return recursSum(numbers, n) / n
}

fun iterationAverage(numbers: IntArray, n: Int): Double{
    require(n > 0) { "n должно быть больше 0" }
    require(n <= numbers.size) { "n не может превышать размер массива" }

    var sum = 0.0
    for(i in 0..numbers.size - 1){
        sum += numbers[i]
    }
    return sum / n
}

fun main() {

    val mass = IntArray(20) { Random.nextInt(100) }
    println("Целые числа: ${mass.joinToString()}")

    val res = averageRecursive(mass, mass.size)
    println("Среднее арифметическое: $res")

    val res_average = iterationAverage(mass, mass.size)
    println("Среднее арифметическое через сумму: $res_average")

    println()

    var startTime = System.nanoTime()

    println("время через рекурсию")
    for (i in 1..mass.size){
        val endTime = System.nanoTime()
        val result = averageRecursive(mass, mass.size)
        val recurs_time = (endTime - startTime) / 1_000_000     // мс

        println("Итерация $i: ${recurs_time}мс, среднее арифметическое =${result}")
    }

    println()

    println("время через итерации")
    for (i in 1..mass.size){
        val endTime = System.nanoTime()
        val result = iterationAverage(mass, mass.size)
        val iter_time = (endTime - startTime) / 1_000_000     // мс

        println("Итерация $i: ${iter_time}мс, среднее арифметическое =${result}")
    }

}