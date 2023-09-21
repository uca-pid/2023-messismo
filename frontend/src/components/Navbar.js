import React from 'react'
import 'fontsource-roboto';
import { FaHome } from 'react-icons/fa'
import { BsPersonCircle } from 'react-icons/bs'
import { PiCoffeeFill } from 'react-icons/pi'
import { ImExit } from 'react-icons/im'
import { styled } from 'styled-components'
import { Link } from 'react-router-dom'
import BurgerIcon from './BurgerIcon'
import { useSelector, useDispatch } from 'react-redux';
import { toggleClicked } from '../redux/navSlice';
import { signout } from '../redux/authSlice';

const NavLink = styled(Link)`

    align-items: center;
    display: inline-flex;
    text-decoration: none;

    .icon{
        color: white;
        font-size: 24px;
        margin-right: 0.5rem;
    }

    span{
        color: white;
        margin-right: 1rem;
        font-family: 'Roboto';
        font-size: 20px;
    }

`;

const NavContainer = styled.nav`
    padding: 2rem;
    background-color: ${props => (props.clicked ? '' : 'rgb(157,187,191,0.3)')};
    display: flex;
    align-items: center;
    justify-content: space-between;

    @media(min-width: 768px){
        justify-content: center;
    }

    .burger{
        @media(min-width: 768px){
            display: none;
        }
    }

    .links{
        position: absolute;
        top: -700px;
        left: -2000px;
        right: 0;
        margin-left: auto;
        margin-right: auto;
        text-align: center;
        transition: all .5s ease;

        ${NavLink}{
            color: white;
            font-size: 2rem;
            display: block;
        }

        @media(min-width: 768px){
            position: initial;
            margin: 0;

            ${NavLink}{
                font-size: 1rem;
                color: white;
                display: inline;
            }

            display: block;
        }

    }

    .links.active{
        width: 100%;
        display: block;
        position: absolute;
        margin-left: auto;
        margin-right: auto;
        top: 30%;
        left: 0;
        right: 0;
        text-align: center;

        ${NavLink}{
            padding: 1rem;
            .icon{
                color: white;
                font-size: 30px;
                margin-right: 1rem;
            }

            span{
                color: white;
                font-family: 'Roboto';
                font-size: 38px;
            }
        }

    }
`;

const BgDiv = styled.div`
    background-color: rgb(157,187,191,0.3);
    position: absolute;
    top: -1000px;
    left: -1000px;
    width: 100%;
    height: 100%;
    z-index: -1;
    transition: all .6s ease ;

    &.active{
        border-radius: 0 0 70% 0;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
    }
`;

function Navbar() {

    const userType = 'admin'
    const userPending = 'no'

    const clicked = useSelector((state) => state.navigation.clicked);
    const dispatch = useDispatch();

    const handleClick = () => {
        dispatch(toggleClicked());
    };

    const handleSignOut = () => {
        dispatch(signout())
    }

    return(

        <NavContainer clicked={clicked}>

            <div className={`links ${clicked ? 'active' : ''}`}>

                <NavLink to={'/homepage'} onClick={clicked ? handleClick : undefined}>
                    <FaHome className='icon'/>
                    <span>Home</span>
                </NavLink>

                {userPending === 'no' && (
                    <NavLink to={'/products'} onClick={clicked ? handleClick : undefined}>
                        <PiCoffeeFill className='icon'/>
                        <span>Products</span>
                    </NavLink>
                )}

                {userType === 'admin' && userPending === 'no' && (
                    <NavLink to={'/resources'} onClick={clicked ? handleClick : undefined}>
                        <BsPersonCircle className='icon'/>
                        <span>Resources</span>
                    </NavLink>
                )}

                <NavLink to={'/'} onClick={() => {handleSignOut()}}>
                    <ImExit className='icon'/>
                    <span>Sign Out</span>
                </NavLink>

            </div>

            <div className='burger'>
                <BurgerIcon clicked={clicked} handleClick={handleClick} />
            </div>
            <BgDiv className={ `initial ${clicked ? ' active' : ''}` }/>

        </NavContainer>

    )
}

export default Navbar;