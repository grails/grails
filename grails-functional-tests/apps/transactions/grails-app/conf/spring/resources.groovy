// Place your Spring DSL code here
beans = {
    javaService(JavaService) { bean -> bean.autowire = true }
}