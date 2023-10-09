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
    

    return error;
}

export default FormValidation;