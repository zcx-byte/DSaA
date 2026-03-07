import com.dsa.algorithms.*

fun main() {

    val array = generateRandomArray(
        size = 5,
        min = 1,
        max = 3
    )
    println(array.joinToString())

    findLineElement(array, 2)

    val array2 = intArrayOf(10, 29, 40, 50, 49, 12, 60)

    println(array2.joinToString())

    findLineElement(array2, 49)
}