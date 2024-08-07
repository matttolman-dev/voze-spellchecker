import com.voze.mtolman.Dictionary
import kotlin.test.Test
import kotlin.test.assertEquals

class DistanceTests {
    @Test
    fun wikipediaExamples() {
        assertEquals(1, Dictionary.distance("kitten", "sitten"))
        assertEquals(1, Dictionary.distance("sitten", "sittin"))
        assertEquals(1, Dictionary.distance("sittin", "sitting"))
        assertEquals(1, Dictionary.distance("uninformed", "uniformed"))
    }

    @Test
    fun moreComplexExamples() {
        assertEquals(2, Dictionary.distance("digs", "dog"))
        assertEquals(3, Dictionary.distance("iter", "bitten"))
        assertEquals(3, Dictionary.distance("cross", "drove"))
        assertEquals(4, Dictionary.distance("mix", "boxer"))
    }
}