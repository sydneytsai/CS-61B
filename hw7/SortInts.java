/** HW #7, Distribution counting for large numbers.
 *  @author
 */
public class SortInts {

    /** Sort A into ascending order.  Assumes that 0 <= A[i] < n*n for all
     *  i, and that the A[i] are distinct. */
    static void sort(long[] A) {
        long val = A[0];
        int index = 0;
        for (int i = 1; i < A.length; i++) {
            val = A[i];
            index = i - 1;
            while (index >= 0) {
                if (A[index] > val) {
                    A[index + 1] = A[index];
                }
                index--;
            }
            A[index + 1] = val;
        }
        // FILL IN
    }

}

