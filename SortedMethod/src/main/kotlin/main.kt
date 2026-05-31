import com.dsa.algorithms.*     // подключаем мою библиотеку

fun main() {

    // генерируем массив из случайных чисел
    val array = generateRandomArray(
        size = 100,
        min = 1L,
        max = 30_00L
    )

    println("массив до сортировки: ${array.joinToString()}")

    val boxedArray = array.toTypedArray()

    quickSort(boxedArray, Comparator { a, b -> a.compareTo(b) })

    println("Максимальное число в массиве: ${array.max()}")
    println("Минимальное число в массиве: ${array.min()}")

    println("после: ${boxedArray.joinToString()}")
}
