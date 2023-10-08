import React from 'react'
import { useForm, Controller } from 'react-hook-form'
import './Login.css'
import '../App.css'
import image from '../images/signup2.png'
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

const Register = () => {

    return(
        <div className='loginPage flx'>
            <div className='cntainr flx'>

                <div className='imageDiv'>
                    <img src={image} className='imag'></img>

                    <div className='textDiv'>
                        <h2 className='title'>Hi There</h2>
                        <p className='subtitle'>Please note that admin verification is required to activate your account</p>
                    </div>

                    <div className='footerDiv flx'>
                        <span className='text'>Already have an account?</span>
                        <Link to={'/login'}>
                            <button className='btn'>Sign In</button>
                        </Link>
                    </div>
                </div>

                <div className='formDiv flx'>
                
                    <form action='' className='form grd'>
                        
                        <div className='headerDiv'>
                            <img src={logo} className='logoimg'></img>
                        </div>
                        
                        <div className='inputDiv'>
                            <label htmlFor='username' className='labl'>Username</label>
                            <div className='inpt flx'>
                                <FaUserShield className='icn' />
                                <input type='text' id='username' placeholder='Enter your username' className='inpt'/>
                            </div>
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
                                <input type='password' id='password' placeholder='Enter your password' className='inpt'/>
                            </div>
                        </div>

                        <button type='submit' className='btn flx'>
                            <span>Sign Up</span>
                        </button>

                    </form>
                </div>

            </div>
        </div>
    )
}

export default Register;