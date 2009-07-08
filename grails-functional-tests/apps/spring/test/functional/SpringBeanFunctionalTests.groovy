class SpringBeanFunctionalTests extends functionaltestplugin.FunctionalTestCase {
	
    void testOverrideExistingSpringBean() {
        get('/test/testOverridenBean')
        assertStatus 200
        assertContentContains 'Resolver class = org.springframework.web.servlet.i18n.FixedLocaleResolver'
    }

	void testOverridingExistingSpringBeanXml() {
        get('/test/testOverridenBeanXml')
        assertStatus 200
        assertContentContains 'MessageSource class = org.springframework.context.support.StaticMessageSource'
		
	}

	void testUseSpringBeanAlias() {
        get('/test/testSpringBeanAlias')
        assertStatus 200
        assertContentContains 'Aliased bean = org.springframework.context.support.StaticMessageSource'
		
	}
	
	void testUseSpringNamespaceConfig() {
        get('/test/testNamespaceConfig')
        assertStatus 200
        assertContentContains 'Component class = beans.TestComponent'
		
	}
	
	void testUseSpringRequestScope() {
        get('/test/testRequestScopedBean')
        assertStatus 200
        assertContentContains 'Scoped bean = true'		
	}
	
    void testLookupServletContextA() {
        get('/test/testAppCtxInServletContextA')
        assertStatus 200
        assertContentContains 'Resolver class = org.springframework.web.servlet.i18n.FixedLocaleResolver'
    }

    void testLookupServletContextB() {
        get('/test/testAppCtxInServletContextB')
        assertStatus 200
        assertContentContains 'Resolver class = org.springframework.web.servlet.i18n.FixedLocaleResolver'
    }
	
}
