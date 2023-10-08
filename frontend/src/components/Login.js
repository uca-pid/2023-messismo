import React from 'react'
import { useForm, Controller } from 'react-hook-form'
import './Login.css'
import '../App.css'
import image from '../images/signin2.png'
import logo from '../images/logo.png'
import { Link } from 'react-router-dom'
import { FaUserShield } from 'react-icons/fa'
import { BsFillShieldLockFill } from 'react-icons/bs'
import { MdEmail } from 'react-icons/md'
import styled from 'styled-components'

const ForgotPassword = styled.a`   
    text-decoration: none;
    color: #a7d0cd;
`;

const Login = () => {

    return(
        <div className='loginPage flx'>
            <div className='cntainr flx'>

                <div className='formDiv flx'>
                
                    <form action='' className='form grd'>
                        
                        <div className='headerDiv'>
                            <img src={logo} className='logoimg'></img>
                        </div>
                        
                        <div className='inputDiv'>
                            <label htmlFor='email' className='labl'>Email</label>
                            <div className='inpt flx'>
                                <MdEmail className='icn' />
                                <input type='text' id='email' placeholder='Enter your email' className='inpt'/>
                            </div>
                        </div>

                        <div className='inputDiv'>
                            <label htmlFor='password' className='labl'>Password</label>
                            <div className='inpt flx'>
                                <BsFillShieldLockFill className='icn' />
                                <input type='password' id='username' placeholder='Enter your password' className='inpt'/>
                            </div>
                        </div>

                        <button type='submit' className='btn flx'>
                            <span>Sign In</span>
                        </button>

                        <span className='forgotPassword'>
                            <ForgotPassword href=''>Forgot your Password?</ForgotPassword>
                        </span>

                    </form>
                </div>

                <div className='imageDiv'>
                    <img src={image} className='imag'></img>

                    <div className='textDiv'>
                        <h2 className='title'>Welcome Back</h2>
                        <p className='subtitle'>Please log in to your account to get started</p>
                    </div>

                    <div className='footerDiv flx'>
                        <span className='text'>Don't have an account?</span>
                        <Link to={'/register'}>
                            <button className='btn'>Sign Up</button>
                        </Link>
                    </div>
                </div>

            </div>
        </div>
    )
}

export default Login;