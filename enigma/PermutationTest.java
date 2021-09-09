package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Permutation class.
 *
 * @author Ryan Johnson
 */
public class PermutationTest {

    /**
     * Testing time limit.
     */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /**
     * Check that perm has an alphabet whose size is that of
     * FROMALPHA and TOALPHA and that maps each character of
     * FROMALPHA to the corresponding character of FROMALPHA, and
     * vice-versa. TESTID is used in error messages.
     */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                    e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                    c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                    ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                    ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void testCase1() {
        Alphabet c = new Alphabet("ABCD");
        Permutation p = new Permutation("(BACD)", c);
        assertEquals('B', p.invert('A'));
        assertEquals('A', p.permute('B'));
        assertEquals('D', p.invert('B'));
        assertEquals('B', p.permute('D'));
        assertEquals('C', p.invert('D'));
        assertEquals('D', p.permute('C'));
        assertEquals(4, p.size());
        assertEquals(0, p.permute(1));
        assertEquals(1, p.invert(0));
        assertEquals(1, p.permute(-1));
        assertEquals(3, p.permute(6));
        assertEquals(2, p.invert(3));
        assertEquals(3, p.invert(1));
        assertTrue(p.derangement());
    }

    @Test
    public void testCase2() {
        Alphabet a = new Alphabet("A");
        Permutation p = new Permutation("(A)", a);
        assertEquals('A', p.invert('A'));
        assertEquals('A', p.permute('A'));
        assertEquals(1, p.size());
        assertEquals(0, p.permute(6));
        assertEquals(0, p.permute(-6));
        assertFalse(p.derangement());
    }

    @Test
    public void testCase3() {
        Alphabet b = new Alphabet("");
        Permutation p = new Permutation("", b);
        assertEquals(0, p.size());
        assertTrue(p.derangement());
    }

    @Test
    public void testCase4() {
        Alphabet c = new Alphabet("ABCD");
        Permutation p = new Permutation("(BAC)", c);
        assertFalse(p.derangement());
    }

}
