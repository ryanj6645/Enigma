package enigma;

import java.util.Collection;
import java.util.ArrayList;

import static enigma.EnigmaException.*;

/**
 * Class that represents a complete enigma machine.
 *
 * @author Ryan Johnson
 */
class Machine {

    /**
     * A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     * and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     * available rotors.
     */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = new ArrayList<Rotor>(allRotors);
        _rotors = new ArrayList<Rotor>();
    }

    /**
     * Return the number of rotor slots I have.
     */
    int numRotors() {
        return _numRotors;
    }

    /**
     * Return the number pawls (and thus rotating rotors) I have.
     */
    int numPawls() {
        return _pawls;
    }

    /**
     * Set my rotor slots to the rotors named ROTORS from my set of
     * available rotors (ROTORS[0] names the reflector).
     * Initially, all rotors are set at their 0 setting.
     */
    void insertRotors(String[] rotors) {
        _rotors = new ArrayList<Rotor>();
        for (int i = 0; i < rotors.length; i++) {
            for (int j = 0; j < _allRotors.size(); j++) {
                if (rotors[i].equals(_allRotors.get(j).name())) {
                    _rotors.add(_allRotors.get(j));
                }
            }
        }
    }

    /**
     * Set my rotors according to SETTING, which must be a string of
     * numRotors()-1 characters in my alphabet. The first letter refers
     * to the leftmost rotor setting (not counting the reflector).
     */
    void setRotors(String setting) {
        for (int i = 0; i < setting.length(); i++) {
            _rotors.get(i + 1).set(setting.charAt(i));
        }
        int temporary = 0;
        for (Rotor rot : _rotors) {
            if (rot instanceof MovingRotor) {
                temporary += 1;
            }
        }
        if (_pawls != temporary) {
            throw error("Wrong number of moving rotors");
        }
    }

    /**
     * Set the plugboard to PLUGBOARD.
     */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /**
     * Returns the result of converting the input character C (as an
     * index in the range 0..alphabet size - 1), after first advancing
     * the machine.
     */
    int convert(int c) {
        int[] check = new int[_rotors.size()];
        for (int i = 0; i < check.length; i++) {
            check[i] = 0;
        }
        check[_rotors.size() - 1] = 1;
        for (int i = _rotors.size() - 1; i > 0; i--) {
            if (_rotors.get(i).atNotch() && _rotors.get(i - 1).rotates()) {
                check[i - 1] = 1;
                check[i] = 1;
            }
        }
        for (int i = 0; i < _rotors.size(); i++) {
            if (check[i] == 1) {
                _rotors.get(i).advance();
            }
        }
        int permutated = _plugboard.permute(c);
        int count = _rotors.size() - 1;
        while (count > 0) {
            permutated = _rotors.get(count).convertForward(permutated);
            count -= 1;
        }
        for (Rotor rot : _rotors) {
            permutated = rot.convertBackward(permutated);
        }
        int answer = _plugboard.permute(permutated);
        return answer;
    }

    /**
     * Takes the string ALPHA and sets the alphabet ring
     * accordingly.
     */
    void setMachineAlphabet(String alpha) {
        for (int i = 0; i < alpha.length(); i++) {
            _rotors.get(i + 1).setAlphabetRing(alpha.charAt(i));
        }
    }

    /**
     * Returns the encoding/decoding of MSG, updating the state of
     * the rotors accordingly.
     */
    String convert(String msg) {
        String temp = "";
        for (int i = 0; i < msg.length(); i++) {
            temp += _alphabet.toChar(convert(_alphabet.toInt(msg.charAt(i))));
        }
        return temp;
    }

    /**
     * Common alphabet of my rotors.
     */
    private final Alphabet _alphabet;
    /**
     * Number of rotors.
     */
    private int _numRotors;
    /**
     * Number of pawls.
     */
    private int _pawls;
    /**
     * List of all Rotors.
     */
    private ArrayList<Rotor> _allRotors;
    /**
     * List of rotors in order.
     */
    private ArrayList<Rotor> _rotors;
    /**
     * The plugboard.
     */
    private Permutation _plugboard;
}
