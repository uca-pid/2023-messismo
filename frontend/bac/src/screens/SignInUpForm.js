import React, { useState } from 'react'
import { styled } from 'styled-components';
import 'fontsource-roboto';
import { Link } from 'react-router-dom';

const Label = styled.label`
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: #a7d0cd;
  font-size: 1.2rem;
  font-family: 'Roboto';
  margin-top: 1rem;
`;

const Switch = styled.div`
    position: relative;
    width: 45px;
    height: 20px;
    background: #b3b3b3;
    border-radius: 20px;
    transition: 300ms all;

    &:before {
    transition: 300ms all;
    content: "";
    position: absolute;
    width: 25px;
    height: 25px;
    border-radius: 20px;
    top: 50%;
    left: 0px;
    background: white;
    transform: translate(0, -50%);
    }
`;

const SwitchInput = styled.input`
    display: none;

    &:checked + ${Switch} {
        background: #a4d4cc;

        &:before {
            transform: translate(20px, -50%);
        }
    }
`;

const BackgroundBox = styled.div`
    background-color: black;
    height: 50vh;
    width: 90vh;
    display: flex;
    justify-content: center;
    position: relative;
    margin: 23rem auto;
    border-radius: 23px;
    min-width: 300px;
    min-height: 300px;

    .signin{
        z-index: ${props => props.clicked ? "-600": "500"};
        transform: ${props => props.clicked ? "none": "0%"};
        transition: transform 0.5s;
    }

    .signup{
        z-index: ${props => props.clicked ? "500": "-600"};
        transform: ${props => props.clicked ? "100%": "none"};
        transition: transform 0.5s;
    }

    .signintext{
        z-index: ${props => props.clicked ? "-600": "500"};
        transform: ${props => props.clicked ? "none": "0%"};
        transition: transform 0.5s;
    }

    .signuptext{
        z-index: ${props => props.clicked ? "500": "-600"};
        transform: ${props => props.clicked ? "100%": "none"};
        transition: transform 0.5s;
    }
`;

const Logo = styled.img`
    max-width: 5%;
    height: auto;
    display: flex;
`;

const Box1 = styled.div`
    background-color: #58476a;
    width: 50%;
    height: 100%;
    position: absolute;
    left: 0;
    top: 0;
    display: flex;
    align-items: center;
    justify-content: center;

    transform: ${(props) => 
        props.clicked ? "translateX(89.3%)": "translateX(10%)"
    };
    transition: transform 0.5s;

    &::after,&::before{
        content:"";
        position: absolute;
        width: 100%;
        height: 100%;
        background-color: #58476a;
        z-index: -200;
    }

    &::before{
        top: 3rem;
        border-radius: 23px;
        border: 4px solid black
    }

    &::after{
        bottom: 3rem;
        border-radius: 23px 23px 0 0;
        border-top: 4px solid black;
        border-left: 4px solid black;
        border-right: 4px solid black;
    }
`;

const Box2 = styled.div`
    width: 45%;
    height: 100%;
    position: absolute;
    right: 0;
    top: 0;
    display: flex;
    align-items: center;
    justify-content: center;

    transform: ${(props) => 
        props.clicked ? "translateX(-123%)": "translateX(0%)"
    };
    transition: transform 0.5s;

    border-radius: ${(props) => 
        props.clicked ? "23px 0 0 23px": "0 23px 23px 0"
    };

    background-image: ${(props) =>
        props.clicked ? 'url(/images/signup2.png)': 'url(/images/signin2.png)'};
    background-size: cover;
    background-repeat: no-repeat;

`;

const Form = styled.form`
    width: 60%;
    display: flex;
    flex-direction: column;
    align-items: center;

    display: ${props => (props.show ? 'flex' : 'none')};

    ::placeholder {
        color: #a4d4cc;
    }
`;

const Input = styled.input`
    background-color: rgba(167,208,205,0.2);
    border: none;
    border-bottom: 2px solid #053271;
    padding: 1rem 2rem;
    margin: 0.5rem 0;
    width: 100%;
    font-family: 'Roboto';

    &:focus{
        outline: none;
        border: none;
    }

`;

const NavLink = styled(Link)`
    border-radius: 3px;
    padding: 1rem 3.5rem;
    margin-top: 2rem;
    border: 1px solid black;
    background-color: #a4d4cc;
    color: black;
    text-transform: uppercase;
    cursor: pointer;
    letter-spacing: 1px;
    box-shadow: 0 3px #999;
    font-family: 'Roboto';
    text-align: center;

    &:hover{
        background-color: #a7d0cd;
    }
    &:active{
        background-color: #a4d4cc;
        box-shadow: 0 3px #666;
        transform: translateY(4px);
    }
    &:focus{
        outline: none;
    }
`;



const Title = styled.h1`
    font-size: 3.5rem;
    margin-bottom: 2rem;
    font-family: 'Roboto';
    color: #a7d0cd;
    text-align: center;
`;

const ForgotLink = styled.a`
    text-decoration: none;
    color: #a7d0cd;
    margin-top: 1.5rem;
    font-size: 1.1rem;
    font-family: 'Roboto';
`;

const ButtonAnimate = styled.button`
    position: absolute;
    z-index: 1000;
    border: none;
    cursor: pointer;
    background-color: transparent;
    color: white;
    font-family: 'Roboto';
    background-color: rgba(167, 208, 205, 0.2);
    padding: 10px;
    text-align: center;
    top: 60%;
    
    right: ${(props) =>
        props.clicked ? "35%": "35%"
    };
    transition: right 0.5s;

    &::before{
        content: "Click Here";
        font-size: 2rem;
    }

    &:focus{
        outline: none;
    }

`;

const Text = styled.div`
    width: 80%;
    max-width: 400px;
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
    font-size: 1.5rem;
    letter-spacing: 0.1rem;
    color: white;
    font-family: 'Roboto';
    background-color: rgba(167, 208, 205, 0.2);
    padding: 10px;
    display: ${props => (props.show ? 'block' : 'none')};
`;


function SignInUpForm(){
    const [click, setClick] = useState(false);
    const handleClick = () => setClick(!click);

    const [checked, setChecked] = useState(false);
    const handleChange = (e) => setChecked(e.target.checked);

    return(
        <>
            <BackgroundBox clicked={click}>

                <Logo src="/images/logo.png" />

                <Box1 clicked={click}>
                    <Form className='signin' show={!click}>
                        <Title>Sign In</Title>
                        <Input type='email' name='email' id='emailId'
                        placeholder='Email'
                        />
                        <Input 
                        type='password' name='password' id='passwordId'
                        placeholder='Password'
                        />
                        <ForgotLink href='#'>Forgot your Password?</ForgotLink>
                        <NavLink to={'/home'} style={{ textDecoration: 'none' }}>Sign In</NavLink>
                    </Form>

                    <Form className='signup' show={click}>
                        <Title>Sign Up</Title>
                        <Input type='text' name='username' id='usernameId'
                        placeholder='Username'
                        />
                        <Input type='email' name='email' id='emailId'
                        placeholder='Email'
                        />
                        <Input 
                        type='password' name='password' id='passwordId'
                        placeholder='Password'
                        />

                        <Label>
                            <span>{checked ? 'Admin' : 'Employee'}</span>
                            <SwitchInput checked={checked} type="checkbox" onChange={handleChange} />
                            <Switch />
                        </Label>

                        <NavLink style={{ textDecoration: 'none' }}>Sign Up</NavLink>
                    </Form>
                </Box1>

                <Box2 clicked={click}>
                    <Text className='signintext' show={!click}>
                        <h1>Welcome Back</h1>
                        Don't have an account yet?
                        <br />
                        <ButtonAnimate clicked={click} onClick={handleClick}></ButtonAnimate>
                    </Text>
                    <Text className='signuptext' show={click}>
                        <h1>Hi There</h1>
                        Already have an account?
                        <br />
                        <ButtonAnimate clicked={click} onClick={handleClick}></ButtonAnimate>
                    </Text>
                    
                </Box2>

            </BackgroundBox>
        </>
    )
}

export default SignInUpForm;