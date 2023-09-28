import React, { useEffect } from "react";
import { redirect } from "react-router-dom";
import Navbar from "./Navbar";
import "./Products.css";
import { Link } from "react-router-dom";
import { useState } from "react";
import { IconButton } from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import EditIcon from "@mui/icons-material/Edit";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";
import Button from "@mui/material/Button";
import AddIcon from "@mui/icons-material/Add";
import Form from "./Form";
import EditForm from "./EditForm";
import { makeStyles } from "@mui/styles";
import productsService from "../services/products.service";
import { useSelector, useDispatch } from "react-redux";
import Tooltip from "@mui/material/Tooltip";

const ProductsList = () => {
  const [userType, setUserType] = useState("admin");
  const [openFormModal, setOpenFormModal] = useState(false);
  const [editingProduct, setEditingProduct] = useState(null);
  const [isEditFormOpen, setIsEditFormOpen] = useState(false);
  const [products, setProducts] = useState([]);
  const [open, setOpen] = React.useState(false);
  const { user: currentUser } = useSelector((state) => state.auth);
  const token = currentUser.access_token;
  const role = currentUser.role;

  useEffect(() => {
    console.log("CURRENT ROLE: ", role);
    productsService
      .getAllProducts()
      .then((response) => {
        setProducts(response.data);
      })
      .catch((error) => {
        console.error("Error al mostrar los productos", error);
      });
  }, [openFormModal, open]);

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleOpenProductsModal = () => {
    setOpenFormModal(true);
  };

  const handleCloseProductsModal = () => {
    setOpenFormModal(false);
    window.location.reload();
  };

  const [selectedProduct, setSelectedProduct] = useState(null);

  const handleDeleteClick = (producto) => {
    console.log(producto);
    setSelectedProduct(producto);
    console.log(producto);
    setOpen(true);
  };

  const deleteProduct = () => {
    if (selectedProduct) {
      console.log(selectedProduct.id);
      productsService.deleteProduct(selectedProduct.productId);
      setSelectedProduct(null);
      setOpen(false);
      window.location.reload();
    }
  };

  const handleEditClick = (producto) => {
    setEditingProduct(producto);
    setIsEditFormOpen(true);
  };

  const handleCloseEditForm = () => {
    setIsEditFormOpen(false);
  };

  const handleSaveProduct = (newProductData) => {
    setProducts([...products, newProductData]);
    console.log(products);
  };

  const handleEditProduct = (newProductData) => {
    const productToUpdate = products.find(
      (product) => product.id === editingProduct.id
    );

    if (productToUpdate) {
      const updatedProduct = { ...productToUpdate, ...newProductData };

      const updatedProducts = products.map((product) =>
        product.id === editingProduct.id ? updatedProduct : product
      );

      setProducts(updatedProducts);
    }

    handleCloseEditForm();
  };

  return (
    <div className="container">
      <div className="add-product">
      {role === "ADMIN" || role === "MANAGER" || role === "VALIDATEDEMPLOYEE"? (
        <Button
          variant="contained"
          endIcon={<AddIcon />}
          style={{
            color: "white",
            borderColor: "#007bff",
            marginTop: "4%",
            fontSize: "1.3rem",
            height: "40px",
          }}
          onClick={handleOpenProductsModal}
        >
          Add Product
        </Button>
        ) : (
           console.log("")
        )}
      </div>
      <Dialog
        open={openFormModal}
        dividers={true}
        onClose={handleCloseProductsModal}
        aria-labelledby="form-dialog-title"
        className="custom-dialog"
        maxWidth="sm"
        fullWidth
      >
        <DialogContent>
          <Form onClose={handleCloseProductsModal} onSave={handleSaveProduct} />
        </DialogContent>
      </Dialog>
      {products.map((producto, index) => (
        <div className="entradas" key={index}>
          <div className="product">
            <div className="firstLine">
              <div className="names">
                <div className="name">
                <p className="text" style={{ fontWeight: "bold" }}>
                  {producto.name}
                </p>
                </div>
                <div className="category">
                <p className="text" >{producto.category}</p>
                </div>
                <p className="text">{producto.unitPrice}</p>
              </div>
              <div className="buttons-edit">
              {role === "ADMIN" || role === "MANAGER" ? (
                <Tooltip title="Edit Price" arrow style={{ fontSize: "2rem" }}>
                <IconButton
                  aria-label="edit"
                  size="large"
                  color="red"
                  onClick={() => handleEditClick(producto)}
                  title="Edit Price"
                >
                  <EditIcon style={{ fontSize: "2rem" }} />
                </IconButton>
                </Tooltip>
              ) : (
                console.log((""))
              )}
                {role === "ADMIN" || role === "MANAGER" ? (
                   <Tooltip title="Delete Product" arrow style={{ fontSize: "2rem" }}>
                  <IconButton
                    aria-label="delete"
                    size="large"
                    style={{ color: "red", fontSize: "1.5 rem" }}
                    onClick={() => handleDeleteClick(producto)}
                  >
                    <DeleteIcon style={{ fontSize: "2rem" }} />
                  </IconButton>
                  </Tooltip>
                  
                ) : (
                  console.log("hola")
                )}
              </div>
            </div>
            <div className="final-line">
              <p className="descripcion">{producto.description}</p>
            </div>
          </div>
        </div>
      ))}
      {isEditFormOpen && (
        <Dialog
          open={isEditFormOpen}
          onClose={handleCloseEditForm}
          aria-labelledby="edit-form-dialog-title"
          className="custom-dialog"
          maxWidth="sm"
          fullWidth
        >
          <DialogContent>
            <EditForm
              product={editingProduct}
              userType={userType}
              onSave={handleEditProduct}
              onClose={handleCloseEditForm}
            />
          </DialogContent>
        </Dialog>
      )}
      {open && (
        <Dialog
          open={open}
          onClose={handleClose}
          aria-labelledby="alert-dialog-title"
          aria-describedby="alert-dialog-description"
          PaperProps={{
            style: {
              backgroundColor: "white",
              boxShadow: "none",
              zIndex: 1000,
              fontSize: '24px',
        
            },
          }}
        >
          <DialogTitle id="alert-dialog-title" style={{fontSize: '1.8rem'}}>
            {selectedProduct &&
              `Are you sure you want to delete the product ${selectedProduct.name}?` }
          </DialogTitle>
          <DialogContent>
            <DialogContentText id="alert-dialog-description" style={{fontSize: '1.3rem'}}>
              The product will be permanently deleted
            </DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleClose} style={{fontSize: '1.3rem'}} >Cancel</Button>
            <Button onClick={deleteProduct} style={{ color: "red", fontSize: '1.3rem' }} autoFocus>
              Delete
            </Button>
          </DialogActions>
        </Dialog>
      )}
    </div>
  );
};

export default ProductsList;
