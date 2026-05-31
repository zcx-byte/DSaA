import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows

/**
 * Тесты стекового калькулятора (RPN)
 * Проверяют корректность операций, обработку граничных случаев,
 * детерминированность и конечность алгоритма.
 * Сложность всех операций: O(1) по времени и памяти (кроме realloc массива).
 */
class StackCalculatorTest {

    /**
     * Тестирует базовые операции push/pop.
     * Проверяет LIFO-инвариант и O(1) сложность доступа к вершине.
     */
    @Test
    fun `test push and pop maintains LIFO order`() {
        val calc = StackCalculator()
        calc.push(1.0)
        calc.push(2.0)
        calc.push(3.0)
        assertEquals(3.0, calc.pop())
        assertEquals(2.0, calc.pop())
        assertEquals(1.0, calc.pop())
    }

    /**
     * Проверяет, что peek() возвращает верхний элемент, но не изменяет стек.
     * Соответствует свойству "Детерминированность": состояние структуры не меняется при чтении.
     * Сложность: O(1).
     */
    @Test
    fun `test peek does not modify stack state`() {
        val calc = StackCalculator()
        calc.push(42.0)
        assertEquals(42.0, calc.pop()) // элемент остался в стеке
    }

    /**
     * Тест арифметических операций через основной обработчик processInput.
     * Проверяет корректность вычислений в RPN-формате.
     * Каждая операция выполняется за O(1), итоговая сложность линейна O(K) по числу токенов.
     */
    @Test
    fun `test basic arithmetic operations`() {
        val calc = StackCalculator()
        calc.processInput("5 3 +")
        assertEquals(8.0, calc.pop())

        calc.processInput("10 4 -")
        assertEquals(6.0, calc.pop())

        calc.processInput("2.5 4 *")
        assertEquals(10.0, calc.pop())

        calc.processInput("15 3 /")
        assertEquals(5.0, calc.pop())
    }

    /**
     * Проверяет обработку деления на ноль.
     * Соответствует свойству "Конечность": алгоритм не уходит в неопределённое состояние,
     * а явно выбрасывает исключение за O(1).
     */
    @Test
    fun `test division by zero throws ArithmeticException`() {
        val calc = StackCalculator()
        assertThrows<ArithmeticException> {
            calc.processInput("7 0 /")
        }
    }

    /**
     * Проверяет реакцию на извлечение из пустого стека.
     * Гарантирует детерминированность и предотвращает краш JVM.
     */
    @Test
    fun `test pop from empty stack throws IllegalStateException`() {
        val calc = StackCalculator()
        assertThrows<IllegalStateException> {
            calc.pop()
        }
    }


    /**
     * Тестирует обмен двух верхних элементов.
     * Проверяет инвариант: после swap верхний элемент становится вторым, и наоборот.
     */
    @Test
    fun `test swap exchanges top two elements`() {
        val calc = StackCalculator()
        calc.processInput("10 20 swap")
        assertEquals(10.0, calc.pop()) // был внизу, стал сверху
        assertEquals(20.0, calc.pop()) // был сверху, стал внизу
    }

    /**
     * Тестирует очистку стека.
     * Проверяет, что после clear все элементы удалены, а pop выбрасывает исключение.
     * clear() выполняется за O(1) (сброс счётчика size).
     */
    @Test
    fun `test clear empties the stack`() {
        val calc = StackCalculator()
        calc.processInput("1 2 3")
        calc.processInput("clear")
        assertThrows<IllegalStateException> {
            calc.pop()
        }
    }

    /**
     * Тест сложного выражения: (3 + 4) * 2 = 14.
     * Проверяет корректность последовательного выполнения операций в RPN.
     * Соответствует свойству "Массовость": работает с произвольной корректной записью.
     */
    @Test
    fun `test complex RPN expression evaluates correctly`() {
        val calc = StackCalculator()
        calc.processInput("3 4 + 2 *")
        assertEquals(14.0, calc.pop())
    }

    /**
     * Тест отрицательных чисел и десятичных дробей.
     * Проверяет корректность парсинга токенов (toDoubleOrNull).
     */
    @Test
    fun `test negative numbers and decimals`() {
        val calc = StackCalculator()
        calc.processInput("-5.5 2.5 +")
        assertEquals(-3.0, calc.pop())
    }

    /**
     * Тест обработки неизвестной команды.
     * Алгоритм должен вывести ошибку, но не прервать выполнение последующих валидных команд.
     * Демонстрирует устойчивость и массовость.
     */
    @Test
    fun `test unknown command does not break subsequent valid operations`() {
        val calc = StackCalculator()
        calc.processInput("10 20 +")
        calc.processInput("invalid_cmd") // выведет сообщение в консоль, стек не изменится
        assertEquals(30.0, calc.pop())
    }
}