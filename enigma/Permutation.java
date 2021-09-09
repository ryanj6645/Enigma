package enigma;

import static enigma.EnigmaException.*;

/**
 * Represents a permutation of a range of integers starting at 0 corresponding
 * to the characters of an alphabet.
 *
 * @author Ryan Johnson
 */
class Permutation {

    /**
     * Set this Permutation to that specified by CYCLES, a string in the
     * form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     * is interpreted as a permutation in cycle notation.  Characters in the
     * alphabet that are not included in any cycle map to themselves.
     * Whitespace is ignored.
     */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        int num = 1;
        String temp = "";
        for (int i = 0; i < cycles.length(); i++) {
            if (cycles.charAt(i) == ')'
                    || cycles.charAt(i) == ',' || cycles.charAt(i) == ' ') {
                continue;
            } else {
                temp += cycles.charAt(i);
            }
        }
        for (int i = 0; i < temp.length(); i++) {
            if (temp.charAt(i) == '(' && i != 0 || i != temp.length() - 1) {
                num += 1;
            }
        }
        _cycles = new String[num];
        int holder = 0;
        for (int i = 0; i < num; i++) {
            _cycles[i] = "";
            for (; holder < temp.length(); holder++) {
                if (temp.charAt(holder) == '(') {
                    holder += 1;
                    break;
                } else {
                    _cycles[i] += temp.charAt(holder);
                }
            }
        }

    }

    /**
     * Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     * c0c1...cm.
     */
    private void addCycle(String cycle) {
        String[] temp = new String[_cycles.length + 1];
        for (int i = 0; i < temp.length - 1; i++) {
            temp[i] = _cycles[i];
        }
        String cycle2 = "";
        for (int i = 0; i < cycle.length(); i++) {
            if (cycle.charAt(i) == ' '
                    || cycle.charAt(i) == ')' || cycle.charAt(i) == ',') {
                continue;
            } else {
                cycle2 += cycle.charAt(i);
            }
        }
        temp[temp.length - 1] = cycle2;
        _cycles = temp;
    }

    /**
     * Return the value of P modulo the size of this permutation.
     */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /**
     * Returns the size of the alphabet I permute.
     */
    int size() {
        return _alphabet.size();
    }

    /**
     * Return the result of applying this permutation to P modulo the
     * alphabet size.
     */
    int permute(int p) {
        int num = wrap(p);
        char temp;
        for (String s : _cycles) {
            for (int i = 0; i < s.length(); i++) {
                if (_alphabet.toChar(num)
                        == s.charAt(i) && i != s.length() - 1) {
                    temp = s.charAt(i + 1);
                    return _alphabet.toInt(temp);
                } else if (_alphabet.toChar(num)
                        == s.charAt(i) && i == s.length() - 1) {
                    temp = s.charAt(0);
                    return _alphabet.toInt(temp);
                }
            }
        }
        return p;
    }

    /**
     * Return the result of applying the inverse of this permutation
     * to  C modulo the alphabet size.
     */
    int invert(int c) {
        int num = wrap(c);
        char temp;
        for (String s : _cycles) {
            for (int i = 0; i < s.length(); i++) {
                if (_alphabet.toChar(num) == s.charAt(i) && i != 0) {
                    temp = s.charAt(i - 1);
                    return _alphabet.toInt(temp);
                } else if (_alphabet.toChar(num) == s.charAt(i) && i == 0) {
                    temp = s.charAt(s.length() - 1);
                    return _alphabet.toInt(temp);
                }
            }
        }
        return c;
    }

    /**
     * Return the result of applying this permutation to the index of P
     * in ALPHABET, and converting the result to a character of ALPHABET.
     */
    char permute(char p) {
        return _alphabet.toChar(permute(_alphabet.toInt(p)));
    }

    /**
     * Return the result of applying the inverse of this permutation to C.
     */
    char invert(char c) {
        return _alphabet.toChar(invert(_alphabet.toInt(c)));
    }

    /**
     * Return the alphabet used to initialize this Permutation.
     */
    Alphabet alphabet() {
        return _alphabet;
    }

    /**
     * Return true iff this permutation is a derangement (i.e., a
     * permutation for which no value maps to itself).
     */
    boolean derangement() {
        int num = 0;
        for (String s : _cycles) {
            num += s.length();
            if (s.length() == 1) {
                return false;
            }
        }
        if (num != alphabet().size()) {
            return false;
        }
        return true;
    }

    /**
     * Alphabet of this permutation.
     */
    private Alphabet _alphabet;
    /**
     * Array of cycles.
     */
    private String[] _cycles;
}
