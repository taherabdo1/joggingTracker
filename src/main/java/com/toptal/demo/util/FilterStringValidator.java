package com.toptal.demo.util;

import java.util.Stack;

import com.toptal.demo.controllers.error.ToptalError;
import com.toptal.demo.controllers.error.ToptalException;

public class FilterStringValidator {

    public static String validateAndConvert(String input) throws ToptalException {
        if (input == null || input == "") {
            return "true";
        }
        if (!validateStrinBalance(input)) {
            throw ToptalError.JOGGING_VALIDATION_ERROR_NOT_BALANCED_FILTER_STRING.buildException();
        }
        input = input.toLowerCase();
        input = input.replaceAll("nq", "!=");
        input = input.replaceAll("eq", "=");
        input = input.replaceAll("gt", ">");
        input = input.replaceAll("lt", "<");

        return input;
    }

    private static boolean validateStrinBalance(final String input) {
        boolean result = true;
        final Stack<Character> charsStack = new Stack<>();
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '(') {
                charsStack.push(input.charAt(i));
            } else if (input.charAt(i) == ')') {
                if (charsStack.isEmpty()) {
                    return false;
                }
                charsStack.pop();
            }
        }
        if (!charsStack.isEmpty()) {
            result = false;
        }
        return result;
    }

    public static void main(final String[] args) throws ToptalException {
        System.out.println(validateAndConvert("(date eq '2016-05-01') AND ((distance gt 20) OR (distance lt 10)))"));
    }
}
