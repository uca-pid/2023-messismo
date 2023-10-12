function PriceValidation(values) {
    let error = {}
    const priceRegex = /^[+-]?\d+(\.\d+)?$/
    const stockRegex = /[-+]?\d*\.?\d+/g 
    const emailRegex = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i


    if (values.minPrice === "") {
        
    }

    else if (!priceRegex.test(values.minPrice)) {
        error.minPrice = "Invalid price"
    }
    
    if(values.maxPrice === "" ) {
        
    } 

    else if(!priceRegex.test(values.maxPrice)) {
        error.maxPrice = "Invalid Price"
    } 
    
if (values.maxPrice !== "" && values.minPrice !== "") {
        if (parseFloat(values.minPrice) > parseFloat(values.maxPrice)) {
          error.maxPrice = "Max price must be greater than min price";
        }
      }

    return error;
}

export default PriceValidation;