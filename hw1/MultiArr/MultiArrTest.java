import static org.junit.Assert.*;
import org.junit.Test;

public class MultiArrTest {

    @Test
    public void testMaxValue() {
        assertEquals(9, MultiArr.maxValue(new int[][]{ {1, 2, 3},{4, 5, 6},{7, 8, 9} }));

    }

    @Test
    public void testAllRowSums() {
        assertArrayEquals(new int[] {6, 15, 24}, MultiArr.allRowSums(new int[][]{ {1, 2, 3},{4, 5, 6},{7, 8, 9} }));
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
