package com.three.dwms.common;

import com.three.dwms.beans.JsonData;
import com.three.dwms.exception.ParamException;
import com.three.dwms.exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by csw on 2017/12/9.
 * Description:
 */
@Slf4j
public class ExceptionResolver  implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String url = request.getRequestURL().toString();
        ModelAndView mv;
        String defaultMsg = "System error";

        if (ex instanceof PermissionException || ex instanceof ParamException
                || ex instanceof NullPointerException) {
            JsonData result = JsonData.fail(ex.getMessage());
            mv = new ModelAndView("jsonView", result.toMap());
        } else {
            log.error("unknown json exception, url:" + url, ex);
            JsonData result = JsonData.fail(defaultMsg);
            mv = new ModelAndView("jsonView", result.toMap());
        }

        return mv;
    }
}
