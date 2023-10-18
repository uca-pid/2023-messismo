function FormValidation(values) {
    let error = {}
    const priceRegex = /[-+]?\d*\.?\d+/g
    const stockRegex = /[-+]?\d*\.?\d+/g 
    const emailRegex = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i


    if (values.price === "") {
        error.price = "Field is empty"
    }

    else if (!priceRegex.test(values.price)) {
        error.price = "Invalid price"
    }
    
    else if (values.price < 0){
        error.price = "Invalid price"
    }
    
    if(values.name === "" ) {
        error.name = "Field is empty"
    } 

    if(values.category === "" ) {
        error.category = "Field is empty"
    } 

    if (values.stock === "") {
        error.stock = "Field is empty"
    }

    else if (!stockRegex.test(values.stock)) {
        error.stock = "Invalid stock"
    }

    else if (values.stock < 0) {
        error.stock = "Invalid stock"
    }
    
    if (values.cost === "") {
        error.cost = "Field is empty"
    }

    else if (!priceRegex.test(values.cost)) {
        error.cost = "Invalid cost"
    }
    
    else if (values.cost < 0){
        error.cost = "Invalid cost"
    }
    
    return error;
}

export default FormValidation;