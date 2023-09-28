function FormValidation(values) {
    let error = {}
    const priceRegex = /[-+]?\d*\.?\d+/g


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
    

    return error;
}

export default FormValidation;