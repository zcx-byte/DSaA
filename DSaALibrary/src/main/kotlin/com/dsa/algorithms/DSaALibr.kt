// Автор: Прасков Д.Е. Студент 2-го курса ЗабГУ ИВТ-24

package com.dsa.algorithms

import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import kotlin.collections.mutableListOf
import kotlin.random.Random

// ================= ПОДСЧЁТ ВРЕМЕНИ =================
/**
 * Измеряет суммарное время выполнения [operation] за [iterations] повторов.
 * Возвращает время в наносекундах (для перевода в мс: делите на 1_000_000.0).
 *
 * @param iterations количество повторений замера
 * @param label метка для удобства (не используется в расчётах)
 * @param operation лямбда-функция для измерения
 * @return суммарное время всех итераций в наносекундах
 */
fun <T> measureExecutionTime(
    iterations: Int,
    label: String,
    operation: () -> T
): Long {
    return (1..iterations).sumOf {
        val start = System.nanoTime()
        operation()
        System.nanoTime() - start
    }
}

// ================= ЧИСЛОВЫЕ ФУНКЦИИ (LongArray) =================
// Эти функции требуют арифметических операций,
// поэтому оставлены для типа LongArray.

/**
 * Заполняет массив случайными числами в заданном диапазоне.
 *
 * @param size размер массива
 * @param min минимальное значение (включительно)
 * @param max максимальное значение (исключительно)
 * @param seed опциональное зерно для генератора (для воспроизводимости)
 * @return новый массив LongArray со случайными значениями
 */
fun generateRandomArray(
    size: Int,
    min: Long = 0,
    max: Long = 1000,
    seed: Long? = null
): LongArray {

    require(size >= 0) { "Размер массива не может быть отрицательным" }
    require(min < max) { "min должен быть меньше max" }

    val random = seed?.let { Random(it) } ?: Random
    return LongArray(size) { random.nextLong(min, max) }
}

/**
 * Заполняет существующий массив случайными числами (изменяет исходный массив).
 *
 * @param array массив для заполнения
 * @param min минимальное значение (включительно)
 * @param max максимальное значение (исключительно)
 * @param seed опциональное зерно для генератора
 * @return тот же массив (для поддержки цепочки вызовов)
 */
fun fillArrayWithRandom(
    array: LongArray,
    min: Long = 0,
    max: Long = 1000,
    seed: Long? = null
): LongArray {

    require(min < max) { "min должен быть меньше max" }
    val random = seed?.let { Random(it) } ?: Random
    for (i in array.indices) {
        array[i] = random.nextLong(min, max)
    }
    return array
}

// ================= СОХРАНИТЬ/ЗАГРУЗИТЬ МАССИВ В/ИЗ ФАЙЛА =================

/**
 * Сохраняет массив целых чисел типа Long в текстовый файл.
 *
 * @param array массив для сохранения
 * @param filePath путь к файлу
 * @param delimiter разделитель (по умолчанию пробел)
 * @param append false — перезапись, true — дозапись
 * @return true если успешно, false при ошибке
 */
fun saveArrayToFile(
    array: LongArray,
    filePath: String,
    delimiter: String = " ",
    append: Boolean = false
): Boolean {
    return try {
        FileOutputStream(filePath, append).use { fos ->
            OutputStreamWriter(fos, Charsets.UTF_8).use { writer ->
                writer.write(array.joinToString(delimiter))
            }
        }
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * Загружает массив целых чисел типа Long из текстового файла.
 *
 * @param filePath путь к файлу
 * @param delimiter разделитель (должен совпадать с тем, что при сохранении)
 * @return массив с данными типа LongArray или пустой массив при ошибке/отсутствии данных
 */
fun loadArrayFromFile(
    filePath: String,
    delimiter: String = " "
): LongArray {
    return try {
        val content = File(filePath).readText(Charsets.UTF_8).trim()
        if (content.isEmpty()) return LongArray(0)
        content.split(delimiter)
            .filter { it.isNotBlank() }
            .map { it.toLong() }
            .toLongArray()
    } catch (e: Exception) {
        LongArray(0)
    }
}

// ================= СОРТИРОВКА МАССИВА =================

/**
 * Возвращает отсортированную копию массива (по возрастанию согласно компаратору).
 * Исходный массив не изменяется.
 *
 * @param T тип элементов, поддерживающий сравнение через компаратор
 * @param array исходный массив
 * @param comparator объект, определяющий порядок сравнения элементов
 * @return новая отсортированная копия массива
 */
fun <T> sortArrayAscending(array: Array<T>, comparator: Comparator<T>): Array<T> {
    return array.copyOf().apply { sortWith(comparator) }
}

/**
 * Сортирует массив по возрастанию (изменяет данный массив).
 *
 * ## Сложность:
 * - Время: **O(n^2)** — реализован алгоритм пузырьковой сортировки.
 *
 * @param T тип элементов, поддерживающий сравнение через компаратор
 * @param array массив для сортировки
 * @param comparator объект, определяющий порядок сравнения элементов
 * @return тот же массив, отсортированный по возрастанию
 */
fun <T> BubleSortArrayInPlace(array: Array<T>, comparator: Comparator<T>): Array<T> {

    val n = array.size

    for (i in 0 until n - 1) {

        for (j in 0 until n - 1 - i) {

            // сравнение
            if (comparator.compare(array[j], array[j + 1]) > 0) {

                val temp = array[j]

                array[j] = array[j + 1]

                array[j + 1] = temp
            }
        }
    }
    return array
}

/**
 * Сортирует массив по возрастанию с помощью алгоритма быстрой сортировки (изменяет исходный массив).
 *
 * ## Как работает:
 * 1. Выбирается опорный элемент (`pivot`) из середины отрезка.
 * 2. Массив делится на две части: элементы < pivot -> влево, > pivot -> вправо.
 * 3. Алгоритм рекурсивно применяется к левой и правой части.
 *
 * ## Сложность:
 * - Время (средний случай): **O(n log n)**
 * - Время (худший случай): **O(n²)** — при неудачном выборе pivot (например, уже отсортированный массив)
 *
 * @param T тип элементов, который должен реализовывать `Comparable<T>` для возможности сравнения
 * @param array массив типа `Array<T>` для сортировки
 * @param left начальный индекс сортируемого отрезка (обычно `0`)
 * @param right конечный индекс сортируемого отрезка (обычно `array.lastIndex`)
 * @return Unit (функция изменяет массив «на месте», явный возврат не требуется)
 */
fun <T: Comparable<T>> quickSort(
    array: Array<T>,
    left: Int,
    right: Int) {

    // если наш массив уже отсортирован (состоит из 1 элемента)
    // по факту - условие выхода из рекурсии
    if (left >= right) return

    // берём число, которое хранится в середине массива
    val pivot = array[(left + right) / 2]

    // элемент, который пойдёт слева направо и будет искать больше pivot
    var i = left

    // элемент, который пойдёт справа налево и будет искать меньше pivot
    var j = right

    // цикл разделения на подмассивы
    while(i <= j){

        // пока левый элемент меньше pivot - сдвигаем его вправо
        while (array[i] < pivot) i++

        // пока правый элемент больше pivot - сдвигаем его влево
        while (array[j] > pivot) j--

        // дополнительная проверка, если нашёлся такой элемент, который "стоит не на своём месте"
        if (i <= j){
            val temp = array[i]
            array[i] = array[j]
            array[j] = temp
            i++
            j--
        }
    }

    // дальше с помощью рекурсии сортируем наши получившиеся массивы
    //
    quickSort(array, left, j)
    quickSort(array, i, right)
}

/**
 * Сортирует массив по возрастанию с помощью алгоритма сортировки слиянием (Merge Sort).
 *
 * ## Как работает (принцип «разделяй и властвуй»):
 * 1. **Базовый случай**: если массив содержит 0 или 1 элемент, он уже считается отсортированным.
 * 2. **Разделение**: массив делится пополам на `left` и `right` с помощью `copyOfRange`.
 * 3. **Рекурсия**: `mergeSort` вызывается рекурсивно для каждой половины до достижения базового случая.
 * 4. **Слияние**: отсортированные половины объединяются функцией [merge] в один отсортированный массив.
 *
 * ## Сложность:
 * - Время (все случаи): **O(n log n)** — массив делится пополам `log n` раз, слияние на каждом уровне требует `O(n)`.
 *
 * @param T тип элементов, который должен реализовывать `Comparable<T>` для возможности сравнения
 * @param array массив типа `Array<T>` для сортировки
 * @return новый отсортированный массив типа `Array<T>` (исходный массив не изменяется)
 */
fun <T: Comparable<T>> mergeSort(
    array: Array<T>,
): Array<T> {
    if (array.size <= 1 ) return array

    // вычисляем индекс элемента посередине
    val mid = array.size / 2

    // разбиваем массив
    // от 0 элемента до mid
    val left = array.copyOfRange(0, mid)

    // и от mid до последнего элемента
    val right = array.copyOfRange(mid, array.size)

    // рекурсией уже делаем тоже самое, только с ранее разделёнными массивами
    // получаем по итогу деиничные массивы
    val sortedLeft = mergeSort(left)
    val sortedRight = mergeSort(right)

    // возвращаем с помощью основной функции отсортированный массив
    return merge(sortedLeft, sortedRight)
}

/**
 * Объединяет два уже отсортированных массива в один отсортированный массив.
 *
 * ## Алгоритм слияния:
 * 1. Создаётся буферный массив `result` размером `left.size + right.size` того же типа, что и входные массивы.
 * 2. Три указателя (`i`, `j`, `k`) последовательно проходят по `left`, `right` и `result`.
 * 3. На каждом шаге сравниваются текущие элементы `left[i]` и `right[j]`:
 *    - меньший элемент записывается в `result[k]`, соответствующие указатели сдвигаются.
 * 4. После исчерпания одного из массивов оставшиеся элементы второго дописываются в конец результата.
 *
 * ## Сложность:
 * - Время: **O(n)**, где `n = left.size + right.size` — каждый элемент просматривается ровно один раз.
 *
 * @param T тип элементов, который должен реализовывать `Comparable<T>`
 * @param left первый отсортированный массив
 * @param right второй отсортированный массив
 * @return новый массив, содержащий все элементы из `left` и `right` в отсортированном порядке
 */
private fun <T: Comparable<T>> merge(
    left: Array<T>, right: Array<T>
): Array<T> {

    // val result = ArrayList<T>(left.size + right.size)
    // val result = mutableListOf<T>()
    // val result = arrayOfNulls<Any?>(left.size + right.size)

    // чтобы не было проблем с типом, копируем уже созданный массив (который мы передаём в функцию) с определённым типом
    val result = left.copyOf(left.size + right.size)

    var i = 0
    var j = 0
    var k = 0

    // TODO: В ОТДЕЛЬНУЮ ФУНКЦИЮ АЛГОРИТМ

    while (i < left.size && j < right.size){

        if (left[i] <= right[j]){
            result[k] = left[i]
            //result.add(left[i])
            i++
            k++

        } else {
            result[k] = right[j]
            //result.add(right[j])
            j++
            k++
        }
    }

    while (i < left.size){
        result[k] = left[i]
        //result.add(left[i])
        i++
        k++
    }

    while (j < right.size){
        result[k] = right[j]
        //result.add(right[j])
        j++
        k++
    }

    //return result.toTypedArray()
    return result as Array<T>

}

/**
 * Сортирует массив по возрастанию методом Шелла (in-place).
 *
 * ## Сложность:
 * - Время: **O(n^(3/2))** в среднем (используется последовательность шагов Кнута)
 *
 * @param T тип элементов, поддерживающий естественное сравнение через [Comparable]
 * @param array массив для сортировки (изменяется непосредственно)
 */
fun <T: Comparable<T>> shellSort(
  array: Array<T>
){

    val n = array.size
    if(n < 2) return

    // вычисляем начальный шаг
    var gap = n / 2

    while (gap > 0){

        for (i in gap until n){
            var temp = array[i]

            var j = i

            while (j >= gap && array[j-gap] > temp){
                array[j] = array[j - gap]
                j -= gap
            }
            array[j] = temp
        }

        // сокращаем шаг в 2 раз
        gap /= 2
    }
    // всегда приходим к gap = 1
}

/**
 * Проверяет, отсортирован ли массив по возрастанию согласно компаратору.
 *
 * ## Сложность:
 * - Время: **O(n)** — в худшем случае проверяются все соседние пары
 * - Память: **O(1)** — константная дополнительная память
 *
 * @param T тип элементов, поддерживающий сравнение через компаратор
 * @param array массив для проверки
 * @param comparator объект, определяющий порядок сравнения элементов
 * @return true если массив отсортирован по неубыванию
 */
fun <T> isArraySortedAscending(array: Array<T>, comparator: Comparator<T>): Boolean {
    for (i in 0 until array.lastIndex) {
        if (comparator.compare(array[i], array[i + 1]) > 0) return false
    }
    return true
}


// ================= ФУНКЦИИ ПОИСКА =================

/**
 * Линейный поиск элемента в массиве с использованием компаратора.
 * Последовательно проверяет каждый элемент до нахождения совпадения.
 *
 * ## Сложность:
 * - Время:
 *   - Лучший случай: **O(1)** — искомый элемент на первой позиции
 *   - Средний случай: **O(n)** — элемент находится примерно в середине
 *   - Худший случай: **O(n)** — элемент в конце массива или отсутствует
 * - Память: **O(1)** — используется константное количество дополнительной памяти
 *
 * @param T тип элементов, поддерживающий сравнение через компаратор
 * @param array массив для поиска (может быть неотсортированным)
 * @param target искомое значение
 * @param comparator объект, определяющий порядок сравнения элементов
 * @return индекс элемента или -1 если не найден
 */
fun <T> findLineElement(array: Array<T>, target: T, comparator: Comparator<T>): Long {
    for (i in array.indices) {

        if (comparator.compare(array[i], target) == 0) {

            return i.toLong()
        }
    }
    return -1
}

/**
 * Бинарный поиск элемента в отсортированном массиве.
 * На каждом шаге отбрасывает половину диапазона, обеспечивая логарифмическую сложность.
 *
 * ## Сложность:
 * - Время:
 *   - Лучший случай: **O(1)** — искомый элемент сразу в середине
 *   - Средний случай: **O(log n)**
 *   - Худший случай: **O(log n)** — требуется максимальное число делений пополам
 *
 * @param T тип элементов, поддерживающий сравнение через компаратор
 * @param Comparable<T> Интерфейс, который гарантирует, что объекты типа T можно сравнивать друг с другом
 * @param array отсортированный по возрастанию массив
 * @param target искомое значение
 * @return индекс элемента или -1 если не найден
 */
fun <T: Comparable<T>> binarySearch(
    array: Array<T>, target: T
): Long{

    /**
     * Comparable <T>
     * Тип Т может быть любым, только если он реализует Comparable<T>
     * Операторы сравнения (>, <, >=, <=) в Kotlin автоматически вызывают метод compare
     * без такого ограничения компилятор не позволит выполнить сравнение, т.к. не знает,
     * поддерживает ли Т сравнеие
     * <T: Comparable<T>> - обеспечивает типобезопаснсть (гарантируем, что объекты будут одного типа)
     */

    var left = 0
    var right = array.size - 1

    while (left <= right){

        val mid = left + (right - left) / 2

        if (array[mid] == target){
            return mid.toLong()
        } else{
            if (array[mid] < target){
                left = mid + 1
            } else {
                right = mid - 1
            }
        }
    }

    return -1
}

/**
 * Интерполяционный поиск элемента в отсортированном массиве.
 * Оценивает позицию искомого элемента на основе его числового значения относительно границ диапазона.
 *
 * ## Сложность:
 * - Время:
 *   - Лучший случай: **O(1)** — угадали позицию с первой попытки
 *   - Средний случай: **O(log log n)** — *только при равномерном распределении данных*
 *   - Худший случай: **O(n)** — при неравномерном распределении
 * - Память: **O(1)** — константная дополнительная память
 *
 * ## Важные условия:
 * - Массив **должен быть отсортирован** согласно [comparator]
 * - Данные должны быть **равномерно распределёнными** для эффективности
 *
 * @param T тип элементов массива
 * @param array отсортированный массив
 * @param target искомое значение
 * @param comparator компаратор для сравнения элементов
 * @param toLong функция преобразования элемента в Long для расчёта интерполяции
 * @return индекс элемента или -1 если не найден
 */
fun <T> interpolationSearch(
    array: Array<T>,
    target: T,
    comparator: Comparator<T>,
    toDouble: (T) -> Double
): Long {

    var left = 0
    var right = array.size - 1

    while (left <= right) {

        val targetVal = toDouble(target)

        // значения
        val leftVal = toDouble(array[left])
        val rightVal = toDouble(array[right])

        // Защита от деления на ноль (все элементы в диапазоне одинаковы)
        if (rightVal == leftVal) {
            if (comparator.compare(array[left], target) == 0) return left.toLong()
            break
        }

        // Формула интерполяции
        val pos = left + ((targetVal - leftVal) * (right - left) / (rightVal - leftVal)).toInt()

        // Проверка границ (на случай выхода зца пределы из-за округления)
        if (pos < left || pos > right) break

        when {
            comparator.compare(array[pos], target) == 0 -> return pos.toLong()
            comparator.compare(array[pos], target) < 0 -> left = pos + 1
            else -> right = pos - 1
        }
    }

    return -1
}

/**
 * Линейный поиск первого элемента, удовлетворяющего условию (предикату).
 *
 * Последовательно проверяет каждый элемент массива, передавая его в функцию [predicate].
 * Возвращает первый элемент, для которого предикат вернул `true`.
 *
 * ## Сложность:
 * - Время:
 *   - Лучший случай: **O(1)** — первый элемент удовлетворяет условию
 *   - Средний случай: **O(n)** — в зависимости от распределения подходящих элементов
 *   - Худший случай: **O(n)** — ни один элемент не подходит или подходящий последний
 * - Память: **O(1)** — не использует дополнительную память, кроме хранения замыкания
 *
 * ## Когда использовать:
 * - когда нужен поиск по **сложному условию** (не просто сравнение с значением)
 * - когда массив **не отсортирован**
 * - когда нужно найти **первый подходящий** элемент
 * - Не стоит для простого поиска по значению
 *
 * @param T тип элементов массива
 * @param array массив для поиска
 * @param predicate функция-условие, принимающая элемент и возвращающая Boolean
 * @return первый элемент, удовлетворяющий условию, или `null` если ничего не найдено
 *
 * @sample
 * val numbers = arrayOf("1", "5", "12", "8", "20")
 * val firstEven = findByPredicate(numbers) { it.toInt() % 2 == 0 }  // "12"
 */
fun <T> findByPredicate(array: Array<T>, predicate: (T) -> Boolean): T? {

    // 1. Последовательный обход всех элементов массива
    for (element in array) {

        // 2. Применение предиката к текущему элементу
        // Если условие выполнено — предикат вернёт true
        if (predicate(element)) {

            // 3. Немедленный возврат найденного элемента
            // Функция завершает работу, не проверяя остальные элементы
            return element
        }
        // Если условие ложно — цикл продолжается
    }

    // 4. Если цикл завершился без возврата — элемент не найден
    return null
}