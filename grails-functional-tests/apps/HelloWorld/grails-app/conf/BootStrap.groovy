class BootStrap {
    def init = { servletContext ->
        new Car(make: "Porsche", dateOfManufacture: new Date(), engineCapacity: 4200, listPrice: 78000G).save()
        new Car(make: "Renault", dateOfManufacture: new Date(), engineCapacity: 1400, listPrice: 14000G).save()
    }

    def destroy = {
    }
}
