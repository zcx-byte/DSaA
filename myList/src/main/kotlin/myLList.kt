/**
 * Узел односвязного списка.
 * @param T тип хранимых данных.
 * @property value значение, хранимое в узле.
 * @property next ссылка на следующий узел (может быть null, если это последний элемент).
 */
class Node<T>(
    val value: T,

    // ссылка на объект типа Node (=null)
    var next: Node<T>? = null)

/**
 * Реализация односвязного списка с поддержкой дженериков.
 * @param T тип элементов в списке.
 * @param initialCap начальный размер (параметр зарезервирован, в текущей реализации на основе связной структуры не используется).
 */
class MyList<T>(initialCap: Int = 10){

    //-- TODO: Добавить функцию получения элемента по индексу
    //-- TODO:  комментарии, bigO уточнить

    private var head: Node<T>? = null
    private var size: Int = 0

    /**
     * Добавляет элемент в **конец** списка.
     *
     * **Big O: O(n)** — требуется полный обход списка для поиска последнего узла.
     *
     * @param element добавляемый элемент.
     */
    fun add(element: T){

        val newNode = Node(element)

        if(head == null){
            head = newNode
        } else{
            var current = head

            while (current?.next != null){
                current = current.next
            }
            current?.next = newNode
        }
        size++
    }

    /**
     * Добавляет элемент в **начало** списка (голова).
     * Аналог операции push в стеке.
     *
     * **Big O: O(1)** — операция выполняется за константное время, не требует обхода.
     *
     * @param value добавляемый элемент.
     */
    fun push(value: T){
        head = Node(value, head)
        size ++
    }

    /**
     * Удаляет и возвращает **первый** элемент списка (голову).
     * Аналог операции pop в стеке.
     *
     * **Big O: O(1)** — операция выполняется за константное время.
     *
     * @return значение удалённого элемента.
     * @throws NoSuchElementException если список пуст.
     */
    fun pop():T{
        val node = head ?: throw NoSuchElementException("Список пуст")

        head = node.next
        size--
        return node.value
    }

    /**
     * Удаляет и возвращает элемент по указанному индексу.
     *
     * **Big O: O(n)** — в худшем случае требуется обход до указанного индекса.
     *
     * @param index индекс удаляемого элемента (0..size-1).
     * @return значение удалённого элемента.
     * @throws IllegalArgumentException если индекс выходит за границы.
     */
    fun removeAt(index: Int):T{

        require(index in 0 until size) {"Индекс выходит за границы"}

        // если удаляется первыый элемент, то испольщуем метод pop, чтобы не повторяться
        if (index == 0) return pop()

        // ищем узел перед удаляемым
        var prev = head

        // сдвигаем указаетель, чтобы по итогу дойти до значения перед целевым
        repeat(index - 1){
            prev = prev!!.next
        }

        // сохраняем ссылку на удаляемый узел
        val remove = prev!!.next!!

        // перезаписываем сслыку
        prev.next = remove.next
        size--

        return remove.value
    }

    /**
     * Удаляет первое вхождение указанного значения из списка.
     *
     * **Big O: O(n)** — в худшем случае требуется полный обход списка.
     *
     * @param target значение для удаления.
     * @return `true`, если элемент был найден и удалён; `false` в противном случае.
     */
    fun removeEl(target: T): Boolean{

        if(head == null) return false

        if(head?.value == target){
            head = head?.next
            size--
            return true
        }

        var curent = head

        while (curent?.next != null){
            if (curent.next?.value == target){
                curent.next = curent.next?.next
                size--
                return true
            }
            curent = curent.next
        }

        return false
    }

    fun size() = size
    
    /**
     * Возвращает элемент по указанному индексу.
     *
     * **Big O: O(n)** — требуется последовательный обход до нужного индекса.
     *
     * @param index индекс запрашиваемого элемента.
     * @return значение элемента по индексу.
     * @throws IndexOutOfBoundsException если индекс отрицательный или превышает размер списка.
     */
    fun getIdEl(index: Int):T{

        if (index < 0 || head == null){

            // коммент
            throw IndexOutOfBoundsException("Индекс $index вышел за границы")
        }

        var current = head
        var i = 0
        while (current != null && i < index){
            current = current.next
            i++
        }

        if (current == null){
            throw IndexOutOfBoundsException("такого числа нету в массиве")
        }

        return current.value
    }

    /**
     * Возвращает строковое представление списка в формате [elem1, elem2, ...].
     *
     *
     * @return строка с элементами списка.
     */
    override fun toString(): String {
        val sb = StringBuilder("[")
        var current = head
        while (current != null) {
            sb.append(current.value)
            current = current.next
            if (current != null) sb.append(", ")
        }
        return sb.append("]").toString()
    }
}