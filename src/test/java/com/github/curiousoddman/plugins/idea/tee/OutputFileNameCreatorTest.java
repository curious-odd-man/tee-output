package com.github.curiousoddman.plugins.idea.tee;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.curiousoddman.plugins.idea.tee.OutputFileNameCreator.ensurePathValid;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class OutputFileNameCreatorTest {

    public static Stream<Arguments> replaceTestsData() {
        return Stream.of(
                paths("Valid", "Valid"),
                paths("is+valid", "is+valid"),
                paths("in/valid", "in/valid"),
                paths("in:valid", "in_valid"),
                paths("in\"valid", "in_valid"),
                paths("in*valid", "in_valid"),
                paths("in?valid", "in_valid"),
                paths("in<valid", "in_valid"),
                paths("in>valid", "in_valid"),
                paths("in|valid", "in_valid"),
                paths("in\\valid", "in\\valid"),
                paths("C:\\valid\\path", "C:\\valid\\path"),
                paths("C:\\valid\\path:with:invalid:chars", "C:\\valid\\path_with_invalid_chars"),
                paths("C:/valid/path", "C:/valid/path")
        );
    }

    private static Arguments paths(String input, String expected) {
        return Arguments.of(input, expected);
    }

    @ParameterizedTest
    @MethodSource("replaceTestsData")
    void replaceUnsupportedCharactersTest(String input, String expected) {
        assertEquals(expected, ensurePathValid(input));
    }
}
