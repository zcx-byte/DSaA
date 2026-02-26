import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class RecursFunTest{

    @Test
    fun `zero in params`(){

        val mass = IntArray(0)

        val exception = assertThrows<IllegalArgumentException>{

            averageRecursive(mass, mass.size)
        }

        assertEquals("n должно быть больше 0", exception.message)
   }

    @Test
    fun `n bigger`(){
        val mass =  IntArray(1)

        val exception = assertThrows<IllegalArgumentException>{

            averageRecursive(mass, mass.size+1)
        }

        assertEquals("n не может превышать размер массива", exception.message)
    }

    @Test
    fun `zero in params in iterations`(){
        val mass = IntArray(0)

        val exception = assertThrows<IllegalArgumentException>{

            iterationAverage(mass, mass.size)
        }

        assertEquals("n должно быть больше 0", exception.message)
    }

    @Test
    fun `n bigger in iterations`(){
        val mass =  IntArray(1)

        val exception = assertThrows<IllegalArgumentException>{

            iterationAverage(mass, mass.size+1)
        }

        assertEquals("n не может превышать размер массива", exception.message)
    }

    // функция корректно вычисляет среднее арифметическое для положительных чисел
    // Параметр 0.001 в assertEquals задаёт допустимую погрешность для сравнения чисел с плавающей точкой
    @Test
    fun `averageRecursive calculates correct average for positive numbers`() {
        val mass = intArrayOf(10, 20, 30, 40, 50)
        val result = averageRecursive(mass, mass.size)
        assertEquals(30.0, result, 0.001)
    }

    // среднее значение массива с одним элементом
    @Test
    fun `average with single element array`() {
        val mass = intArrayOf(42)
        val recursResult = averageRecursive(mass, 1)
        val iterResult = iterationAverage(mass, 1)
        assertEquals(42.0, recursResult, 0.001)
        assertEquals(42.0, iterResult, 0.001)
    }

    // правильно обрабатывает отрицательные числа
    // Входные данные: массив [-10, -20, 30].
    // Вычисление суммы: -10 + (-20) + 30 = 0.
    // Вычисление среднего: 0 / 3 = 0.0.
    @Test
    fun `handles negative numbers correctly`() {
        val mass = intArrayOf(-10, -20, 30)
        val result = averageRecursive(mass, mass.size)
        assertEquals(0.0, result, 0.001)
    }
}
