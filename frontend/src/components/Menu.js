import React, { useState } from "react";
import './Menu.css'
import Card from "./Card";

function Menu() {
 
    const [productCard, setProductCard] = useState(null);
    const [userType, setUserType] = useState('admin');
    const products = [
        {
          nombre: 'Papas con cheddar',
          categoria: 'Entradas',
          descripcion: 'Papas fritas con queso cheddar',
          precio: '$3000',
        },
        {
          nombre: 'Papas bravas',
          categoria: 'Entradas',
          descripcion: 'Papas fritas con salsa brava, picante',
          precio: '$3000',
        },
        {
        nombre: 'Bueñuelos de espinaca',
          categoria: 'Entradas',
          descripcion: 'Bueñuelos de espinaca hechos con mucho amor',
          precio: '$2500',
        },
        {
          nombre: 'Bastones de muzzarellaa',
          categoria: 'Entradas',
          descripcion: 'Bastones de muzzarella con sal marina',
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


      const openCard = (producto) => {
        setProductCard(producto);
    };

    const closeCard = () => {
        setProductCard(null);
    };
   
    return(
        <div className="container">
        <h1>Menu</h1>
        {Object.keys(productsByCategory).map((categoria) => (
            <div key={categoria} className="entradas">
                <p className="categories">{categoria}</p>
                {productsByCategory[categoria].map((producto, index) => (
                    <div key={index} className="product">
                        <div className="firstLine">
                        <p className="text">{producto.nombre}</p>
                        <p className="text">{producto.precio}</p>
                        </div>
                        <p className="descripcion">{producto.descripcion}</p>
                    </div>

                ))}
            </div>
        ))}
    </div>

    )
    
}

export default Menu;