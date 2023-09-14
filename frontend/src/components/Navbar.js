import React, {useState} from "react";
import { Link } from "react-router-dom";
import './Navbar.css';

function Navbar() {
    const [activeLink, setActiveLink] = useState('');
    const handleClick = (link) => {
        setActiveLink(link);
      };
    return(
       <div className="nav_container">
        <h2 style={{color: 'white', fontWeight: 400, marginLeft: '1rem'}}>Bar</h2>
       </div>
       

    )
}

export default Navbar;