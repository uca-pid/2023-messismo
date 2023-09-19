import React from "react";
import 'fontsource-roboto';
import { FaHome } from 'react-icons/fa'
import { BsPersonCircle } from 'react-icons/bs'
import { PiCoffeeFill } from 'react-icons/pi'
import { ImExit } from 'react-icons/im'
import { AiOutlineClose, AiOutlineMenu } from 'react-icons/ai'
import { styled } from 'styled-components';
import { Link } from 'react-router-dom';

const NavContainer = styled.nav`
    padding: 2rem;
    background-color: rgb(157,187,191,0.3);
    display: flex;
    align-items: center;
    justify-content: center;
`;

const NavLink = styled(Link)`

    align-items: center;
    display: inline-flex;

    .icon{
        color: white;
        font-size: 24px;
        margin-right: 0.5rem;
    }

    span{
        color: white;
        text-decoration: none;
        margin-right: 2rem;
        font-family: 'Roboto';
        font-size: 20px;
        
    }
`;

const BurgerIcon = styled(AiOutlineMenu)`

`;

const CloseIcon = styled(AiOutlineClose)`
    
`;


function Navbar() {

    const userType = 'admin'
    const userPending = 'no'

    return(

        <NavContainer>

            <div>
                <NavLink to={'/homepage'}>
                    <FaHome className='icon'/>
                    <span>Home</span>
                </NavLink>

                {userPending === 'no' && (
                    <NavLink to={'/products'}>
                        <PiCoffeeFill className='icon'/>
                        <span>Products</span>
                    </NavLink>
                )}

                {userType === 'admin' && userPending === 'no' && (
                    <NavLink to={'/resources'}>
                        <BsPersonCircle className='icon'/>
                        <span>Resources</span>
                    </NavLink>
                )}

                <NavLink to={'/'}>
                    <ImExit className='icon'/>
                    <span>Sign Out</span>
                </NavLink>
            </div>


        </NavContainer>

    )
}

export default Navbar;