package io.hybridtheory.mutalisk.common.mapper.annotation;


// if you does run javac with -g flag to enable method parameter name record, you may ignore these annotation
// otherwise please add this one to enable name auto mapping
// see : https://stackoverflow.com/questions/6759880/getting-the-name-of-a-method-parameter/37021110#37021110

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticSearchParam {
    String name();
}
