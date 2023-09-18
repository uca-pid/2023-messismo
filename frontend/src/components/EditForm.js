import React, { useState } from "react";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import "./Form.css";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import Select, { SelectChangeEvent } from "@mui/material/Select";
import { useSlotProps } from "@mui/base";

const EditForm = (props) => {
  const [nombre, setNombre] = useState("");
  const [categoria, setCategoria] = useState("");
  const [descripcion, setDescripcion] = useState("");
  const [precio, setPrecio] = useState("");

  console.log(props.product.nombre);
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

  return (
    <div>
      <h2 style={{ marginBottom: "7%" }}>Editar Producto</h2>
      <p>Nombre</p>
      <TextField
        required
        id="nombre"
        onChange={handleNombreChange}
        variant="outlined"
        style={{ width: "80%" }}
        defaultValue={props.product.nombre}
      />
      <p>Categoria</p>
      <Select
        labelId="demo-simple-select-label"
        id="demo-simple-select"
        onChange={handleCategoriaChange}
        style={{ width: "80%" }}
        defaultValue={props.product.categoria}
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
        onChange={handleDescripcionChange}
        variant="outlined"
        style={{ width: "80%" }}
        defaultValue={props.product.descripcion}
      />
      <p>Precio</p>
      {props.userType === "admin" || props.userType === "manager" ? (
        <div>
          <TextField
            required
            id="precio"
            onChange={handlePrecioChange}
            variant="outlined"
            style={{ width: "80%" }}
            defaultValue={props.product.precio}
          />
        </div>
      ) : (
        <TextField
          disabled
          id="outlined-disabled"
          style={{ width: "80%" }}
          defaultValue={props.product.precio}
        />
      )}
      <div className="buttons-add">
        <Button
          variant="outlined"
          style={{ color: "grey", borderColor: "grey", width: "40%" }}
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
          }}
        >
          Agregar
        </Button>
      </div>
    </div>
  );
};

export default EditForm;
