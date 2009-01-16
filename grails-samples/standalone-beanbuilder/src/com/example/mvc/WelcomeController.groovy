package com.example.mvc

import org.springframework.web.servlet.mvc.Controller
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.context.ApplicationContextAware
import org.springframework.context.ApplicationContext

/**
 * @author Graeme Rocher
 * @since 1.0
 * 
 * Created: Jan 16, 2009
 */

public class WelcomeController implements Controller, ApplicationContextAware{

    ApplicationContext applicationContext

    ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        httpServletResponse.writer.write "The message is ${applicationContext.getBean('hello')}"
        httpServletResponse.writer.flush()
        return null
    }

}