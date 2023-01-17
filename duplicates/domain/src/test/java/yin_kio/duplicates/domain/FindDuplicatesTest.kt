package yin_kio.duplicates.domain


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FindDuplicatesTest {

    @Test
    fun `test findDuplicates`(){
        assertEquals(listOf<Int>(), listOf<Int>().findDuplicates())
        assertEquals(listOf<Int>(), listOf(1).findDuplicates())
        assertEquals(listOf<Int>(), listOf(1, 2).findDuplicates())
        assertEquals(listOf(listOf(1, 1)), listOf(1, 1).findDuplicates())
        assertEquals(listOf(listOf(1, 1, 1)), listOf(1, 1, 1).findDuplicates())
        assertEquals(listOf(listOf(1, 1)), listOf(1, 1, 2).findDuplicates())
        assertEquals(listOf(listOf(1, 1), listOf(2, 2)), listOf(1, 1, 2, 2).findDuplicates())
        assertEquals(listOf(listOf(3, 3)), listOf(1, 2, 3, 5, 6, 4, 3).findDuplicates())
        assertEquals(listOf(listOf("a", "a")), listOf("a", "a").findDuplicates())
        assertEquals(listOf(listOf("a", "a")), listOf("a", "b", "a").findDuplicates())
        assertEquals(listOf(listOf("a", "a")), listOf("a", "b", "a").findDuplicates { a, b -> a == b })
    }


}