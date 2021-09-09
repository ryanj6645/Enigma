package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Rotor class.
 *
 * @author Ryan Johnson
 */
public class RotorTest {

    /**
     * Testing time limit.
     */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void testCase1() {
        Alphabet a = new Alphabet("ABCD");
        Permutation p = new Permutation("(BACD)", a);
        Rotor x = new Rotor("Rotor1", p);
        assertEquals(0, x.setting());
        x.set(3);
        assertEquals(3, x.setting());
        x.set(10);
        assertEquals(2, x.setting());
        x.set(-2);
        assertEquals(2, x.setting());
        x.set('D');
        assertEquals(3, x.setting());
        assertEquals(0, x.convertForward(3));
        assertEquals(3, x.convertBackward(0));
        assertEquals(0, x.convertForward(-1));
        assertEquals(0, x.convertForward(7));
        assertEquals(3, x.convertBackward(4));
        assertEquals(3, x.convertBackward(-4));
    }

    @Test
    public void testCase2() {
        Alphabet a = new Alphabet("ABCD");
        Permutation p = new Permutation("", a);
        Rotor x = new Rotor("Rotor1", p);
        assertEquals(0, x.setting());
        x.set(3);
        assertEquals(3, x.setting());
        x.set(10);
        assertEquals(2, x.setting());
        x.set(-2);
        assertEquals(2, x.setting());
        x.set('D');
        assertEquals(3, x.setting());
        assertEquals(3, x.convertForward(3));
        assertEquals(0, x.convertBackward(0));
    }

    @Test
    public void testCase3() {
        Alphabet a = new Alphabet("1234");
        Permutation p = new Permutation("(12) (3)", a);
        Rotor x = new Rotor("Rotor1", p);
        assertEquals(0, x.setting());
        x.set(3);
        assertEquals(3, x.setting());
        x.set(10);
        assertEquals(2, x.setting());
        x.set(-2);
        assertEquals(2, x.setting());
        x.set('4');
        assertEquals(3, x.setting());
        assertEquals(1, x.convertForward(2));
        assertEquals(0, x.convertBackward(0));
        assertEquals(3, x.convertForward(3));
    }
}
