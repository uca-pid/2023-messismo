import React from "react";
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

const Products = () => {
  const [userType, setUserType] = useState("admin");
  const products = [
    {
      nombre: "Papas con cheddar",
      categoria: "Entradas",
      descripcion: "Papas fritas con queso cheddar",
      precio: "$3000",
    },
    {
      nombre: "Papas bravas",
      categoria: "Entradas",
      descripcion: "Papas fritas con salsa brava, picante",
      precio: "$3000",
    },
    {
      nombre: "Bueñuelos de espinaca",
      categoria: "Entradas",
      descripcion: "Bueñuelos de espinaca hechos con mucho amor",
      precio: "$2500",
    },
    {
      nombre: "Bastones de muzzarellaa",
      categoria: "Entradas",
      descripcion: "Bastones de muzzarella con sal marina",
      precio: "$50",
    },
    {
      nombre: "Negroni",
      categoria: "Tragos",
      descripcion: "Este es el producto 1",
      precio: "$100",
    },
    {
      nombre: "Gin Tonic",
      categoria: "Tragos",
      descripcion: "Este es el producto 2",
      precio: "$50",
    },
    {
      nombre: "Fernet",
      categoria: "Tragos",
      descripcion: "Este es el producto 1",
      precio: "$100",
    },
    {
      nombre: "Hamburguesa Martin",
      categoria: "Platos",
      descripcion: "Este es el producto 2",
      precio: "$50",
    },
    {
      nombre: "Pancho Carla",
      categoria: "Platos",
      descripcion: "Este es el producto 1",
      precio: "$100",
    },
    {
      nombre: "Agua sin gas",
      categoria: "Bebidas sin alcohol",
      descripcion: "Agua sin gas 500ml ",
      precio: "$50",
    },
    {
      nombre: "Agua con gas",
      categoria: "Bebidas sin alcohol",
      descripcion: "Agua sin gas 500ml ",
      precio: "$50",
    },
  ];

  const [open, setOpen] = React.useState(false);

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const [selectedProduct, setSelectedProduct] = useState(null);

  const handleDeleteClick = (producto) => {
    setSelectedProduct(producto);
    console.log(producto)
    setOpen(true);
  };

  return (
    <div className="container">
      <h1>Productos</h1>
      {products.map((producto, index) => (
        <div className="entradas">
          <div key={index} className="product">
            <p className="categoria">Categoria: {producto.categoria}</p>
            <div className="firstLine">
              <div className="names">
                <p className="text">{producto.nombre}</p>
                <p className="text">{producto.precio}</p>
              </div>
              <div className="buttons">
                <IconButton aria-label="edit" size="large" color="red">
                  <EditIcon />
                </IconButton>
                <IconButton
                  aria-label="delete"
                  size="large"
                  className="icon-button-red"
                  onClick={() => handleDeleteClick(producto)} 
                >
                  <DeleteIcon />
                </IconButton>
                <Dialog
                  open={open}
                  onClose={handleClose}
                  aria-labelledby="alert-dialog-title"
                  aria-describedby="alert-dialog-description"
                >
                  <DialogTitle id="alert-dialog-title">
                  {selectedProduct && `¿Estás seguro que quieres eliminar el producto ${selectedProduct.nombre}?`}
                  </DialogTitle>
                  <DialogContent>
                    <DialogContentText id="alert-dialog-description">
                      El producto será eliminado permanentemente de la lista.
                    </DialogContentText>
                  </DialogContent>
                  <DialogActions>
                    <Button onClick={handleClose}>Cancelar</Button>
                    <Button onClick={handleClose} autoFocus>
                      Eliminar
                    </Button>
                  </DialogActions>
                </Dialog>
              </div>
            </div>
            <p className="descripcion">{producto.descripcion}</p>
          </div>
        </div>
      ))}
    </div>
  );
};

export default Products;
