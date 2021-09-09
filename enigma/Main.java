package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.error;

/**
 * Enigma simulator.
 *
 * @author Ryan Johnson
 */
public final class Main {

    /**
     * Process a sequence of encryptions and decryptions, as
     * specified by ARGS, where 1 <= ARGS.length <= 3.
     * ARGS[0] is the name of a configuration file.
     * ARGS[1] is optional; when present, it names an input file
     * containing messages.  Otherwise, input comes from the standard
     * input.  ARGS[2] is optional; when present, it names an output
     * file for processed messages.  Otherwise, output goes to the
     * standard output. Exits normally if there are no errors in the input;
     * otherwise with code 1.
     */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /**
     * Check ARGS and open the necessary files (see comment on main).
     */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /**
     * Return a Scanner reading from the file named NAME.
     */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /**
     * Return a PrintStream writing to the file named NAME.
     */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /**
     * Configure an Enigma machine from the contents of configuration
     * file _config and apply it to the messages in _input, sending the
     * results to _output.
     */
    private void process() {
        Machine enigmaMachine = readConfig();
        if (_input.hasNext("\\*")) {
            String temp = _input.nextLine();
            Scanner second = new Scanner(temp);
            while (second.hasNext() && second.hasNext("\\*")) {
                String[] newRotors = new String[_numRotors];
                helper(second, newRotors, _numRotors);
                enigmaMachine.insertRotors(newRotors);
                if (!second.hasNext()) {
                    throw error("No line to encode");
                }
                String settings = second.next();
                setUp(enigmaMachine, settings);
                if (second.hasNext() && !second.hasNext("\\(?.+\\)")) {
                    enigmaMachine.setMachineAlphabet(second.next());
                }
                String cycles = "";
                while (second.hasNext("\\(?.+\\)")) {
                    cycles += second.next();
                }
                Permutation perm = new Permutation(cycles, _alphabet);
                enigmaMachine.setPlugboard(perm);
                if (!_input.hasNext()) {
                    break;
                }
                String x = removeWhite(_input.nextLine());
                if (x.equals("")) {
                    _output.println();
                }
                if (x.contains("*")) {
                    break;
                }
                while (_input.hasNext() && !_input.hasNext("\\*")) {
                    String answer = enigmaMachine.convert(x);
                    printMessageLine(answer);
                    if (_input.hasNext("\\*")) {
                        break;
                    }
                    x = removeWhite(_input.nextLine());
                }
                String answer = enigmaMachine.convert(x);
                printMessageLine(answer);
                if (_input.hasNextLine()) {
                    temp = _input.nextLine();
                    while (temp.isEmpty()) {
                        _output.println();
                        if (_input.hasNextLine()) {
                            temp = _input.nextLine();
                        } else {
                            break;
                        }
                    }
                    second = new Scanner(temp);
                }
            }
        } else {
            throw error("input has wrong format");
        }
    }

    /**
     * Return a string that is STR with no spaces.
     */
    String removeWhite(String str) {
        String changer = "";
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ') {
                continue;
            } else {
                changer += str.charAt(i);
            }
        }
        return changer;
    }

    /**
     * Helper function taking in SECOND, NEWROTORS, and NUMROTORS.
     */
    void helper(Scanner second, String[] newRotors, int numRotors) {
        second.next();
        for (int i = 0; i < numRotors; i++) {
            newRotors[i] = second.next();
        }
    }

    /**
     * Return an Enigma machine configured from the contents of configuration
     * file _config.
     */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.next());
            int numrotors = _config.nextInt();
            _numRotors = numrotors;
            int pawls = _config.nextInt();
            _numPawls = pawls;
            ArrayList<Rotor> allRotors = new ArrayList<Rotor>();
            while (_config.hasNext()) {
                allRotors.add(readRotor());
            }
            return new Machine(_alphabet, numrotors, pawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /**
     * Return a rotor, reading its description from _config.
     */
    private Rotor readRotor() {
        try {
            String name = _config.next();
            String middle = _config.next();
            String cycles = "";
            while (_config.hasNext("\\(?.+\\)")) {
                cycles += _config.next();
            }
            if (middle.charAt(0) == 'M') {
                String notches = middle.substring(1);
                return new MovingRotor(name,
                        new Permutation(cycles, _alphabet), notches);
            } else if (middle.charAt(0) == 'N') {
                return new FixedRotor(name, new Permutation(cycles, _alphabet));
            } else if (middle.charAt(0) == 'R') {
                return new Reflector(name, new Permutation(cycles, _alphabet));
            } else {
                throw error("Wrong Format");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /**
     * Set M according to the specification given on SETTINGS,
     * which must have the format specified in the assignment.
     */
    private void setUp(Machine M, String settings) {
        M.setRotors(settings);
    }

    /**
     * Print MSG in groups of five (except that the last group may
     * have fewer letters).
     */
    private void printMessageLine(String msg) {
        String temp = "";
        for (int i = 0; i < msg.length(); i++) {
            temp += msg.charAt(i);
            if (temp.length() == 5) {
                _output.print(temp);
                temp = "";
                if (i == msg.length() - 1) {
                    _output.println();
                } else {
                    _output.print(" ");
                }
            } else if (i == msg.length() - 1) {
                _output.print(temp);
                _output.println();
            }
        }
    }

    /**
     * Alphabet used in this machine.
     */
    private Alphabet _alphabet;

    /**
     * Source of input messages.
     */
    private Scanner _input;

    /**
     * Source of machine configuration.
     */
    private Scanner _config;

    /**
     * File for encoded/decoded messages.
     */
    private PrintStream _output;

    /**
     * Number of rotors.
     */
    private int _numRotors;

    /**
     * Number of pawls.
     */
    private int _numPawls;
}
