package com.toptal.demo.controllers.filtter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CriteriaParser {

    public static List<Object> parse(String filtterString) {
        final List<String> comparisonOperators = new ArrayList<String>();
        comparisonOperators.add("eq");
        comparisonOperators.add("nq");
        comparisonOperators.add("gt");
        comparisonOperators.add("lt");

        Pattern.compile("^\\((\\w+)\\s*(eq|ne|gt|lt)\\s*(\\p{Punct}?)([\\w\\W]+)(\\p{Punct}?)\\)\\s*$");

        // String filtterString = "(date eq '2016-05-01') AND ((distance gt 20) OR (distance lt 10))";
        filtterString = filtterString.replace("'", "");
        final List<Object> tokens = new ArrayList<Object>();

        for (int i = 0; i < filtterString.length();) {
            if (filtterString.charAt(i) == '(') {
                tokens.add('(');
                i++;
            } else if (filtterString.charAt(i) == ')') {
                tokens.add(')');
                i++;
            } else {
                final int upper = nextStop(i + 1, filtterString);
                final String current = filtterString.substring(i, upper);
                i = upper;
                if (current.matches("\\s") || current == null || current.equals(null)) {// it was a space
                    continue;
                }
                tokens.add(current.trim());
            }

        }

        final List<Object> specs = new ArrayList<>();

        for (int i = 0; i < tokens.size() - 1;) {
            if (comparisonOperators.contains(tokens.get(i + 1))) {
                specs.add(new SpecFilterCriteria((String) tokens.get(i), RsqlSearchOperation.valueOf((String) tokens.get(i + 1)), tokens.get(i + 2)));
                i += 3;
            } else {
                specs.add(tokens.get(i));
                i++;
            }
            System.out.println(specs);
        }
        // add the last ")"
        if (tokens.get(tokens.size() - 1) instanceof Character && (char) tokens.get(tokens.size() - 1) == ')') {
            specs.add(tokens.get(tokens.size() - 1));
        }

        return specs;

    }

    private static int nextStop(final int i, final String str) {

        final int openParen = str.indexOf('(', i) < 0 ? Integer.MAX_VALUE : str.indexOf('(', i);
        final int closeParen = str.indexOf(')', i) < 0 ? Integer.MAX_VALUE : str.indexOf(')', i);
        final int space = str.indexOf(' ', i) < 0 ? Integer.MAX_VALUE : str.indexOf(' ', i);
        return Math.min(Math.min(openParen, closeParen), space);

    }
}
