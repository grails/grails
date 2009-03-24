class Car {
    Date dateOfManufacture
    String make
    int engineCapacity
    BigDecimal listPrice
    double mpg

    static constraints = {
        make()
        engineCapacity()
        dateOfManufacture()
        listPrice()
        mpg()
    }
}
