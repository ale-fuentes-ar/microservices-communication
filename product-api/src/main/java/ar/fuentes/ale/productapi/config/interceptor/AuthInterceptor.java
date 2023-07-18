package ar.fuentes.ale.productapi.config.interceptor;


import ar.fuentes.ale.productapi.config.exception.ValidationException;
import ar.fuentes.ale.productapi.modules.jwt.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

import static org.springframework.util.ObjectUtils.isEmpty;

@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION = "Authorization";
    private static final String TRANSACTION_ID = "transactionid";
    private static final String SERVICE_ID = "serviceid";

    private final JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        if (isOptions(request)) {
            return true;
        }

        if (isEmpty(request.getHeader(TRANSACTION_ID))) {
            throw new ValidationException("The transaction header is required.");
        }

        var authorization = request.getHeader(AUTHORIZATION);
        jwtService.validationAuthorization(authorization);

        request.setAttribute(SERVICE_ID, UUID.randomUUID().toString());
        return true;
    }

    private static boolean isOptions(HttpServletRequest request) {
        return HttpMethod.OPTIONS.name().equals(request.getMethod());
    }
}
