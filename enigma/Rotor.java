package enigma;

import static enigma.EnigmaException.*;

/**
 * Superclass that represents a rotor in the enigma machine.
 *
 * @author Ryan Johnson
 */
class Rotor {

    /**
     * A rotor named NAME whose permutation is given by PERM.
     */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _setting = 0;
        _alphabetRing = 0;
    }

    /**
     * Return my name.
     */
    String name() {
        return _name;
    }

    /**
     * Return my alphabet.
     */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /**
     * Return my permutation.
     */
    Permutation permutation() {
        return _permutation;
    }

    /**
     * Return the size of my alphabet.
     */
    int size() {
        return _permutation.size();
    }

    /**
     * Return true iff I have a ratchet and can move.
     */
    boolean rotates() {
        return false;
    }

    /**
     * Return true iff I reflect.
     */
    boolean reflecting() {
        return false;
    }

    /**
     * Return my current setting.
     */
    int setting() {
        return _setting;
    }

    /**
     * Set setting() to POSN.
     */
    void set(int posn) {
        _setting = _permutation.wrap(posn);
    }

    /**
     * Set setting() to character CPOSN.
     */
    void set(char cposn) {
        if (!alphabet().contains(cposn)) {
            throw error("Does not contain");
        }
        _setting = alphabet().toInt(cposn);
    }

    /**
     * Return the conversion of P (an integer in the range 0..size()-1)
     * according to my permutation.
     */
    int convertForward(int p) {
        int permutable = (p + _setting - _alphabetRing) % size();
        if (permutable < 0) {
            permutable += size();
        }
        int perm = (_permutation.permute(permutable)
                - _setting + _alphabetRing) % size();
        if (perm < 0) {
            perm += size();
        }
        return perm;
    }

    /**
     * Return the conversion of E (an integer in the range 0..size()-1)
     * according to the inverse of my permutation.
     */
    int convertBackward(int e) {
        int permutable = (e + _setting - _alphabetRing) % size();
        if (permutable < 0) {
            permutable += size();
        }
        int perm = (_permutation.invert(permutable)
                - _setting + _alphabetRing) % size();
        if (perm < 0) {
            perm += size();
        }
        return perm;
    }

    /**
     * Sets the alphabet ring to LETTER.
     */
    void setAlphabetRing(char letter) {
        _alphabetRing = _permutation.alphabet().toInt(letter);
    }

    /**
     * Returns true iff I am positioned to allow the rotor to my left
     * to advance.
     */
    boolean atNotch() {
        return false;
    }

    /**
     * Advance me one position, if possible. By default, does nothing.
     */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /**
     * My name.
     */
    private final String _name;

    /**
     * The permutation implemented by this rotor in its 0 position.
     */
    private Permutation _permutation;
    /**
     * My setting.
     */
    private int _setting;
    /**
     * My alphabetRing letter.
     */
    private int _alphabetRing;

}
