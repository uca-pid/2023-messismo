function FormValidation(values) {
    let error = {}
    const priceRegex = /[-+]?\d*\.?\d+/g


    if (values.price === "") {
        error.price = "Field is empty"
    }

    else if (!priceRegex.test(values.price)) {
        error.email = "Invalid price"
    }
    
    if(values.name === "" || values.category === "" ) {
        error.password = "Field is empty"
    } 
    

    return error;
}

export default FormValidation;