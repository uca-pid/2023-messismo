import React, { useState } from 'react'
import { styled } from 'styled-components';
import 'fontsource-roboto';
import { FaHome } from 'react-icons/fa'
import { BsPersonCircle } from 'react-icons/bs'
import { PiCoffeeFill } from 'react-icons/pi'
import { RiLogoutCircleLine } from 'react-icons/ri'
import { AiOutlineClose, AiOutlineMenu } from 'react-icons/ai'
import { Link } from 'react-router-dom';

const NavBar = styled.div`
    display: flex;
    position: fixed;
    flex-direction: column;
    background-color: rgb(157,187,191,0.3);
    align-items: center;
    min-height: 400px;

    inset: ${ (props) => props.opened ? '0 85% 0 0': '0 95% 0 0' };

    @media (max-width: 1600px){
        inset: 0 95% 0 0;
    }
`;

const BurgerIcon = styled(AiOutlineMenu)`
    color: #a4d4cc;
    font-size: 30px;
    margin-block: 3rem;
    position: absolute;

    display: ${ (props) => props.opened ? 'none': 'flex' };

    @media (max-width: 1600px){
        display: none;
    }
`;

const CloseIcon = styled(AiOutlineClose)`
    color: #a4d4cc;
    font-size: 30px;
    margin-inline: 3rem;
    margin-block: 3rem;
    position: absolute;
    right: 0;

    display: ${ (props) => props.opened ? 'flex': 'none' };

    @media (max-width: 1600px){
        display: none;
    }
`;

const Ul = styled.ul`
    display: flex;
    flex-direction: column;
    margin-block: 7rem;
    list-style: none;
    position: absolute;
    width: 100%;
`;

const NavLink = styled(Link)`
    display: flex;
    align-items: center;
    gap: 2.5rem;
    cursor: pointer;
    padding-block: 1.2rem;
    margin-block: 1.2rem;
    text-decoration: none;
    color: #a4d4cc;
    font-size: 30px;

    &:hover{
        background-color: rgb(164,212,204,0.1);
    }

    span{
        display: ${ (props) => props.expanded ? 'flex': 'none' };
        font-family: 'Roboto';
        font-size: 25px;
    }

    .icon{
        margin-left: ${ (props) => props.expanded ? '11%': '33%' };
    }

    @media (max-width: 1600px){
        span{display: none};
        .icon{margin-left: 30%};
    }

`;

const LogOutNavLink = styled(Link)`
    display: flex;
    align-items: center;
    gap: 2.5rem;
    cursor: pointer;
    padding-block: 1.2rem;
    margin-block: 1.2rem;
    text-decoration: none;
    color: #a4d4cc;
    position: absolute;
    bottom: 25px;
    font-size: 30px;
    width: 100%;
    
    &:hover{
        background-color: rgb(164,212,204,0.1);
    }

    span{
        display: ${ (props) => props.expanded ? 'flex': 'none' };
        font-family: 'Roboto';
        font-size: 25px;
    }

    .icon{
        margin-left: ${ (props) => props.expanded ? '11%': '33%' };
    }

    @media (max-width: 1600px){
        span{display: none};
        .icon{margin-left: 30%};
    }
    
`;


const Header = () => {

    const [open, setOpenNavBar] = useState(true);
    const openNavBar = () => setOpenNavBar(!open);

    const userType = 'admin'
    const userPending = 'no'

    return(
        <NavBar opened={open}>

            <BurgerIcon opened={open} onClick={openNavBar}/>
            <CloseIcon opened={open} onClick={openNavBar}/>

            <Ul>

                <NavLink to={'/homepage'} expanded={open}>
                    <FaHome className='icon'/>
                    <span>Home</span>
                </NavLink>

                {userPending === 'no' && (
                    <NavLink to={'/products'} expanded={open}>
                        <PiCoffeeFill className='icon'/>
                        <span>Products</span>
                    </NavLink>
                )}

                {userType === 'admin' && userPending === 'no' && (
                    <NavLink to={'/resources'} expanded={open}>
                        <BsPersonCircle className='icon'/>
                        <span>Resources</span>
                    </NavLink>
                )}
                

            </Ul>

            <LogOutNavLink to={'/'} expanded={open}>
                <RiLogoutCircleLine className='icon'/>
                <span>Sign Out</span>
            </LogOutNavLink>

        </NavBar>
    )
}

export default Header