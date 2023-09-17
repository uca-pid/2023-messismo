function Validation(values) {
    let error = {}
    const usernameRegex = /^[a-zA-Z]+$/;
    const emailRegex = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i
    const passwordRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9]{8,}$/

    if(values.username === "") {
        error.username = "Field is empty"
    } 
    else if(!usernameRegex.test(values.username)) {
        error.username = "Username must only contain letters"
    }

    if(values.email === "") {
        error.email = "Field is empty"
    }
    else if(!emailRegex.test(values.email)) {
        error.email = "Invalid email address"
    }

    if(values.password === "") {
        error.password = "Field is empty"
    } 
    else if(!passwordRegex.test(values.password)) {
        error.password = "Password must contain at least 8 characters, 1 uppercase and 1 number"
    }

    return error;
}

export default Validation;