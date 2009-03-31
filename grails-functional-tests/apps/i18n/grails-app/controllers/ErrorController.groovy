class ErrorController { 

     def pageNotFound = {
        [exception: request.exception]
    }

}
