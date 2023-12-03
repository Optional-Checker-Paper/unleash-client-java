package io.getunleash.strategy.constraints;

import io.getunleash.Constraint;
import io.getunleash.UnleashContext;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class StringConstraintOperator implements ConstraintOperator {
    private Locale comparisonLocale;

    public StringConstraintOperator(Locale comparisonLocale) {
        this.comparisonLocale = comparisonLocale;
    }

    @Override
    public boolean evaluate(Constraint constraint, UnleashContext context) {
        List<String> values = constraint.getValues();
        Optional<String> contextValue = context.getByName(constraint.getContextName());
        boolean caseInsensitive = constraint.isCaseInsensitive();
        switch (constraint.getOperator()) {
            case IN:
                return isIn(values, contextValue, caseInsensitive);
            case NOT_IN:
                return !isIn(values, contextValue, caseInsensitive);
            case STR_CONTAINS:
                return contains(values, contextValue, caseInsensitive);
            case STR_STARTS_WITH:
                return startsWith(values, contextValue, caseInsensitive);
            case STR_ENDS_WITH:
                return endsWith(values, contextValue, caseInsensitive);
            default:
                return false;
        }
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType") // intellij-suppression-optional-used-as-parameter
    private boolean endsWith(
            List<String> values, Optional<String> contextValue, boolean caseInsensitive) {
        return contextValue
                .map(
                        c ->
                                values.stream()
                                        .anyMatch(
                                                v -> {
                                                    if (caseInsensitive) {
                                                        return c.toLowerCase(comparisonLocale)
                                                                .endsWith(
                                                                        v.toLowerCase(
                                                                                comparisonLocale));
                                                    } else {
                                                        return c.endsWith(v);
                                                    }
                                                }))
                .orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType") // intellij-suppression-optional-used-as-parameter
    private boolean startsWith(
            List<String> values, Optional<String> contextValue, boolean caseInsensitive) {
        return contextValue
                .map(
                        c ->
                                values.stream()
                                        .anyMatch(
                                                v -> {
                                                    if (caseInsensitive) {
                                                        return v.toLowerCase(comparisonLocale)
                                                                .startsWith(
                                                                        c.toLowerCase(
                                                                                comparisonLocale));
                                                    } else {
                                                        return c.startsWith(v);
                                                    }
                                                }))
                .orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType") // intellij-suppression-optional-used-as-field
    private boolean contains(
            List<String> values, Optional<String> contextValue, boolean caseInsensitive) {
        return contextValue
                .map(
                        c ->
                                values.stream()
                                        .anyMatch(
                                                v -> {
                                                    if (caseInsensitive) {
                                                        return c.toLowerCase(comparisonLocale)
                                                                .contains(
                                                                        v.toLowerCase(
                                                                                comparisonLocale));
                                                    } else {
                                                        return c.contains(v);
                                                    }
                                                }))
                .orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType") // intellij-suppression-optional-used-as-field
    private boolean isIn(List<String> values, Optional<String> value, boolean caseInsensitive) {
        return value.map(
                        v ->
                                values.stream()
                                        .anyMatch(
                                                c -> {
                                                    if (caseInsensitive) {
                                                        return c.equalsIgnoreCase(v);
                                                    } else {
                                                        return c.equals(v);
                                                    }
                                                }))
                .orElse(false);
    }
}
