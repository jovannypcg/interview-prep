package mx.jovannypcg.algo.p086_longestsubstringwithoutrepeating;

import java.util.HashSet;
import java.util.Set;

/**
 * Given a string {@code s}, find the length of the longest substring without duplicate
 * characters.
 *
 * <p>The answer must be a contiguous substring of {@code s}, not a subsequence.
 *
 * @see <a href="https://leetcode.com/problems/longest-substring-without-repeating-characters/">Problem Source</a>
 */
public class Solution {

    public int lengthOfLongestSubstring(String str) {
        if (str == null || str.isEmpty()) return 0;

        // Characters currently inside the [left, right) window
        Set<Character> seen = new HashSet<>();

        int left = 0,
            right = 0,
            longest = 0;

        while (right < str.length()) {
            char current = str.charAt(right);

            // No duplicate: grow the window and move on
            if (!seen.contains(current)) {
                seen.add(current);
                right++;
                continue;
            }

            // Duplicate found: the window [left, right) is as long as it'll get, record it
            longest = Math.max(longest, right - left);

            // Shrink from the left, evicting characters, until the duplicate is gone
            while (seen.contains(current)) {
                seen.remove(str.charAt(left++));
            }

            // current no longer collides with what remains in the window
            seen.add(current);
            right++;
        }

        // Final window never got a chance to be measured inside the loop
        longest = Math.max(longest, right - left);

        return longest;
    }
}
