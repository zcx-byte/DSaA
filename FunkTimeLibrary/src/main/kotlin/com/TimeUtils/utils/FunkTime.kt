package com.TimeUtils.utils

import java.io.File
import kotlin.random.Random

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
    var totalTime = 0L                  // L означает тип Long (64-битное целое число)
    println("время через $label")       // $label - string template (вставка переменной в строку)

    val iterStart = System.nanoTime()     // nanoTime() возвращает время в наносекундах (Long)

    for (i in 1..iterations) {          // 1..iterations - closed range (включает iterations)

        val result = operation()            // вызов лямбда-функции, переданной как параметр
        val iterEnd = System.nanoTime()

        // (iterEnd - iterStart) - разница в наносекундах
        // / 1_000_000 - перевод в миллисекунды (_ для читаемости больших чисел)
        val iterTime = (iterEnd - iterStart) / 1_000_000
        totalTime += iterTime  // сокращённая запись: totalTime = totalTime + iterTime
        println("Итерация $i: ${iterTime}мс, результат = $result")  // ${} - для сложных выражений
    }
    return totalTime
}

/**
 * Заполняет массив случайными числами в заданном диапазоне.
 *
 * @param size размер массива
 * @param min минимальное значение (включительно)
 * @param max максимальное значение (исключительно)
 * @param seed опциональное зерно для генератора (для воспроизводимости)
 * @return массив IntArray со случайными значениями
 */
fun generateRandomArray(
    size: Int,
    min: Int = 0,       // параметр со значением по умолчанию
    max: Int = 1000,
    seed: Long? = null      // Long? - nullable тип (может быть null)
): IntArray {
    require(size >= 0) { "Размер массива не может быть отрицательным" }  // проверка условия, иначе IllegalArgumentException
    require(min < max) { "min должен быть меньше max" }

    // seed?.let { Random(it) } - если seed не null, создаём Random с этим seed
    // ?: Random - если seed null, используем Random без параметров (случайный seed)
    val random = seed?.let { Random(it) } ?: Random

    // IntArray(size) { ... } - конструктор с лямбдой
    // Создаёт массив из size элементов, где каждый элемент вычисляется лямбдой
    // it - это индекс элемента (0, 1, 2, ..., size-1)
    return IntArray(size) { random.nextInt(min, max) }
}

/**
 * Заполняет существующий массив случайными числами (изменяет исходный массив).
 *
 * @param array массив для заполнения
 * @param min минимальное значение (включительно)
 * @param max максимальное значение (исключительно)
 * @param seed опциональное зерно для генератора
 * @return тот же массив, заполненный случайными значениями
 */
fun fillArrayWithRandom(
    array: IntArray,
    min: Int = 0,
    max: Int = 1000,
    seed: Long? = null
): IntArray {
    require(min < max) { "min должен быть меньше max" }

    val random = seed?.let { Random(it) } ?: Random

    // array.indices - диапазон валидных индексов массива (0 until array.size)
    // until - это полуоткрытый диапазон (не включает последний элемент)
    for (i in array.indices) {
        array[i] = random.nextInt(min, max)  // изменяем элемент по индексу
    }
    return array  // возвращаем тот же объект (для цепочки вызовов)
}

/**
 * Сохраняет массив целых чисел в текстовый файл.
 *
 * @param array массив для сохранения (IntArray)
 * @param filePath путь к файлу (строка, например "data/output.txt")
 * @param delimiter разделитель между значениями (по умолчанию пробел " ")
 *        Пример: если delimiter = ",", массив [1, 2, 3] запишется как "1,2,3"
 * @param append режим дозаписи:
 *        - false (по умолчанию) — файл будет перезаписан (старое содержимое удалится)
 *        - true — новые данные добавятся в конец существующего файла
 * @return Boolean: true если запись прошла успешно, false если произошла ошибка
 *
 * Пример использования:
 * val data = intArrayOf(10, 20, 30)
 * val success = saveArrayToFile(data, "numbers.txt", delimiter = ";")
 * // Файл numbers.txt будет содержать: "10;20;30"
 */
fun saveArrayToFile(
    array: IntArray,
    filePath: String,
    delimiter: String = " ",  // параметр со значением по умолчанию
    append: Boolean = false   // можно не указывать, будет false
): Boolean {
    // try-catch — конструкция для обработки исключений (ошибок)
    // Если внутри try произойдёт ошибка (например, нет прав на запись),
    // выполнение перейдёт в блок catch, а не завершит программу
    return try {
        // File(filePath) — создаёт объект File, представляющий путь к файлу
        // .bufferedWriter() — создаёт буферизированный писатель для эффективной записи
        // .use { writer -> ... } — scope function для автоматического управления ресурсом:
        //   • writer — параметр лямбды, представляющий BufferedWriter
        //   • Код внутри { } выполняется с открытым файлом
        //   • После завершения блока (даже при ошибке) файл автоматически закроется
        //   • Это аналог try-with-resources из Java, но короче и безопаснее
        File(filePath).bufferedWriter().use { writer ->

            // Если append = false (перезапись) и файл уже существует — удаляем его
            // !append — логическое НЕ: true если append = false
            // && — логическое И: условие выполнится только если оба условия истинны
            if (!append && File(filePath).exists()) {
                // File(filePath).delete() — удаляет файл с диска
                // Важно: создаём новый объект File, а не используем writer,
                // потому что writer — это поток записи, а не сам файл
                File(filePath).delete()
            }

            // array.joinToString(delimiter) — функция расширения для коллекций:
            //   • Преобразует массив в одну строку
            //   • Между элементами вставляет разделитель delimiter
            //   • Пример: intArrayOf(1, 2, 3).joinToString(",") → "1,2,3"
            // writer.write(...) — записывает строку в буфер (не сразу на диск!)
            writer.write(array.joinToString(delimiter))

        } // <- Здесь автоматически вызывается writer.close(), данные сбрасываются на диск

        // Если код дошёл сюда — ошибок не было
        println("Массив сохранён в файл: $filePath")  // $filePath — string template
        true  // возвращаем true (успех) — это значение вернётся из try и из функции

    } catch (e: Exception) {  // catch перехватывает любую ошибку типа Exception или его наследников
        // e — объект исключения, содержащий информацию об ошибке
        // e.message — строковое описание ошибки (может быть null, но println обработает)
        println("Ошибка при сохранении файла: ${e.message}")  // ${} — для вставки выражений

        // Возвращаем false, чтобы вызывающий код мог проверить результат
        // Это пример "мягкой" обработки ошибок: программа не падает, а возвращает статус
        false
    }
}

/**
 * Загружает массив целых чисел из текстового файла.
 *
 * @param filePath путь к файлу для чтения (например "data/input.txt")
 * @param delimiter разделитель, который использовался при сохранении (по умолчанию пробел)
 *        Важно: должен совпадать с тем, что использовался в saveArrayToFile
 * @return IntArray:
 *         - массив с данными, если чтение прошло успешно
 *         - пустой массив IntArray(0), если файл пуст или произошла ошибка
 *
 * Пример использования:
 * val numbers = loadArrayFromFile("numbers.txt", delimiter = ";")
 * // Если файл содержал "10;20;30", вернётся intArrayOf(10, 20, 30)
 *
 * // Проверка на пустой результат:
 * if (numbers.isEmpty()) {
 *     println("Файл пуст или не найден")
 * }
 */
fun loadArrayFromFile(
    filePath: String,
    delimiter: String = " "
): IntArray {
    return try {

        // File(filePath).readText() — читает ВСЁ содержимое файла в одну строку (String)
        // .trim() — удаляет пробелы, переносы строк и табуляцию в начале и конце строки
        // Это нужно, чтобы пустой файл или файл с только пробелами считался пустым
        val content = File(filePath).readText().trim()

        // Если после trim() строка пустая — нечего парсить, возвращаем пустой массив
        // return внутри try немедленно завершает функцию, возвращая значение
        if (content.isEmpty()) return IntArray(0)

        // Цепочка вызовов (method chaining) — каждая функция возвращает объект,
        // у которого сразу вызывается следующий метод. Читается слева направо, сверху вниз:

        content

            // .split(delimiter) — разбивает строку на части по разделителю
            // Возвращает List<String> (список строк)
            // Пример: "10;20;30".split(";") → listOf("10", "20", "30")
            .split(delimiter)

            // .filter { it.isNotBlank() } — оставляет только непустые элементы
            // filter — функция высшего порядка, принимает лямбду-предикат
            // it — неявный параметр лямбды, каждый элемент списка по очереди
            // isNotBlank() — возвращает true если строка не пустая и не состоит из пробелов
            // Пример: listOf("10", "", "  ", "30").filter { it.isNotBlank() } → listOf("10", "30")
            .filter { it.isNotBlank() }

            // .map { it.toInt() } — преобразует каждый элемент списка
            // map — функция высшего порядка для трансформации коллекции
            // it.toInt() — парсит строку в целое число (Int)
            // Если строка не число — выбросит NumberFormatException (перехватится в catch)
            // Возвращает List<Int>
            // Пример: listOf("10", "30").map { it.toInt() } → listOf(10, 30)
            .map { it.toInt() }

            // .toIntArray() — преобразует List<Int> в примитивный массив IntArray
            // Важно: в Kotlin List и Array — разные типы, конвертация явная
            // Возвращает IntArray — то, что нам нужно
            .toIntArray()

            // .also { ... } — scope function, которая:
            //   • Передаёт объект (IntArray) в лямбду как параметр 'it'
            //   • Выполняет код внутри { } (например, логирование)
            //   • Возвращает тот же объект неизменным (для продолжения цепочки или return)
            // Отличие от apply: apply использует 'this' и чаще для инициализации,
            // also использует 'it' и чаще для побочных эффектов (лог, отладка)
            .also {

                // it — это наш IntArray внутри лямбды
                // it.size — свойство массива, возвращает количество элементов
                println("Массив загружен из файла: $filePath (${it.size} элементов)")
            }
        // <- Здесь also возвращает IntArray, который возвращается из try и из функции

    } catch (e: Exception) {
        // Обрабатываем любые ошибки: файл не найден, нет прав на чтение,
        // или в файле есть нечисловые данные (NumberFormatException при toInt())
        println("Ошибка при загрузке файла: ${e.message}")

        // Возвращаем пустой массив вместо падения программы
        // Это позволяет вызывающему коду проверить результат:
        // val data = loadArrayFromFile("file.txt")
        // if (data.isNotEmpty()) { ... }
        IntArray(0)
    }
}

/**
 * Возвращает отсортированную копию массива (по возрастанию).
 * Исходный массив не изменяется.
 *
 * @param array исходный массив
 * @return новая отсортированная копия массива
 */
fun sortArrayAscending(array: IntArray): IntArray {

    // array.copyOf() - создаёт копию массива (чтобы не изменять оригинал)
    // .apply { ... } - scope function, которая:
    //   1. Передаёт объект (копию массива) в лямбду как 'this'
    //   2. Позволяет вызывать методы объекта без явного указания this
    //   3. Возвращает тот же объект (для цепочки вызовов)
    // sort() - сортирует массив на месте (in-place)
    // Итого: создали копию -> отсортировали копию -> вернули копию
    return array.copyOf().apply { sort() }
}

/**
 * Сортирует массив по возрастанию на месте (изменяет исходный массив).
 *
 * @param array массив для сортировки
 * @return тот же массив, отсортированный по возрастанию
 */
fun sortArrayInPlace(array: IntArray): IntArray {
    array.sort()    // sort() - метод расширения для массивов, сортирует по возрастанию
    return array    // возвращаем тот же объект
}

/**
 * Проверяет, отсортирован ли массив по возрастанию (монотонно).
 *
 * @param array массив для проверки
 * @return true если массив отсортирован по неубыванию
 */
fun isArraySortedAscending(array: IntArray): Boolean {

    // array.lastIndex - последний валидный индекс массива (array.size - 1)
    // 0 until array.lastIndex - диапазон от 0 до предпоследнего элемента
    // until не включает lastIndex, поэтому мы не выйдем за границы при array[i + 1]
    for (i in 0 until array.lastIndex) {

        // Если текущий элемент больше следующего - массив не отсортирован
        if (array[i] > array[i + 1]) return false  // ранний возврат из функции
    }
    return true  // если цикл завершился, массив отсортирован
}