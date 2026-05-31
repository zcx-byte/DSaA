class myDynamicArr<T>(initialCapacity: Int = 10) {

    // создаём наш "динамический" массив
    private var elements: Array<T?> = arrayOfNulls(initialCapacity)
    private var size = 0
    var capacity: Int = initialCapacity

    // TODO : использовать массив.. // узнать выделенную память, deox комменты и bigO

    // функция по изменению размера массивв
    private fun resize(newCapacity: Int){

        // выделится ли память?
        val newElements: Array<T?> = arrayOfNulls(newCapacity)

        // копируем эелемнты из уже созданного массива в новый
        for (i in 0 until size){
            newElements[i] = elements[i]
        }


        // обноовляем раннее созданный массив
        elements = newElements

        // и меняем размер на новый
        capacity = newCapacity
    }

    // проверка на выход за границ массива
    /**
     * Check index
     *
     * @param index
     */
    private fun checkIndex(index: Int){
        if (index < 0 || index >= size) throw IndexOutOfBoundsException("Index: $index, Size: $size")
    }

    // функция по добавлению элемента в конец массива
    fun push(element: T){
        if ( size == capacity ){
            resize(capacity * 2)
        }
        elements[size] = element
        size++
    }

    // функция удаление последнего элемента
    fun pop(): T{
        if (size == 0) throw NoSuchElementException("Нельзя применить с пустым массивом")
        val top = elements[size - 1]!!
        elements[size - 1] = null
        size--
        return top
    }

    // operator - делаем перегрузку встроенного оператора [] для получения элемента
    // это позволяет обращаться к элементам по типу arr[0] вместо arr.get(0)
    operator fun get(index: Int): T{
        checkIndex(index)

        // говорим, что данный элемент точно не null
        return elements[index]!!
    }

    // тоже самое и здесь - перегружаем оеротор [] для записи значения
    // заменяем запись arr.set(2, "..") на arr[2] = ".."
    operator fun set(index: Int, element: T){
        checkIndex(index)
        elements[index] = element
    }

    // функция удаления элемента по индексу
    fun removeAt(index: Int):T {
        checkIndex(index)

        val removed = elements[index]!!

        // сдвигаем все эелемнты справа от index на один шаг влево
        for (i in index until size - 1){

            /*
            Элемент, который был на позиции index + 1 - перезаписывает index;
            тот, что был на index + 2 - перезаписывает index + 1 и т.д.
             */
            elements[i] = elements[i + 1]
        }

        // у нас остаётся одна освободившаяся позиция - занулливаем её
        elements[size - 1] = null
        size--

        return removed
    }

    override fun toString(): String{

        if (size == 0) return "[]"

        val result = StringBuilder()
        result.append("[")

        // отделяем эелемнты списка между собой запятыми
        for (i in 0 until size){
            result.append(elements[i])
            if (i < size - 1){
                result.append(", ")
            }
        }

        result.append("]")
        return result.toString()
    }

    // функция, которая получает размер массива
    fun size() = size

    // провекра на пустой массив
    fun isEmpty(): Boolean {
        return size == 0
    }
}