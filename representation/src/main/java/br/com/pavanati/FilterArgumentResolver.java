package br.com.pavanati;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Primary;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

@Primary
@Component
public class FilterArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(Filter.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();
        Filter filter = methodParameter.getParameterAnnotation(Filter.class);

        if (Objects.isNull(filter)) {
            return null;
        }

        String filterParam = request.getParameter(filter.param());
        RepresentationBuilderInterface representation = filter.representation().newInstance();

        return RepresentationResolver.resolver(representation.filterableData(), filterParam);
    }
}
