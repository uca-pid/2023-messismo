import React, { useState, useEffect, forwardRef } from 'react'
import './Login.css'
import '../App.css'
import image from '../images/signin2.png'
import logo from '../images/logo.png'
import { Link, Navigate, useNavigate } from 'react-router-dom';
import { FaUserShield } from 'react-icons/fa'
import { BsFillShieldLockFill } from 'react-icons/bs'
import { MdEmail } from 'react-icons/md'
import styled from 'styled-components'
import { useDispatch, useSelector } from 'react-redux';
import SUpPopUp from '../components/SignUpPopUp';
import SInPopUp from '../components/SignInPopUp';
import signupvalidation from '../SignUpValidation';
import signinvalidation from '../SignInValidation';
import { login, register } from "../redux/auth";
import { clearMessage } from "../redux/message";
import Stack from '@mui/material/Stack';
import Snackbar from '@mui/material/Snackbar';
import MuiAlert from '@mui/material/Alert';

const ForgotPassword = styled.a`   
    text-decoration: none;
    color: #a7d0cd;
`;

const ErrorMessage = styled.h4`
    color: red;
    font-family: 'Roboto';
    width: 240px;
    word-wrap: break-word;
`;

const Alert = forwardRef(function Alert(props, ref) {
    return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

function Login() {

    const [open, setOpen] = useState(false);

    const [isRegistered, setIsRegistered] = useState(false);

    const [isSignInValid, setIsSignInValid] = useState(false);
    const [signinvalues, setSignInValues] = useState({
        email: '',
        password: ''
    })
    const [signinerrors, setSignInErrors] = useState({})
    const [SignInPopUp, setSignInPopUp] = useState(false)

    const { isLoggedIn } = useSelector((state) => state.auth);
    const { message } = useSelector((state) => state.message);
    const { user: currentUser } = useSelector((state) => state.auth);

    let navigate = useNavigate();
    const dispatch = useDispatch();

    useEffect(() => {
        const validationErrors = signinvalidation(signinvalues);
        setSignInErrors(validationErrors);
        setIsSignInValid(Object.keys(validationErrors).length === 0);
    }, [signinvalues]);

    useEffect(() => {
        dispatch(clearMessage());
    }, [dispatch]);

    const handleLogin = (userData) => {
        const email = userData.email;
        const password = userData.password;
      
        dispatch(login({ email, password }))
          .unwrap()
          .then((response) => {
            const userRole = response.user.role;
            if (userRole === "ADMIN" || userRole === "MANAGER" || userRole === "VALIDATEDEMPLOYEE" ) {
                navigate("/homepage");
            } 
            if (userRole === "EMPLOYEE") {
                setIsRegistered(true);
                setSignInPopUp(true);
            }
          })
          .catch(() => {
            setIsRegistered(false);
            setSignInPopUp(true);
          });
      };

    if ((isLoggedIn) && (currentUser.role === "ADMIN" || currentUser.role === "MANAGER" || currentUser.role === "VALIDATEDEMPLOYEE" )){
        return <Navigate to="/homepage" />;
    }

    const handleSignInInput = (e) => {
        setSignInErrors((prevErrors) => ({
            ...prevErrors,
            [e.target.name]: '',

        }));
        setSignInValues({ ...signinvalues, [e.target.name]: e.target.value });
    }

    function handleSignInValidation() {
        const validationErrors = signinvalidation(signinvalues);
        setSignInErrors(validationErrors);
        if (Object.keys(validationErrors).length === 0){
            setIsSignInValid(true);
        }
        else {
            setIsSignInValid(false);
            handleSnackClick();
        }
    }

    const handleSnackClick = () => {
        setOpen(true);
      };
    
      const handleSnackClose = (event, reason) => {
        if (reason === 'clickaway') {
          return;
        }
    
        setOpen(false);
      };

    return(
        <div>
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
                                    <input type='email' name='email' id='emailSiId' placeholder='Enter your email' className='inpt' onChange={handleSignInInput}/>
                                </div>
                                {signinerrors.email && <ErrorMessage>{signinerrors.email}</ErrorMessage>}
                            </div>

                            <div className='inputDiv'>
                                <label htmlFor='password' className='labl'>Password</label>
                                <div className='inpt flx'>
                                    <BsFillShieldLockFill className='icn' />
                                    <input type='password' name='password' id='passwordSiId' placeholder='Enter your password' className='inpt' onChange={handleSignInInput}/>
                                </div>
                                {signinerrors.password && <ErrorMessage>{signinerrors.password}</ErrorMessage>}
                            </div>

                            <Link className='btn flx'
                            onClick={() => {
                                handleSignInValidation();
                                if(isSignInValid) {
                                    const userData = {
                                        email: signinvalues.email,
                                        password: signinvalues.password,
                                    }
                                    handleLogin(userData);
                                } 
                            }}
                            disabled = {Object.keys(signinerrors).length > 0 || !isSignInValid}>
                                <span>Sign In</span>
                            </Link>

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

                { SignInPopUp && <SInPopUp setSignInPopUp={setSignInPopUp} isRegistered={isRegistered} /> }

            </div>

            <Stack spacing={2} sx={{ width: '100%' }} >
                <Snackbar open={open} autoHideDuration={2500} onClose={handleSnackClose} anchorOrigin={{ vertical: 'bottom', horizontal: 'left' }}>
                    <Alert onClose={handleSnackClose} severity="error" sx={{ width: '100%' }}>
                        <h2 style={{fontFamily: 'Roboto', fontSize: "1rem"}}>One or more fields are empty/incorrect</h2>
                    </Alert>
                </Snackbar>
            </Stack>

        </div>
    )
}

export default Login;