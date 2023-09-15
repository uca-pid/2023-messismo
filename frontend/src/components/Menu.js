import React from "react";
import './Menu.css'

function Menu() {
    const products = [
        {
          nombre: 'Papas con cheddar',
          categoria: 'Entradas',
          descripcion: 'Este es el producto 1',
          precio: '$3000',
        },
        {
          nombre: 'Papas bravas',
          categoria: 'Entradas',
          descripcion: 'Este es el producto 2',
          precio: '$3000',
        },
        {
        nombre: 'Papas con cheddar',
          categoria: 'Entradas',
          descripcion: 'Este es el producto 1',
          precio: '$2500',
        },
        {
          nombre: 'Papas bravas',
          categoria: 'Entradas',
          descripcion: 'Este es el producto 2',
          precio: '$50',
        },
        {
        nombre: 'Negroni',
          categoria: 'Tragos',
          descripcion: 'Este es el producto 1',
          precio: '$100',
        },
        {
          nombre: 'Gin Tonic',
          categoria: 'Tragos',
          descripcion: 'Este es el producto 2',
          precio: '$50',
        },
        {
        nombre: 'Fernet',
          categoria: 'Tragos',
          descripcion: 'Este es el producto 1',
          precio: '$100',
        },
        {
          nombre: 'Hamburguesa Martin',
          categoria: 'Platos',
          descripcion: 'Este es el producto 2',
          precio: '$50',
        },
        {
        nombre: 'Pancho Carla',
          categoria: 'Platos',
          descripcion: 'Este es el producto 1',
          precio: '$100',
        },
        {
          nombre: 'Agua sin gas',
          categoria: 'Bebidas sin alcohol',
          descripcion: 'Agua sin gas 500ml ',
          precio: '$50',
        },
        {
            nombre: 'Agua con gas',
            categoria: 'Bebidas sin alcohol',
            descripcion: 'Agua sin gas 500ml ',
            precio: '$50',
          },

      ];
      const productsByCategory = {};

      products.forEach((producto) => {
          const categoria = producto.categoria;
          if (!productsByCategory[categoria]) {
            productsByCategory[categoria] = [];
          }
          productsByCategory[categoria].push(producto);
      });
   
    return(
        <div className="container">
            <h1>Menu</h1>
            {Object.keys(productsByCategory).map((categoria) => (
                <div key={categoria} className="entradas">
                    <p className="categories">{categoria}</p>
                    {productsByCategory[categoria].map((producto, index) => (
                        <div key={index} className="product">
                            <p className="text">{producto.nombre}</p>
                            <p className="text">{producto.precio}</p>
                        </div>
                    ))}
                </div>
            ))}
        </div>

    )
}

export default Menu;