import React, { useState, useEffect, forwardRef } from 'react'
import './Login.css'
import '../App.css'
import image from '../images/signup2.png'
import logo from '../images/logo.png'
import { Link, Navigate, useNavigate } from 'react-router-dom';
import { FaUserShield } from 'react-icons/fa'
import { BsFillShieldLockFill } from 'react-icons/bs'
import { MdEmail } from 'react-icons/md'
import styled from 'styled-components'
import { useDispatch, useSelector } from 'react-redux';
import SUpPopUp from '../components/SignUpPopUp';
import signupvalidation from '../SignUpValidation';
import { register } from "../redux/auth";
import { clearMessage } from "../redux/message";
import Stack from '@mui/material/Stack';
import Snackbar from '@mui/material/Snackbar';
import MuiAlert from '@mui/material/Alert';
import { CircularProgress } from '@mui/material'
import Box from '@mui/material/Box'


const ErrorMessage = styled.h4`
    color: red;
    font-family: 'Roboto';
    width: 240px;
    word-wrap: break-word;
`;

const Alert = forwardRef(function Alert(props, ref) {
    return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

const Register = () => {

    const [open, setOpen] = useState(false);

    const [isRegistered, setIsRegistered] = useState(false);

    const [isSignUpValid, setIsSignUpValid] = useState(false);
    const [signupvalues, setSignUpValues] = useState({
        username: '',
        email: '',
        password: ''
    })
    const [signuperrors, setSignUpErrors] = useState({})
    const [SignUpPopUp, setSignUpPopUp] = useState(false)

    const { isLoggedIn } = useSelector((state) => state.auth);
    const { message } = useSelector((state) => state.message);
    const { user: currentUser } = useSelector((state) => state.auth);
    const [isLoading, setIsLoading] = useState(false);

    let navigate = useNavigate();
    const dispatch = useDispatch();

    useEffect(() => {
        const validationErrors = signupvalidation(signupvalues);
        setSignUpErrors(validationErrors);
        setIsSignUpValid(Object.keys(validationErrors).length === 0);
    }, [signupvalues]);

    useEffect(() => {
        dispatch(clearMessage());
    }, [dispatch]);

    if ((isLoggedIn) && (currentUser.role === "ADMIN" || currentUser.role === "MANAGER" || currentUser.role === "VALIDATEDEMPLOYEE" )){
        return <Navigate to="/homepage" />;
    }

    const handleRegister = (userData) => {
        setIsLoading(true);
        const username = userData.username;
        const email = userData.email;
        const password = userData.password;

        dispatch(register({ username, email, password }))
        .unwrap()
        .then(() => {
            setIsRegistered(false);
            setSignUpPopUp(true);
        })
        .catch(() => {
            setIsRegistered(true);
            setSignUpPopUp(true);
        })
        .finally(() => {
            setIsLoading(false);
        });

    };

    const handleSignUpInput = (e) => {
        setSignUpValues({ ...signupvalues, [e.target.name]: e.target.value });
    }

    const handleSignUpErrors = (e) => {
        setSignUpErrors((prevErrors) => ({
            ...prevErrors,
            [e.target.name]: '',
        }));
    }

    function handleSignUpValidation() {
        const validationErrors = signupvalidation(signupvalues);
        setSignUpErrors(validationErrors);
        if (Object.keys(validationErrors).length === 0){
            setIsSignUpValid(true);
        }
        else {
            setIsSignUpValid(false);
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
                                <label htmlFor='username' className='labl'>Username</label>
                                <div className='inpt flx'>
                                    <FaUserShield className='icn' />
                                    <input type='text' name='username' id='usernameId' placeholder='Enter your username' className='inpt' onChange={handleSignUpInput} onBlur={handleSignUpErrors}/>
                                </div>
                                {signuperrors.username && <ErrorMessage>{signuperrors.username}</ErrorMessage>}
                            </div>

                            <div className='inputDiv'>
                                <label htmlFor='email' className='labl'>Email</label>
                                <div className='inpt flx'>
                                    <MdEmail className='icn' />
                                    <input type='email' name='email' id='emailSuId' placeholder='Enter your email' className='inpt' onChange={handleSignUpInput} onBlur={handleSignUpErrors}/>
                                </div>
                                {signuperrors.email && <ErrorMessage>{signuperrors.email}</ErrorMessage>}
                            </div>

                            <div className='inputDiv'>
                                <label htmlFor='password' className='labl'>Password</label>
                                <div className='inpt flx'>
                                    <BsFillShieldLockFill className='icn' />
                                    <input type='password' name='password' id='passwordSuId' placeholder='Enter your password' className='inpt' onChange={handleSignUpInput} onBlur={handleSignUpErrors}/>
                                </div>
                                {signuperrors.password && <ErrorMessage>{signuperrors.password}</ErrorMessage>}
                            </div>
                            {isLoading && (
                                <Box
                                    sx={{
                                        display: 'flex',
                                        justifyContent: 'center',
                                        alignItems: 'center',
                                        marginTop: '10%',
                                    }}
                                >
                                    <CircularProgress style={{ color: "#a4d4cc" }} />
                                </Box>
                            )}
                            <Link type='submit' className='btn flx'
                            onClick={() => {
                                handleSignUpValidation();
                                if(isSignUpValid) {
                                    const userData = {
                                        username: signupvalues.username,
                                        email: signupvalues.email,
                                        password: signupvalues.password,
                                    }
                                    handleRegister(userData);
                                }
                            }}
                            disabled = {Object.keys(signuperrors).length > 0 || !isSignUpValid}>
                                <span>Sign Up</span>
                            </Link>

                        </form>
                    </div>
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

                </div>

                { SignUpPopUp && <SUpPopUp setSignUpPopUp={setSignUpPopUp} isRegistered={isRegistered} /> }

            </div>

            <Stack spacing={2} sx={{ width: '100%' }}>
                <Snackbar open={open} autoHideDuration={2500} onClose={handleSnackClose} anchorOrigin={{ vertical: 'bottom', horizontal: 'left' }}>
                    <Alert onClose={handleSnackClose} severity="error" sx={{ width: '100%' }}>
                    <h2 style={{fontFamily: 'Roboto', fontSize: "1rem"}}>One or more fields are empty/incorrect</h2>
                    </Alert>
                </Snackbar>
            </Stack>

        </div>
    )
}

export default Register;