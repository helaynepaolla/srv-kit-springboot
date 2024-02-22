package br.com.bradesco.kit.srv.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Essa classe é um exemplo de um loggerInteceptor para auxiliar em um caso muito muito especifico, de forma geral seu uso é restrito.
 * A ideia é que voce possa logar os parametros q estão chegando no metodo alterando a variveis de ambiente do tipo 'loglevel'.
 * Com isso esse interceptor ficaria parecido com o access.log de um nginx (que é mais aconselhavel o uso por ser mais performatico fazer isso lá).
 * <p>
 * https://www.baeldung.com/spring-mvc-handlerinterceptor-vs-filter
 * https://www.baeldung.com/spring-mvc-handlerinterceptor
 */
public class LoggerInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoggerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (log.isTraceEnabled()) {
            log.trace("Antes de chamar o controller");
            log.trace("[preHandle][{}] {}", request.getMethod(), request.getRequestURI());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView)
            throws Exception {
        if (log.isTraceEnabled()) {
            log.trace("Logo apos o processamento no controller...");
            log.trace("[postHandle] response_http_code [{}]", response.getStatus());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        if (ex != null && log.isTraceEnabled()) {
            log.trace("Apos a requisição ser finalizada, ultima barreira antes do retorno ao usuário");
            log.trace("[afterCompletion][{}][exception: {}]", handler, ex);
        }
    }
}
