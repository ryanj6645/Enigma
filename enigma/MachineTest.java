package enigma;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.ArrayList;

import static enigma.TestUtils.UPPER_STRING;
import static org.junit.Assert.assertEquals;

/** The suite of all JUnit tests for the Machine class.
 *  @author Ryan Johnson
 */
public class MachineTest {
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
        Alphabet a = new Alphabet(
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        Permutation iiip = new Permutation(
                "(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)", a);
        Permutation iip = new Permutation(
                "(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) (GR) (NT) (A) (Q)", a);
        Permutation ip = new Permutation(
                "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", a);
        Permutation betap = new Permutation(
                "(ALBEVFCYODJWUGNMQTZSKPR) (HIX)", a);
        Permutation bp = new Permutation(
                "(AE) (BN) (CK) (DQ) (FU) (GY) (HW) (IJ) "
                        + "(LO) (MP) (RX) (SZ) (TV)", a);
        Permutation plug = new Permutation("(TD) (KC) (JZ)", a);
        MovingRotor iii = new MovingRotor("III", iiip, "A");
        MovingRotor ii = new MovingRotor("II", iip, "C");
        MovingRotor i = new MovingRotor("I", ip, "G");
        FixedRotor beta = new FixedRotor("Beta", betap);
        Reflector b = new Reflector("B", bp);
        ArrayList<Rotor> allRotors = new ArrayList<Rotor>();
        allRotors.add(iii);
        allRotors.add(ii);
        allRotors.add(i);
        allRotors.add(beta);
        allRotors.add(b);
        Machine M = new Machine(a, 5, 3, allRotors);
        String[] x = new String[5];
        x[0] = "B";
        x[1] = "Beta";
        x[2] = "I";
        x[3] = "II";
        x[4] = "III";
        M.insertRotors(x);
        M.setRotors("AAAA");
        M.setPlugboard(plug);
        assertEquals("YPVVOPSH", M.convert("AKJFGKDS"));
        assertEquals("YPVVOPSH", M.convert("NNOPTGGL"));
    }

    @Test
    public void testCase2() {
        Alphabet a = new Alphabet(
                "OPQRSTUVWXYZABCDEFGHIJKLMN");
        Permutation iiip = new Permutation(
                "(KXSGABDHPEJT) (CFLVMZOYQIRWU) ", a);
        Permutation iip = new Permutation(
                "(FIXVYAOMWQ) (CDKESLHUP) ", a);
        Permutation ip = new Permutation(
                "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", a);
        Permutation betap = new Permutation(
                "(ALBEVFCYODJ) (HIWUGNMQTZSKPRX)", a);
        Permutation bp = new Permutation(
                "(AE) (BN) (CK) (DQ) (FU) (GY) (HW) (IJ) "
                        + "(LO)", a);
        Permutation plug = new Permutation("(TD) (JAZ)", a);
        MovingRotor iii = new MovingRotor("III", iiip, "A");
        MovingRotor ii = new MovingRotor("II", iip, "C");
        MovingRotor i = new MovingRotor("I", ip, "G");
        FixedRotor beta = new FixedRotor("Beta", betap);
        Reflector b = new Reflector("B", bp);
        ArrayList<Rotor> allRotors = new ArrayList<Rotor>();
        allRotors.add(iii);
        allRotors.add(ii);
        allRotors.add(i);
        allRotors.add(beta);
        allRotors.add(b);
        Machine M = new Machine(a, 5, 3, allRotors);
        String[] x = new String[5];
        x[0] = "B";
        x[1] = "Beta";
        x[2] = "I";
        x[3] = "II";
        x[4] = "III";
        M.insertRotors(x);
        M.setRotors("BCAB");
        M.setPlugboard(plug);
        assertEquals("SRRPOGOWEZ", M.convert("HELLOWORLD"));

    }


}
