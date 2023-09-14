import React from "react";
import { redirect } from "react-router-dom";
import Navbar from "./Navbar";
import './Products.css'
import { Link } from "react-router-dom";


const Products = () => {
  return (
    <div style={styles.container}>
      <div style={styles.formContainer}>
        <h1>Producto</h1>
        <form>
          <div input style={styles.section}>
          <p>Nombre</p>
          <input style={styles.input_box} type="text" id="nombre" name="nombre" />
          </div>
          <div>
          <p>Descripción</p>
          <input style={styles.input_box} type="text" id="descripcion" name="descripcion" />
          </div>
          <div>
          <p>Categoría</p>
          <select id="opciones">
            <option value="opcion1">Bebida sin alcohol</option>
            <option value="opcion2">Trago</option>
            <option value="opcion3">Entrada</option>
            <option value="opcion3">Plato principal</option>
            <option value="opcion3">Postre</option>
          </select>
          </div>
          <div>
          <p>Precio unitario</p>
          <input style={styles.input_box} type="email" id="precio" name="precio" />
          </div>
          <div className="buttons">
          <Link to="/" className="boton-estilizado2">
            Volver
          </Link>
          <button className="boton-estilizado" type="submit">Cargar Producto</button>
          </div>
        </form>
      </div>
    </div>
  );
};

const styles = {
  container: {
    display: 'flex',
    flex: 1, 
    justifyContent: 'center',
    alignItems: 'center',
    //padding: '20px',
  
  },
  formContainer: {
    backgroundColor: '#fff',
    border: '3px solid #ccc',
    borderRadius: '10px',
    padding: '20px',
    width: '80%',
    height: '75%',
    justifyContent: 'center',
    //alignItems: 'center',
    boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
    flexDirection: 'column',
    },
  section: {
    height: '80%',
  },

  input_box: {
    height: 30,
    width:'30%',

  }
};

export default Products;