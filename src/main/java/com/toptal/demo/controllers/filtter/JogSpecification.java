package com.toptal.demo.controllers.filtter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import com.toptal.demo.controllers.error.ToptalError;
import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.entities.Jogging;

public class JogSpecification {

    public static Specification<Jogging> getJogSpecification(final List<Object> specFilterCriteria) throws ToptalException {
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

            // return builder.and(ands.toArray(new Predicate[ands.size()]));

        }
        while (stack.size() > 1) {
            stack = removeParenthises(stack);
            stack = constructLogicalPredicates(stack);

        }
        return (Specification) stack.get(0);
    }

    public static Specification<Jogging> getSpecification(final SpecFilterCriteria criteria) {
        return new Specification<Jogging>() {

            @Override
            public Predicate toPredicate(final Root<Jogging> root, final CriteriaQuery<?> query, final CriteriaBuilder cb) {
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                if (criteria.getKey().equalsIgnoreCase("date")) {
                    // Path<Date> expiryDate = accounts.<Date> get("expiryDate");

                    try {
                        final Date startDate = simpleDateFormat.parse((String) criteria.getValue());
                        final Calendar calendar = Calendar.getInstance();
                        calendar.setTime(startDate);
                        calendar.add(Calendar.DATE, 1);
                        final Date endDate = calendar.getTime();
                        return cb.between(root.get("date"), startDate, endDate);// (root.get(criteria.getKey()),
                                                                                            // criteria.getValue());

                    } catch (final ParseException e) {
                        e.printStackTrace();
                    }
                }
                query.distinct(Boolean.TRUE);
                switch (criteria.getOperation()) {
                    case eq:
                        if (criteria.getKey().equalsIgnoreCase("date")) {
                            try {
                                return cb.equal(root.get(criteria.getKey()), simpleDateFormat.parse((String) criteria.getValue()));
                            } catch (final ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
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
                    // // the last ')' is not added
                    // if (i >= input.size()) {
                    // output.add(')');
                    // }
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
        // if(input.size() == 1){
        // output.add(input.get(0));
        // }else if (input.get(0) instanceof Specification && input.get(1) instanceof String) {
        // output.add(input.get(0));
        // }
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
