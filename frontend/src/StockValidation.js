function StockValidation(values) {
    let error = {}
    const stockRegex = /^[+-]?\d+(\.\d+)?$/
    
    if (values.minStock === "") {
        
    }

    else if (!stockRegex.test(values.minStock)) {
        error.minStock = "Invalid stock"
    }
    
    if(values.maxStock === "" ) {
        
    } 

    else if(!stockRegex.test(values.maxStock)) {
        error.maxStock = "Invalid stock"
    } 
    
if (values.maxStock !== "" && values.minStock !== "") {
        if (parseFloat(values.minStock) > parseFloat(values.maxStock)) {
          error.maxStock = "Max stock must be greater than min stock";
        }
      }

    return error;
}

export default StockValidation;