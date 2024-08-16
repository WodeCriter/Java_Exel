package exel.engine.expressions.api;

import exel.engine.effectivevalue.api.EffectiveValue;

public interface Expression {
    EffectiveValue eval();
}
