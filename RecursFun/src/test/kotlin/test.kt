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

}
