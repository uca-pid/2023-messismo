function EditFormValidation(values) {
    let error = {}
    const priceRegex = /[-+]?\d*\.?\d+/g
    const stockRegex = /[-+]?\d*\.?\d+/g 
    const emailRegex = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i


    if (values.price === "") {
        
    }

    else if (!priceRegex.test(values.price)) {
        error.price = "Invalid price"
    }
    
    if(values.stock === "" ) {
       
    } 

    else if (!stockRegex.test(values.stock)) {
        error.stock = "Invalid stock"
    }

    if (values.price === "" && values.stock === "") {
        error.price = "At least one field is required"
        error.stock = "At least one field is required"
    }

    return error;
}

export default EditFormValidation;