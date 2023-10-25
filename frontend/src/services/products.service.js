import axios from "axios";
import authHeader from "./auth-header";
import { useSelector } from "react-redux";
import apiUrl from "../deploy";



const getAllProducts = () => {
  return axios.get(
    apiUrl + "/api/v1/validatedEmployee/getAllProducts",
    { headers: authHeader(), method: "GET", "Content-Type": "application/json" }
  );
};

const addProducts = (product) => {
  const newProduct = {
    name: product.name,
    unitPrice: product.unitPrice,
    description: product.description,
    stock: product.stock,
    category: product.category,
  };


  return axios
    .post(
      apiUrl + "/api/v1/validatedEmployee/product/addProduct",
      product,
      {
        headers: authHeader(),
        method: "POST",
        "Content-Type": "application/json",
      }
    )
    .then((response) => {
      console.log("Producto agregado con éxito:", response.data);
    })
    .catch((error) => {
      console.log(product);
      console.error("Error al agregar el producto:", error);
    });
};

const deleteProduct = (productId) => {
  return axios
    .delete(
      apiUrl + `api/v1/manager/product/deleteProduct/${productId}`,
      {
        headers: authHeader(),
        method: "DELETE",
        "Content-Type": "application/json",
      }
    )


};


const updateProductPrice = (productId, updatedPrice) => {
  const newProductPrice = {
    productId: productId,
    unitPrice: updatedPrice,
  };

  return axios
    .put(
      apiUrl + "/api/v1/manager/product/updatePrice",
      newProductPrice,
      {
        headers: authHeader(),
        method: "PUT",
        "Content-Type": "application/json",
      }
    )
};


const updateProductStock = (productId, operation, updatedStock) => {
  const newProductStock= {
    productId: productId,
    addStock: updatedStock,
  };

  return axios
    .put(
      apiUrl + "/api/v1/manager/product/addStock",
      newProductStock,
      {
        headers: authHeader(),
        method: "PUT",
        "Content-Type": "application/json",
      }
    )
    .then((response) => {
      console.log("Stock modificado con éxito:", response.data);
    })
    .catch((error) => {
      console.error("Error al modificar el Stock del producto:", error);
    });
};

const modifyProductStock = (modifiedProductStock) => {
  console.log(modifiedProductStock);
  return axios
    .put(
      "http://localhost:8080/api/v1/manager/product/modifyProductStock",
      modifiedProductStock,
      {
        headers: authHeader(),
        method: "PUT",
        "Content-Type": "application/json",
      }
    )
    .then((response) => {
      console.log("Stock modificado con éxito:", response.data);
    })
    .catch((error) => {
      console.error("Error al modificar el Stock del producto:", error);
    });
};

const filterByName = (product) => {

  return axios
    .post(
      apiUrl + "/api/v1/validatedEmployee/filterProducts",
      product,
      {
        headers: authHeader(),
        method: "POST",
        "Content-Type": "application/json",
      }
    )
    .then((response) => {
      console.log("Productos encontrados:", response.data);
      return response.data;
    })
    .catch((error) => {
      console.error("Error al buscar el producto:", error);
      throw error;
    });
};


const filter = (product) => {
  console.log(product);
  
  return axios
    .post(
      apiUrl + "/api/v1/validatedEmployee/filterProducts",
      product,
      {
        headers: authHeader(),
        method: "POST",
        "Content-Type": "application/json",
      }
    )
    .then((response) => {
      console.log("Productos encontrados:", response.data);
      return response.data;
    })
    .catch((error) => {
      console.error("Error al buscar el producto:", error);
      throw error;
    });
};

const productsService = {
  getAllProducts,
  addProducts,
  deleteProduct,
  updateProductPrice,
  filterByName,
  filter,
  updateProductStock,
  modifyProductStock

};

export default productsService;
