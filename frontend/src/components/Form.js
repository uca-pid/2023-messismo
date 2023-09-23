import React, { useState } from "react";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import './Form.css'
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select, { SelectChangeEvent } from '@mui/material/Select';
import { useSlotProps } from "@mui/base";

const Form = (props) => {
  const [nombre, setNombre] = useState("");
  const [categoria, setCategoria] = useState("");
  const [descripcion, setDescripcion] = useState("");
  const [precio, setPrecio] = useState("");

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

  const handleAddProduct = () => {

    const newProductData = {
      nombre,
      categoria,
      descripcion,
      precio,
    };

    props.onSave(newProductData);
    props.onClose();

  
    setNombre("");
    setCategoria("");
    setDescripcion("");
    setPrecio("");


  }

  return (
    <div>
      <h2 style={{marginBottom: '7%'}}>Nuevo Producto</h2>
      <p>Nombre</p>
      <TextField
        required
        id="nombre"
        value={nombre}
        onChange={handleNombreChange}
        variant="outlined"
        style={{ width: '80%', marginTop: '3%', marginBottom: '3%', fontSize: '1.5rem'}}
      />
      <p>Categoria</p>
      <Select
          labelId="demo-simple-select-label"
          id="demo-simple-select"
          value={categoria}
          onChange={handleCategoriaChange}
          style={{ width: '80%', marginTop: '3%', marginBottom: '3%', fontSize: '1.5rem'}}
        >
          <MenuItem value={"Entradas"}>Entradas</MenuItem>
          <MenuItem value={"Platos"}>Platos</MenuItem>
          <MenuItem value={"Tragos"}>Tragos</MenuItem>
          <MenuItem value={"Bebidas sin alcohol"}>Bebidas sin alcohol</MenuItem>
          <MenuItem value={"Postres"}>Postres</MenuItem>
        </Select>
      <p>Descripción</p>
      <TextField
        required
        id="descripcion"
        value={descripcion}
        onChange={handleDescripcionChange}
        variant="outlined"
        style={{ width: '80%', marginTop: '3%', marginBottom: '3%', fontSize: '1.5rem'}}
      />
      <p>Precio</p>
      <TextField
        required
        id="precio"
        value={precio}
        onChange={handlePrecioChange}
        variant="outlined"
        style={{ width: '80%', marginTop: '3%', marginBottom: '3%', fontSize: '1.3rem'}}
      />
      <div className="buttons-add">
        <Button
          variant="outlined"
          style={{ color: "grey", borderColor: "grey" , width: "40%", fontSize: '1.3rem'}}
          onClick={cancelarButton}
        >
          Cancelar
        </Button>
        <Button
          variant="contained"
          style={{
            backgroundColor: "green",
            color: "white",
            borderColor: "green",
            width: "40%",
            fontSize: '1.5rem'
          }}
          onClick={handleAddProduct}

        >
          Agregar
        </Button>
      </div>
    </div>
  );
}

export default Form;
