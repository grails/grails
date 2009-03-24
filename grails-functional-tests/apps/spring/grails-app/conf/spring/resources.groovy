import org.springframework.web.servlet.i18n.FixedLocaleResolver
beans = {
	xmlns context:"http://www.springframework.org/schema/context"
	
	context.'component-scan'( 'base-package' :"beans" )
	
    localeResolver(FixedLocaleResolver,new Locale("th", "TH"))

	
	requestScopedBean(beans.ScopedBean) { bean ->
		bean.scope = "request"
	}
}