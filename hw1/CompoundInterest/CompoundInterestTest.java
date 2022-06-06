import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        assertEquals(0, CompoundInterest.numYears(2022));
        assertEquals(1, CompoundInterest.numYears(2023));
        assertEquals(100, CompoundInterest.numYears(2122));
    }

    @Test
    public void testFutureValue() {
        // When working with decimals, we often want to specify a certain
        // range of "wiggle room", or tolerance. For example, if the answer
        // is 5.04, but anything between 5.02 and 5.06 would be okay too,
        // then we can do assertEquals(5.04, answer, .02).

        // The variable below can be used when you write your tests.
        assertEquals(12.544, CompoundInterest.futureValue(10, 12, 2024), 0.01);
    }

    @Test
    public void testFutureValueReal() {

        assertEquals(11.803, CompoundInterest.futureValueReal(10, 12, 2024, 3), 0.01);

    }


    @Test
    public void testTotalSavings() {

        assertEquals(16550, CompoundInterest.totalSavings(5000, 2024, 10), 0.01);
    }

    @Test
    public void testTotalSavingsReal() {

        assertEquals(5000, CompoundInterest.totalSavingsReal(5000, 2022, 10, 10), 0.01);

    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
