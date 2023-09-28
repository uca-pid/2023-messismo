import React, { useState } from "react";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import "./Form.css";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import Select, { SelectChangeEvent } from "@mui/material/Select";
import { useSlotProps } from "@mui/base";
import productsService from "../services/products.service";
import { useSelector, useDispatch } from 'react-redux';
import FormValidation from "../FormValidation";

const EditForm = (props) => {
  const [nombre, setNombre] = useState("");
  const [categoria, setCategoria] = useState("");
  const [descripcion, setDescripcion] = useState("");
  const [precio, setPrecio] = useState("");
  const { user: currentUser } = useSelector((state) => state.auth);
  const token = currentUser.access_token
  const role = currentUser.role
  const [errors, setErrors] = useState({});
  
  const handleNombreChange = (event) => {
    setNombre(event.target.value);
  };

  const handleCategoriaChange = (event: SelectChangeEvent) => {
    setCategoria(event.target.value);
  };

  const handleDescripcionChange = (event) => {
    setDescripcion(event.target.value);
  };

  const handlePrecioChange = (event) => {
    setPrecio(event.target.value);
  };

  const cancelarButton = (event) => {
    props.onClose();
  };

  const handleEditProduct = () => {
    
    const validationErrors = FormValidation({
      price: precio,
    });

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      console.log(validationErrors);
    } else {

    props.onClose();

    productsService.updateProductPrice(props.product.productId, precio)
    window.location.reload(); 


    setNombre("");
    setCategoria("");
    setDescripcion("");
    setPrecio("");

    }
  }

  return (
    <div>
      <h1 style={{ marginBottom: "5%", fontSize: '2 rem'}}>Edit Product Price</h1>
      
      {/* <p>Name</p>
      <TextField
        disabled
        id="nombre"
        onChange={handleNombreChange}
        variant="outlined"
        style={{ width: "80%", marginTop: '3%', marginBottom: '3%', fontSize: '1.3rem'}}
        defaultValue={props.product.name}
        InputProps={{
          style: {
            fontSize: '1.5rem', 
          },}}
      />
      <p>Category</p>
      <Select
        disabled
        labelId="demo-simple-select-label"
        id="demo-simple-select"
        onChange={handleCategoriaChange}
        style={{ width: "80%", marginTop: '3%', marginBottom: '3%', fontSize: '1.5rem'}}
        defaultValue={props.product.category}
      >
        <MenuItem value={"Entradas"}>Entradas</MenuItem>
        <MenuItem value={"Platos"}>Platos</MenuItem>
        <MenuItem value={"Tragos"}>Tragos</MenuItem>
        <MenuItem value={"Bebidas sin alcohol"}>Bebidas sin alcohol</MenuItem>
        <MenuItem value={"Postres"}>Postres</MenuItem>
      </Select>
      <p>Description</p>
      <TextField
        disabled
        id="descripcion"
        onChange={handleDescripcionChange}
        variant="outlined"
        style={{ width: "80%", marginTop: '3%', marginBottom: '3%' }}
        defaultValue={props.product.description}
        InputProps={{
          style: {
            fontSize: '1.5rem', 
          },}}
      /> */}
      
      <p style={{ color: errors.price ? "red" : "black" }}>Price</p>
      {role === "ADMIN" || role=== "MANAGER" ? (
        <div>
          <TextField
            required
            id="precio"
            onChange={handlePrecioChange}
            variant="outlined"
            value={precio}
            error={errors.price ? true : false}
            helperText={errors.price || ''}
            style={{ width: "80%", marginTop: '3%', marginBottom: '3%' }}
            defaultValue={props.product.unitPrice}
            InputProps={{
              style: {
                fontSize: '1.5rem', 
                inputMode: 'numeric', pattern: '[0-9]*'
              },}}
              FormHelperTextProps={{
                style: {
                  fontSize: '1.1rem', 
                },
              }}
          />
        </div>
      ) : (
        <TextField
          disabled
          id="outlined-disabled"
          style={{ width: "80%" }}
          defaultValue={props.product.unitPrice}
          InputProps={{
            style: {
              fontSize: '1.5rem', 
            },}}
        />
      )}
      <div className="buttons-add">
        <Button
          variant="outlined"
          style={{ color: "grey", borderColor: "grey", width: "40%", fontSize: '1.3rem' }}
          onClick={cancelarButton}
        >
          Cancel
        </Button>
        <Button
          variant="contained"
          style={{
            backgroundColor: "green",
            color: "white",
            borderColor: "green",
            width: "40%",
            fontSize: '1.3rem'
          }}
          onClick={handleEditProduct}
        >
          Save
        </Button>
      </div>
    </div>
  );
};

export default EditForm;
