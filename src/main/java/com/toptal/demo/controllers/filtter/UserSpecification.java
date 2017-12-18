package com.toptal.demo.controllers.filtter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import com.toptal.demo.controllers.error.ToptalError;
import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.entities.User;

public class UserSpecification {

    public static Specification<User> getJogSpecification(final List<Object> specFilterCriteria) throws ToptalException {
        List<Object> stack = new ArrayList();
        for (int i = 0; i < specFilterCriteria.size(); i++) {
            if (specFilterCriteria.get(i).getClass() == SpecFilterCriteria.class) {
                final SpecFilterCriteria criteria = (SpecFilterCriteria) specFilterCriteria.get(i);
                try {
                    stack.add(getSpecification(criteria));
                } catch (final Exception e) {
                    ToptalError.INCORRECT_FILTER_CRITERIA.buildException();
                }
            } else if (specFilterCriteria.get(i) instanceof Character) {
                stack.add(specFilterCriteria.get(i));
            } else if (specFilterCriteria.get(i) instanceof String) {
                stack.add(specFilterCriteria.get(i));
            }

        }
        while (stack.size() > 1) {
            stack = removeParenthises(stack);
            stack = constructLogicalPredicates(stack);

        }
        return (Specification) stack.get(0);
    }

    public static Specification<User> getSpecification(final SpecFilterCriteria criteria) {
        return new Specification<User>() {

            @Override
            public Predicate toPredicate(final Root<User> root, final CriteriaQuery<?> query, final CriteriaBuilder cb) {
                query.distinct(Boolean.TRUE);
                if (((String) criteria.getValue()).equalsIgnoreCase("true")) {
                    criteria.setValue(true);
                } else if (((String) criteria.getValue()).equalsIgnoreCase("false")) {
                    criteria.setValue(false);
                }
                switch (criteria.getOperation()) {
                    case eq:
                        return cb.equal(root.get(criteria.getKey()), criteria.getValue());
                    case ne:
                        return cb.notEqual(root.get(criteria.getKey()), criteria.getValue());
                    case gt:
                        return cb.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
                    case lt:
                        return cb.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
                    default:
                        return null;
                }

            }
        };
    }

    private static List<Object> removeParenthises(final List<Object> input) {
        final List<Object> output = new ArrayList<>();
        for (int i = 0; i < input.size();) {
            if (input.get(i) instanceof Character && (char) input.get(i) == '(') {
                // '(' SPEC ')'
                if (input.get(i + 1) instanceof Specification && input.get(i + 2) instanceof Character && (char) input.get(i + 2) == ')') {
                    output.add(input.get(i + 1));
                    i += 3;
                } else {// outer '('
                    output.add(input.get(i));
                    i++;
                }
            } else {// OR/AND
                output.add(input.get(i));
                i++;
            }
        }
        return output;
    }

    private static List<Object> constructLogicalPredicates(final List<Object> input) throws ToptalException {
        final List<Object> output = new ArrayList<>();
        // starts like (.... --> the parenthisise
        if (!(input.get(0) instanceof Specification)) {
            output.add(input.get(0));
        }
        for (int i = 0; i < input.size();) {
            if (input.get(i) instanceof String) {
                // this is a AND/OR operation
                if (input.get(i - 1) instanceof Specification && input.get(i + 1) instanceof Specification) {
                    if (((String) input.get(i)).equalsIgnoreCase("AND")) {
                        final Specification and = Specifications.where((Specification) input.get(i - 1)).and((Specification) input.get(i + 1));
                        input.remove(i - 1);
                        input.remove(i - 1);
                        input.remove(i - 1);
                        input.add(i - 1, and);
                    } else if (((String) input.get(i)).equalsIgnoreCase("OR")) {
                        final Specification or = Specifications.where((Specification) input.get(i - 1)).or((Specification) input.get(i + 1));
                        input.remove(i - 1);
                        input.remove(i - 1);
                        input.remove(i - 1);
                        input.add(i - 1, or);
                    }
                } else {
                    i++;
                }
            } else {
                i++;
            }
        }
        return input;
    }

}
