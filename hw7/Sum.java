/** HW #7, Two-sum problem.
 * @author
 */
public class Sum {

    /** Returns true iff A[i]+B[j] = M for some i and j. */
    public static boolean sumsTo(int[] A, int[] B, int m) {
        java.util.Arrays.sort(A);
        for (int i = 0; i < B.length; i++) {
            int left = m - B[i];
            if (java.util.Arrays.binarySearch(A, left) >= 0) {
                return true;
            }
        }
        return false;
        // REPLACE WITH YOUR ANSWER
    }
}
