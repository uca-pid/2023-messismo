import axios from "axios";
import authHeader from "./auth-header";
import { useSelector } from "react-redux";


const API_URL =
  "http://localhost:8080//api/v1/validatedEmployee/getAllProducts";

const getAllProducts = () => {
  return axios.get(
    "http://localhost:8080/api/v1/validatedEmployee/getAllProducts",
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
      "http://localhost:8080/api/v1/validatedEmployee/product/addProduct",
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
      `http://localhost:8080/api/v1/manager/product/deleteProduct/${productId}`,
      {
        headers: authHeader(),
        method: "DELETE",
        "Content-Type": "application/json",
      }
    )
    .then((response) => {

      console.log("Producto eliminado con éxito:", response.data);
    })
    .catch((error) => {
      console.error("Error al eliminar el producto:", error);
    });


};


const updateProductPrice = (productId, updatedPrice) => {
  const newProductPrice = {
    productId: productId,
    unitPrice: updatedPrice,
  };

  return axios
    .put(
      "http://localhost:8080/api/v1/manager/product/updatePrice",
      newProductPrice,
      {
        headers: authHeader(),
        method: "PUT",
        "Content-Type": "application/json",
      }
    )
    .then((response) => {
      console.log("Precio modificado con éxito:", response.data);
    })
    .catch((error) => {
      console.error("Error al modificar el precio del producto:", error);
    });
};


const updateProductStock = (productId, operation, updatedStock) => {
  const newProductStock= {
    productId: productId,
    addStock: updatedStock,
  };

  return axios
    .put(
      "http://localhost:8080/api/v1/manager/product/addStock",
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

const filterByName = (product) => {

  return axios
    .post(
      "http://localhost:8080/api/v1/validatedEmployee/filterProducts",
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
      "http://localhost:8080/api/v1/validatedEmployee/filterProducts",
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
  updateProductStock

};

export default productsService;
