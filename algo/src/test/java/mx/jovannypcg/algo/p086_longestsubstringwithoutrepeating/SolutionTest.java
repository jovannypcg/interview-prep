package mx.jovannypcg.algo.p086_longestsubstringwithoutrepeating;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SolutionTest {
    private final Solution solution = new Solution();

    @Test
    @DisplayName("classic repeating pattern → longest run is the first non-repeating prefix")
    void classicRepeatingPattern() {
        assertThat(solution.lengthOfLongestSubstring("abcabcbb")).isEqualTo(3);
    }

    @Test
    @DisplayName("all identical characters → longest substring is a single character")
    void allIdenticalCharacters() {
        assertThat(solution.lengthOfLongestSubstring("bbbbb")).isEqualTo(1);
    }

    @Test
    @DisplayName("window must reset mid-string → answer is a substring, not a subsequence")
    void windowResetsMidString() {
        assertThat(solution.lengthOfLongestSubstring("pwwkew")).isEqualTo(3);
    }

    @Test
    @DisplayName("empty string → no characters, returns 0")
    void emptyString() {
        assertThat(solution.lengthOfLongestSubstring("")).isEqualTo(0);
    }

    @Test
    @DisplayName("single space character → substring of length 1")
    void singleSpaceCharacter() {
        assertThat(solution.lengthOfLongestSubstring(" ")).isEqualTo(1);
    }

    @Test
    @DisplayName("single character string → substring of length 1")
    void singleCharacterString() {
        assertThat(solution.lengthOfLongestSubstring("a")).isEqualTo(1);
    }

    @Test
    @DisplayName("duplicate falls outside the current window → left pointer must not jump backwards")
    void duplicateOutsideCurrentWindow() {
        assertThat(solution.lengthOfLongestSubstring("dvdf")).isEqualTo(3);
    }

    @Test
    @DisplayName("classic stale-index trap → left pointer must use max(), not reset to a stale index")
    void staleIndexTrap() {
        assertThat(solution.lengthOfLongestSubstring("abba")).isEqualTo(2);
    }

    @Test
    @DisplayName("all unique characters → entire string is the longest substring")
    void allUniqueCharacters() {
        assertThat(solution.lengthOfLongestSubstring("abcdef")).isEqualTo(6);
    }

    @Test
    @DisplayName("digits and symbols mixed in → treated as ordinary characters")
    void digitsAndSymbolsMixed() {
        assertThat(solution.lengthOfLongestSubstring("a1!a2@a3#")).isEqualTo(5);
    }

    @Test
    @DisplayName("repeated spaces surrounded by letters → spaces count as duplicate characters too")
    void repeatedSpacesAmongLetters() {
        assertThat(solution.lengthOfLongestSubstring("a b c b")).isEqualTo(3);
    }

    @Test
    @DisplayName("longest window is at the very end of the string")
    void longestWindowAtEnd() {
        assertThat(solution.lengthOfLongestSubstring("aab")).isEqualTo(2);
    }

    @Test
    @DisplayName("large input at the upper length boundary (10^5 chars), all unique via cycling ASCII range")
    void largeInputAtLengthBoundary() {
        int n = 100_000;
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            sb.append((char) ('a' + (i % 26)));
        }

        assertThat(solution.lengthOfLongestSubstring(sb.toString())).isEqualTo(26);
    }
}
