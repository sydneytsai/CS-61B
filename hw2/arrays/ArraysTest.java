package arrays;
import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author Sydney Tsai
 */

public class ArraysTest {
    @Test
    public void testCatenate() {
        int[] x = {1, 2, 3};
        int[] y = {4, 5, 6};
        int[] z = {1, 2, 3, 4, 5, 6};
        assertArrayEquals(z, Arrays.catenate(x, y));
    }

    @Test
    public void testRemove() {
        int[] x = {1, 2, 3};
        int[] y = {1, 3};
        assertArrayEquals(y, Arrays.remove(x, 1, 1));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
