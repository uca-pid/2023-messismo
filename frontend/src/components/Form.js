import React, { useState} from "react";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import './Form.css'
import MenuItem from '@mui/material/MenuItem';
import Select, { SelectChangeEvent } from '@mui/material/Select';
import FormValidation from "../FormValidation";

const Form = (props) => {
  const [name, setName] = useState("");
  const [category, setCategory] = useState("");
  const [description, setDescription] = useState("");
  const [unitPrice, setUnitPrice] = useState("");
  const [errors, setErrors] = useState({});

  const handleNombreChange = (event) => {
    setName(event.target.value);
  };

  const handleCategoriaChange = (event: SelectChangeEvent) => {
    setCategory(event.target.value);
  };

  const handleDescripcionChange = (event) => {
    setDescription(event.target.value);
  };

  const handlePrecioChange = (event) => {
    setUnitPrice(event.target.value);
  };


  const cancelarButton = (event) => {
    props.onClose();
  };

  const handleAddProduct = () => {

    const validationErrors = FormValidation({
      name,
      category,
      description,
      price: unitPrice, 
    });

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      console.log(validationErrors);
    } else {
    const newProductData = {
      name,
      category,
      description,
      unitPrice,
    };

    //productsService.addProducts(newProductData);
    props.onSave(newProductData);
    props.onClose();

  
    setName("");
    setCategory("");
    setDescription("");
    setUnitPrice("");
  }

  }

  return (
    <div>
      <h1 style={{marginBottom: '5%'}}>New Product</h1>
      <p style={{ color: errors.name ? "red" : "black" }}>Name *</p>
      <TextField
        required
        id="name"
        value={name}
        onChange={handleNombreChange}
        variant="outlined"
        error={errors.name ? true : false}
        helperText={errors.name || ''}
        style={{ width: '80%', marginTop: '3%', marginBottom: '3%', fontSize: '1.5rem'}}
        InputProps={{
          style: {
            fontSize: '1.5rem', 
          },}}
          FormHelperTextProps={{
            style: {
              fontSize: '1.1rem', 
            },
          }}
      />
      <p style={{ color: errors.category ? "red" : "black" }}>Category *</p>
      <Select
          labelId="demo-simple-select-label"
          id="demo-simple-select"
          value={category}
          onChange={handleCategoriaChange}
          error={errors.category ? true : false}
          helperText={errors.category || ''}
          style={{ width: '80%', marginTop: '3%', marginBottom: '3%', fontSize: '1.5rem'}}
          InputProps={{
            style: {
              fontSize: '1.5rem', 
            },}}
            FormHelperTextProps={{
              style: {
                fontSize: '1.1rem', 
              },
            }}
        >
          <MenuItem value={"Entradas"}>Entradas</MenuItem>
          <MenuItem value={"Platos"}>Platos</MenuItem>
          <MenuItem value={"Tragos"}>Tragos</MenuItem>
          <MenuItem value={"Bebidas sin alcohol"}>Bebidas sin alcohol</MenuItem>
          <MenuItem value={"Postres"}>Postres</MenuItem>
        </Select>
      <p>Description</p>
      <TextField
        required
        id="description"
        value={description}
        onChange={handleDescripcionChange}
        variant="outlined"
        style={{ width: '80%', marginTop: '3%', marginBottom: '3%', fontSize: '1.5rem'}}
        InputProps={{
          style: {
            fontSize: '1.5rem', 
          },}}
      />
      <p style={{ color: errors.price ? "red" : "black" }}>Price *</p>
      <TextField
        required
        id="unitPrice"
        value={unitPrice}
        onChange={handlePrecioChange}
        variant="outlined"
        style={{ width: '80%', marginTop: '3%', marginBottom: '3%', fontSize: '1.3rem'}}
        error={errors.price ? true : false}
        helperText={errors.price || ''}
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
      <div className="buttons-add">
        <Button
          variant="outlined"
          style={{ color: "grey", borderColor: "grey" , width: "40%", fontSize: '1.3rem'}}
          onClick={cancelarButton}
        >
          Cancel
        </Button>
        <Button
          variant="contained"
          style={{
            backgroundColor: "green",
            color: "black",
            borderColor: "green",
            width: "40%",
            fontSize: '1.5rem'
          }}
          onClick={handleAddProduct}

        >
          Add
        </Button>
      </div>
    </div>
  );
}

export default Form;