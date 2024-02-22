package br.com.bradesco.kit.srv.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;

/**
 * Conversor resolver Jackson para tratamento caixa baixa para o nome de uma classe
 */
public class CaixaBaixaClassResolverConfig extends TypeIdResolverBase {

    @Override
    public String idFromValue(Object value) {
        return value.getClass().getSimpleName().toLowerCase();
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        return idFromValue(value);
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }
}

