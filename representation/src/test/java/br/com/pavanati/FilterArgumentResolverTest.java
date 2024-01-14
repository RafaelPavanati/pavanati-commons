package br.com.pavanati;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class FilterArgumentResolverTest {

    @InjectMocks
    private FilterArgumentResolver filterArgumentResolver;

    @Mock
    private MethodParameter methodParameter;
    @Mock
    private ModelAndViewContainer modelAndViewContainer;
    @Mock
    private NativeWebRequest nativeWebRequest;
    @Mock
    private WebDataBinderFactory webDataBinderFactory;
    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private Filter filter;
    @Mock
    private RepresentationBuilderInterface representationBuilderInterface;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenCalledSupportsParameterShouldReturnTrue() {
        // Arrange
        when(methodParameter.hasParameterAnnotation(Filter.class)).thenReturn(true);

        // Assert
        assertTrue(filterArgumentResolver.supportsParameter(methodParameter));
    }

    @Test
    void whenResolveArgumentShouldntReturnNull() throws Exception {
        // Arrange
        when(nativeWebRequest.getNativeRequest()).thenReturn(httpServletRequest);
        when(methodParameter.getParameterAnnotation(Filter.class)).thenReturn(filter);
        when(filter.representation()).thenReturn((Class) representationBuilderInterface.getClass());
        when(representationBuilderInterface.filterableData()).thenReturn(new RepresentationBuilder());
        when(filter.param()).thenReturn("param");
        when(httpServletRequest.getParameter("param")).thenReturn("value");

        // Assert
        assertNotNull(filterArgumentResolver.resolveArgument(methodParameter, modelAndViewContainer, nativeWebRequest, webDataBinderFactory));
    }

    @Test
    void whenResolveArgumentHasNullFilterShouldReturnNull() throws Exception {
        // Arrange
        when(nativeWebRequest.getNativeRequest()).thenReturn(httpServletRequest);
        when(methodParameter.getParameterAnnotation(Filter.class)).thenReturn(null);

        // Assert
        assertNull(filterArgumentResolver.resolveArgument(methodParameter, modelAndViewContainer, nativeWebRequest, webDataBinderFactory));
    }
}

