class BootStrap {
    def init = { servletContext ->
        new Car(make: "Porsche", dateOfManufacture: new Date(), engineCapacity: 4200, listPrice: 78000G).save()
        new Car(make: "Renault", dateOfManufacture: new Date(), engineCapacity: 1400, listPrice: 14000G).save()
        new Car(make: "GM", dateOfManufacture: new Date(), engineCapacity: 2000, listPrice: 16000G).save()
        new Car(make: "BMW", dateOfManufacture: new Date(), engineCapacity: 3200, listPrice: 54000G).save()
        new Car(make: "Toyota", dateOfManufacture: new Date(), engineCapacity: 1100, listPrice: 7000G).save()
    }

    def destroy = {
    }
}
