package enigma;

import static enigma.EnigmaException.error;

/**
 * An alphabet of encodable characters.  Provides a mapping from characters
 * to and from indices into the alphabet.
 *
 * @author Ryan Johnson
 */
class Alphabet {

    /**
     * A new alphabet containing CHARS.  Character number #k has index
     * K (numbering from 0). No character may be duplicated.
     */
    Alphabet(String chars) {
        _newAlphabet = chars;
    }

    /**
     * A default alphabet of all upper-case characters.
     */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /**
     * Returns the size of the alphabet.
     */
    int size() {
        return _newAlphabet.length();
    }

    /**
     * Returns true if CH is in this alphabet.
     */
    boolean contains(char ch) {
        for (int i = 0; i < size(); i++) {
            if (_newAlphabet.charAt(i) == ch) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns character number INDEX in the alphabet, where
     * 0 <= INDEX < size().
     */
    char toChar(int index) {
        if (index >= size() || index < 0) {
            throw error("index must be: 0 <= INDEX < size()");
        }
        return _newAlphabet.charAt(index);
    }

    /**
     * Returns the index of character CH which must be in
     * the alphabet. This is the inverse of toChar().
     */
    int toInt(char ch) {
        Integer index = null;
        for (int i = 0; i < size(); i++) {
            if (_newAlphabet.charAt(i) == ch) {
                index = i;
                break;
            }
        }
        if (index == null) {
            throw error("not in alphabet");
        }
        return (int) index;
    }

    /**
     * The alphabet.
     */
    private String _newAlphabet;

}
