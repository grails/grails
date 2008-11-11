class Car {
    Date dateOfManufacture
    String make
    int engineCapacity
    BigDecimal listPrice

    static constraints = {
        make()
        engineCapacity()
        dateOfManufacture()
        listPrice()
    }
}
