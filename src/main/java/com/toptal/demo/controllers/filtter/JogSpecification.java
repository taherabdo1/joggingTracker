package com.toptal.demo.controllers.filtter;

import java.util.List;
import java.util.Stack;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.toptal.demo.entities.Jogging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class JogSpecification{

    private SpecFilterCriteria criteria;
    
    public Predicate toPredicate(final Root<Jogging> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {
        switch (criteria.getOperation()) {
            case eq:
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case ne:
                return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case gt:
                return builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case lt:
                return builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
               
            default:
                return null;
        }

    }

    public Specification<Jogging> getJogSpecification(final List<Object> specFilterCriteria) {
        final Stack<Object> stack = new Stack<>();
        return (root, query, builder) -> {
            for (final int i = 0; i < specFilterCriteria.size();) {
                if (specFilterCriteria.get(i).getClass().equals(String.class) && specFilterCriteria.get(i) == "(") {
                    stack.push(specFilterCriteria.get(i));
                } else if (specFilterCriteria.get(i).getClass().equals(String.class) && specFilterCriteria.get(i) == ")") {
                    stack.push(specFilterCriteria.get(i));
                } else if (specFilterCriteria.get(i).getClass().equals(SpecFilterCriteria.class)) {
                    this.criteria = (SpecFilterCriteria) specFilterCriteria;
                    stack.push(toPredicate(root, query, builder));
                }
                
//                return builder.and(ands.toArray(new Predicate[ands.size()]));
            }
            query.distinct(Boolean.TRUE);
            return null;
        };
    }
}
