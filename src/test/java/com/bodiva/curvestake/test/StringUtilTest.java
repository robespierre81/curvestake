package com.bodiva.curvestake.test;

import com.bodiva.curvestake.StringUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StringUtilTest {

    @Test
    public void testApplySha256() {
        // Test input and expected output
        String input = "hello";
        String expectedHash = "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824";

        // Apply SHA-256 using the method
        String actualHash = StringUtil.applySha256(input);

        // Check that the generated hash matches the expected hash
        assertEquals(expectedHash, actualHash, "The SHA-256 hash should match the expected value.");
    }

    @Test
    public void testApplySha256EmptyString() {
        // Test with an empty string
        String input = "";
        String expectedHash = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";

        // Apply SHA-256 using the method
        String actualHash = StringUtil.applySha256(input);

        // Check that the generated hash matches the expected hash
        assertEquals(expectedHash, actualHash, "The SHA-256 hash for an empty string should match the expected value.");
    }

    @Test
    public void testApplySha256DifferentInputs() {
        // Test with different inputs
        String input1 = "abc";
        String input2 = "abcd";

        // Apply SHA-256 using the method
        String hash1 = StringUtil.applySha256(input1);
        String hash2 = StringUtil.applySha256(input2);

        // Ensure the hashes are different
        assertNotEquals(hash1, hash2, "SHA-256 hashes of different inputs should not be the same.");
    }

    @Test
    public void testApplySha256NullInput() {
        // Test with null input should throw an exception
        assertThrows(RuntimeException.class, () -> {
            StringUtil.applySha256(null);
        }, "Applying SHA-256 to a null input should throw a RuntimeException.");
    }
}
