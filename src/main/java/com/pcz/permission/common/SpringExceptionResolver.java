package com.pcz.permission.common;

import com.pcz.permission.exception.ParamException;
import com.pcz.permission.exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author picongzhi
 */
@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver {
    private static final String DEFAULT_MSG = "System error";

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Object handler,
                                         Exception ex) {
        String url = request.getRequestURL().toString();
        ModelAndView modelAndView;
        if (url.endsWith(".json")) {
            if (ex instanceof PermissionException || ex instanceof ParamException) {
                JsonData result = JsonData.fail(ex.getMessage());
                modelAndView = new ModelAndView("jsonView", result.toMap());
            } else {
                log.error("unknown json exception, url: ", url, ex);
                JsonData result = JsonData.fail(DEFAULT_MSG);
                modelAndView = new ModelAndView("jsonView", result.toMap());
            }
        } else if (url.endsWith(".page")) {
            log.error("unknown page exception, url: ", url, ex);
            JsonData result = JsonData.fail(DEFAULT_MSG);
            modelAndView = new ModelAndView("exception", result.toMap());
        } else {
            log.error("unknown exception, url: ", url, ex);
            JsonData result = JsonData.fail(DEFAULT_MSG);
            modelAndView = new ModelAndView("jsonView", result.toMap());
        }

        return modelAndView;
    }
}
