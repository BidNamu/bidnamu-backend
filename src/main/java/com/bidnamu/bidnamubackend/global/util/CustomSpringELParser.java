package com.bidnamu.bidnamubackend.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomSpringELParser {

    public static Object getDynamicValue(
        final String[] parameterNames,
        final Object[] args,
        final String key) {
        final ExpressionParser parser = new SpelExpressionParser();
        final StandardEvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        return parser.parseExpression(key).getValue(context, Object.class);
    }
}
