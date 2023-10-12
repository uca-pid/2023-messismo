import React, { useState, useEffect} from "react";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import './Form.css'
import MenuItem from '@mui/material/MenuItem';
import Select, { SelectChangeEvent } from '@mui/material/Select';
import FormValidation from "../FormValidation";
import categoryService from "../services/category.service";

const Form = (props) => {
  const [name, setName] = useState("");
  const [category, setCategory] = useState("");
  const [description, setDescription] = useState("");
  const [stock, setStock] = useState("");
  const [unitPrice, setUnitPrice] = useState("");
  const [errors, setErrors] = useState({});
  const [characterCount, setCharacterCount] = useState(0);
  const maxCharacterLimit = 255;
  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState('');

  useEffect(() => {
    categoryService.getAllCategories()
      .then(response => {
        setCategories(response.data);
      })
      .catch(error => {
        console.error("Error al obtener categorías:", error);
      });
  }, []);

  const handleNombreChange = (event) => {
    setName(event.target.value);
  };

  const handleStockChange = (event) => {
    setStock(event.target.value);
  };

  const handleCategoriaChange = (event) => {
    setSelectedCategory(event.target.value);
  };

  const handleDescripcionChange = (event) => {
    const text = event.target.value;
    setDescription(text);
    setCharacterCount(text.length);
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
      category: selectedCategory,
      price: unitPrice, 
      stock,
    });

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      console.log(validationErrors);
    } else {
      const selectedCategoryObj = categories.find(cat => cat.name === selectedCategory);
      console.log(selectedCategory);
      


    const newProductData = {
      name,
      category: selectedCategory, 
      description,
      unitPrice,
      stock,
    };

    console.log(newProductData);

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
            fontSize: '1.1rem', 
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
        value={selectedCategory}
        onChange={handleCategoriaChange}
        error={errors.category ? true : false}
        helperText={errors.category || ''}
        style={{ width: '80%', marginTop: '3%', marginBottom: '3%', fontSize: '1.1rem'}}
        InputProps={{
          style: {
            fontSize: '1.1rem',
          },
        }}
        FormHelperTextProps={{
          style: {
            fontSize: '1.1rem',
          },
        }}
      >
        {categories.map(category => (
          <MenuItem key={category.id} value={category.name}>
            {category.name}
          </MenuItem>
        ))}
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
            fontSize: '1.1rem', 
          },}}
        inputProps= {{
            maxLength: 255 // Establecer la longitud máxima permitida
          }}
      />
      <p style={{ fontSize: "1rem", color: characterCount > maxCharacterLimit ? "red" : "black" }}>
        {characterCount}/{maxCharacterLimit}
      </p>
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
            fontSize: '1.1rem', 
            inputMode: 'numeric', pattern: '[0-9]*'
          },}}
          FormHelperTextProps={{
            style: {
              fontSize: '1.1rem', 
            },
          }}
      />
      <p style={{ color: errors.stock ? "red" : "black" }}>Stock *</p>
      <TextField
        required
        id="stock"
        value={stock}
        onChange={handleStockChange}
        variant="outlined"
        style={{ width: '80%', marginTop: '3%', marginBottom: '3%', fontSize: '1.3rem'}}
        error={errors.stock ? true : false}
        helperText={errors.stock || ''}
        InputProps={{
          style: {
            fontSize: '1.1rem', 
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
          style={{ color: "grey", borderColor: "grey" , width: "40%", fontSize: '1rem'}}
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
            fontSize: '1rem'
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