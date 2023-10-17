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

    else if (values.price <= 0) {
        error.price = "Invalid price"
    }
    
    return error;
}

export default EditFormValidation;