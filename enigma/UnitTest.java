package enigma;


import ucb.junit.textui;
/**
 * import org.junit.Test;
* import static org.junit.Assert.assertEquals;
* import static org.junit.Assert.assertTrue;
*/
/** The suite of all JUnit tests for the enigma package.
 *  @author Ryan Johnson
 */
public class UnitTest {

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(PermutationTest.class,
                MovingRotorTest.class, RotorTest.class, MachineTest.class));
    }
}


