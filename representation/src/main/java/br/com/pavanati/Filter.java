package br.com.pavanati;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Filter {

    Class<? extends RepresentationBuilderInterface> representation();

    String param() default "filter";
}
