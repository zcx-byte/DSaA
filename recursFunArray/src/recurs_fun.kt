import kotlin.random.Random

fun recursSum(numbers: IntArray, n: Int): Double {
    if (n <= 0) return 0.0
    return numbers[n - 1].toDouble() + recursSum(numbers, n - 1)
}

fun averageRecursive(numbers: IntArray, n: Int): Double {
    require(n > 0) { "n должно быть больше 0" }
    require(n <= numbers.size) { "n не может превышать размер массива" }
    return recursSum(numbers, n) / n
}

fun main() {
    val mass = IntArray(10) { Random.nextInt(100) }
    println("Целые числа: ${mass.joinToString()}")

    val res = averageRecursive(mass, 10)
    println("Среднее арифметическое: $res")
}