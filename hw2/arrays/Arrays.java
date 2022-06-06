package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author Sydney Tsai
 */
class Arrays {

    /* C1. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        int[] ans = new int[A.length + B.length];
        if (A == null) {
            return B;
        } else{
            if (B == null) {
                return A;
            }else{
                System.arraycopy(A, 0, ans, 0, A.length);
                System.arraycopy(B, 0, ans, A.length , B.length);
            }
        }
        return ans;
    }

    /* C2. */
    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. If the start + len is out of bounds for our array, you
     *  can return null.
     *  Example: if A is [0, 1, 2, 3] and start is 1 and len is 2, the
     *  result should be [0, 3]. */
    static int[] remove(int[] A, int start, int len) {
        int[] ans = new int[A.length-len];
        if (start + len > A.length){
            return null;
        } else{
            System.arraycopy(A, 0, ans, 0, start );
            System.arraycopy(A, start+len, ans, start, A.length-(start+len));
        }
        return ans;
    }
}
