// рекурсивная вспомогательная функиця для вычисления среднего арифмечтисеского чисел в массиве
fun recursSum(numbers: IntArray, n: Int): Double {

    // база рекурсии (при котором рекурсия завершается) n = 0
    // if (n <= 0) - терминальная ветвь
    if (n <= 0) return 0.0

    // рекурсиваная ветвь
    return numbers[n - 1].toDouble() + recursSum(numbers, n - 1)
}

// вспомогатеьная функция для вычисления среднего арифмечтисеского чисел в массиве
fun averageRecursive(numbers: IntArray, n: Int): Double {
    require(n > 0) { "n должно быть больше 0" }
    require(n <= numbers.size) { "n не может превышать размер массива" }
    return recursSum(numbers, n) / n
}

// функция для вычисленяи среднего арифмечтисеского чисел в массиве яерез итерации
fun iterationAverage(numbers: IntArray, n: Int): Double{
    require(n > 0) { "n должно быть больше 0" }
    require(n <= numbers.size) { "n не может превышать размер массива" }

    var sum = 0.0
    for(i in 0 .. numbers.size - 1){
        sum += numbers[i]
    }
    return sum / n
}

// ручное заполнение массвиа
fun manualArr(): IntArray{
    print("Введите размер массива: ")

    // проверка на null
    // если строка прочитана, пытается преобразовать её в Int
    // если преобразование невозможно (ввели текст вместо числа) или строка была null, возвращает null
    // если слева от оператора получился null, то всё выражение вохвращает
    // IntArray(size) — создание массива заданного размера
    // опционально принимает лямбда-функцию (Int) -> Int для инициализации каждого элемента (в лямбду передается индекс элемента).
    // все элементы инициализируются значением 0 по умолчанию, если не задана лямбда.
    val size = readLine()?.toIntOrNull() ?: return IntArray(0)

    val array = IntArray(size)

    for (i in 0 until size) {
        print("Введите элемент [$i]: ")

        array[i] = readLine()?.toIntOrNull()?: 0
    }
    return array
}

/*
* Глубина всегда равна значению параметра n, так как каждый рекурсивный вызов уменьшает n на 1 до достижения базового случая (n <= 0).
*/

// Вспомогательная рекурсивная функция для подсчёта глубины
private fun measureDepthHelper(numbers: IntArray, n: Int, currentDepth: Int): Int {

    // Базовый случай: возвращаем текущую глубину
    if (n <= 0) return currentDepth

    // Рекурсивный вызов с увеличением глубины на 1
    return measureDepthHelper(numbers, n - 1, currentDepth + 1)
}

// Функция для измерения глубины рекурсии
fun measureRecursionDepth(numbers: IntArray, n: Int): Int {
    require(n > 0) { "n должно быть больше 0" }
    require(n <= numbers.size) { "n не может превышать размер массива" }

    return measureDepthHelper(numbers, n, 0)
}