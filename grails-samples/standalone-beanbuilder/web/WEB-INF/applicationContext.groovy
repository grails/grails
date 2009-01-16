import org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping
import com.example.mvc.WelcomeController

beans {
    hello(String, "hello world")
    handlerMapping(ControllerClassNameHandlerMapping)
    welcomeController(WelcomeController)
}