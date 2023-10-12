function ChangePasswordValidation(values) {
    let error = {}
    const emailRegex = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i
    const numRegex = /[-+]?\d*\.?\d+/g
    const passwordRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9]{8,}$/

    if (values.email === "") {
        error.email = "Field is empty"
    }
    else if (!emailRegex.test(values.email)) {
        error.email = "Invalid email address"
    }

    if (values.pin === "") {
        error.pin = "Field is empty"
    }
    else if (!numRegex.test(values.pin)) {
        error.pin = "Invalid pin"
    }

    if(values.password === "") {
        error.password = "Field is empty"
    } 
    else if(!passwordRegex.test(values.password)) {
        error.password = "Password must contain at least 8 characters, 1 uppercase and 1 number"
    }


    console.log(error)

    return error
}
export default ChangePasswordValidation;