function CategoryValidation(values) {
    let error = {}

    if (values.name === "") {
        error.name = "Field is empty"
    }
    return error;
};
export default CategoryValidation;