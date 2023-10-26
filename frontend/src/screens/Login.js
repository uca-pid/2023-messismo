import React, { useState, useEffect, forwardRef } from "react";
import "./Login.css";
import "../App.css";
import image from "../images/signin2.png";
import logo from "../images/logo.png";
import { Link, Navigate, useNavigate } from "react-router-dom";
import { FaUserShield } from "react-icons/fa";
import { BsFillShieldLockFill } from "react-icons/bs";
import { MdEmail } from "react-icons/md";
import styled from "styled-components";
import { useDispatch, useSelector } from "react-redux";
import SUpPopUp from "../components/SignUpPopUp";
import SInPopUp from "../components/SignInPopUp";
import signupvalidation from "../SignUpValidation";
import signinvalidation from "../SignInValidation";
import { login, register } from "../redux/auth";
import { clearMessage } from "../redux/message";
import Stack from "@mui/material/Stack";
import Snackbar from "@mui/material/Snackbar";
import MuiAlert from "@mui/material/Alert";
import Alert from "@mui/material/Alert";
import Dialog from "@mui/material/Dialog";
import TextField from "@mui/material/TextField";
import authService from "../services/auth.service";
import RecoverPasswordValidation from "../RecoverPasswordValidation";
import ChangePasswordValidation from "../ChangePasswordValidation";
import DialogContent from "@mui/material/DialogContent";
import Button from "@mui/material/Button";

const ForgotPassword = styled.a`
  text-decoration: none;
  color: #a7d0cd;
`;

const ErrorMessage = styled.h4`
  color: red;
  font-family: "Roboto";
  width: 240px;
  word-wrap: break-word;
`;

const Alert2 = forwardRef(function Alert(props, ref) {
  return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

function Login() {
  const [open, setOpen] = useState(false);

  const [isRegistered, setIsRegistered] = useState(false);

  const [isSignInValid, setIsSignInValid] = useState(false);
  const [signinvalues, setSignInValues] = useState({
    email: "",
    password: "",
  });
  const [signinerrors, setSignInErrors] = useState({});
  const [SignInPopUp, setSignInPopUp] = useState(false);

  const { isLoggedIn } = useSelector((state) => state.auth);
  const { message } = useSelector((state) => state.message);
  const { user: currentUser } = useSelector((state) => state.auth);

  let navigate = useNavigate();
  const dispatch = useDispatch();
  const [errors, setErrors] = useState({});
  const [openForm, setOpenForm] = useState(false);
  const [email, setEmail] = useState("");
  const [pin, setPin] = useState("");
  const [password, setPassword] = useState("");
  const [openChangePasswordForm, setOpenChangePasswordForm] = useState(false);
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [alertText, setAlertText] = useState("");
  const [isOperationSuccessful, setIsOperationSuccessful] = useState(false);
  const [repeatPassword, setRepeatPassword] = useState("");

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
        if (
          userRole === "ADMIN" ||
          userRole === "MANAGER" ||
          userRole === "VALIDATEDEMPLOYEE"
        ) {
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

  if (
    isLoggedIn &&
    (currentUser.role === "ADMIN" ||
      currentUser.role === "MANAGER" ||
      currentUser.role === "VALIDATEDEMPLOYEE")
  ) {
    return <Navigate to="/homepage" />;
  }

  const handleSignInInput = (e) => {
    setSignInValues({ ...signinvalues, [e.target.name]: e.target.value });
  };

  const handleSignInErrors = (e) => {
    setSignInErrors((prevErrors) => ({
      ...prevErrors,
      [e.target.name]: "",
    }));
  };

  function handleSignInValidation() {
    const validationErrors = signinvalidation(signinvalues);
    setSignInErrors(validationErrors);
    if (Object.keys(validationErrors).length === 0) {
      setIsSignInValid(true);
    } else {
      setIsSignInValid(false);
      handleSnackClick();
    }
  }

  const handleSnackClick = () => {
    setOpen(true);
  };

  const handleSnackClose = (event, reason) => {
    if (reason === "clickaway") {
      return;
    }

    setOpen(false);
  };

  const handleOpenForm = () => {
    setOpenForm(true);
  };
  const handleCloseForm = () => {
    setOpenForm(false);
  };

  const handleInputChange = (e) => {
    setEmail(e.target.value);
  };

  const handleSendEmail = () => {
    const validationErrors = RecoverPasswordValidation({
      email,
    });

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      console.log(validationErrors);
    } else {
      authService
        .forgotPassword(email)
        .then((response) => {
          setAlertText("Email sent succesfully!");
          setIsOperationSuccessful(true);
          setOpenSnackbar(true);
          handleCloseForm();
          setOpenChangePasswordForm(true);
        })
        .catch((error) => {
          setAlertText("Error sending password recovery email");
          setIsOperationSuccessful(false);
          setOpenSnackbar(true);
        });
      setEmail("");
    }
  };

  const handleOpenChangePasswordForm = () => {
    setOpenChangePasswordForm(true);
  };

  const handleCloseChangePasswordForm = () => {
    setOpenChangePasswordForm(false);
  };

  const handlePasswordChange = (e) => {
    setPassword(e.target.value);
  };

  const handleRepeatPasswordChange = (e) => {
    setRepeatPassword(e.target.value);
  };

  const handlePinChange = (e) => {
    setPin(e.target.value);
  };

  const handleChangePassword = () => {
    const form = {
      email: email,
      newPassword: password,
      pin: pin,
    };

    const validationErrors = ChangePasswordValidation({
      email,
      password,
      pin,
      repeatPassword,
    });

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      console.log(validationErrors);
    } else {
      authService
        .changePassword(form)
        .then((response) => {
          setAlertText("Password changed succesfully!");
          setIsOperationSuccessful(true);
          setOpenSnackbar(true);
          setOpenChangePasswordForm(false);
        })
        .catch((error) => {
          setAlertText("Error changing password");
          setIsOperationSuccessful(false);
          setOpenSnackbar(true);
        });
      setEmail("");
      setPassword("");
      setPin("");
    }
  };

  const handleChange = (e) => {
    e.preventDefault();
  };

  return (
    <div>
      <div className="loginPage flx">
        <div className="cntainr flx">
          <div className="formDiv flx">
            <form action="" className="form grd">
              <div className="headerDiv">
                <img src={logo} className="logoimg"></img>
              </div>

              <div className="inputDiv">
                <label htmlFor="email" className="labl">
                  Email
                </label>
                <div className="inpt flx">
                  <MdEmail className="icn" />
                  <input
                    type="email"
                    name="email"
                    id="emailSiId"
                    placeholder="Enter your email"
                    className="inpt"
                    onChange={handleSignInInput}
                    onBlur={handleSignInErrors}
                  />
                </div>
                {signinerrors.email && (
                  <ErrorMessage>{signinerrors.email}</ErrorMessage>
                )}
              </div>

              <div className="inputDiv">
                <label htmlFor="password" className="labl">
                  Password
                </label>
                <div className="inpt flx">
                  <BsFillShieldLockFill className="icn" />
                  <input
                    type="password"
                    name="password"
                    id="passwordSiId"
                    placeholder="Enter your password"
                    className="inpt"
                    onChange={handleSignInInput}
                    onBlur={handleSignInErrors}
                  />
                </div>
                {signinerrors.password && (
                  <ErrorMessage>{signinerrors.password}</ErrorMessage>
                )}
              </div>

              <Link
                className="btn flx"
                onClick={() => {
                  handleSignInValidation();
                  if (isSignInValid) {
                    const userData = {
                      email: signinvalues.email,
                      password: signinvalues.password,
                    };
                    handleLogin(userData);
                  }
                }}
                disabled={
                  Object.keys(signinerrors).length > 0 || !isSignInValid
                }
              >
                <span>Sign In</span>
              </Link>

              <span className="forgotPassword">
                <ForgotPassword
                  onClick={(e) => {
                    e.preventDefault();
                    handleOpenForm();
                  }}
                >
                  Forgot your Password?
                </ForgotPassword>
              </span>
              <Dialog
                open={openForm}
                dividers={true}
                onClose={handleCloseForm}
                aria-labelledby="form-dialog-title"
                className="custom-dialog"
                maxWidth="sm"
                fullWidth
              >
                <DialogContent>
                  <div>
  
                    <h1 style={{ marginBottom: "3%", fontSize: "1.6rem"}}>
                      Password Recovery
                    </h1>
                    <hr
                      style={{
                        borderTop: "1px solid grey",
                        marginBottom: "3%",
                        width: "100%",
                      }}
                    />
                    <p>
                      Please enter you email to receive a link to reset your
                      password
                    </p>
                    <TextField
                      required
                      id="name"
                      value={email}
                      onChange={handleInputChange}
                      variant="outlined"
                      error={errors.email ? true : false}
                      helperText={errors.email || ""}
                      style={{
                        width: "80%",
                        marginTop: "3%",
                        marginBottom: "3%",
                        fontSize: "1.1rem",
                      }}
                      InputProps={{
                        style: {
                          fontSize: "1rem",
                        },
                      }}
                      FormHelperTextProps={{
                        style: {
                          fontSize: "1.1rem",
                        },
                      }}
                    />

                    <div
                      className="buttons"
                      style={{
                        flex: 1,
                        display: "flex",
                        flexDirection: "row",
                        justifyContent: "flex-end",
                      }}
                    >
                      <Button
                        variant="outlined"
                        style={{
                          color: "grey",
                          borderColor: "grey",
                          fontSize: "1rem",
                        }}
                        onClick={handleCloseForm}
                      >
                        Cancel
                      </Button>
                      <Button
                        variant="contained"
                        style={{ marginLeft: "3%", fontSize: "1rem", backgroundColor: "#a4d4cc", color: "black" }}
                        onClick={handleSendEmail}
                      >
                        Send
                      </Button>
                    </div>
                  </div>
                </DialogContent>
              </Dialog>
              <Dialog
                open={openChangePasswordForm}
                dividers={true}
                onClose={handleCloseChangePasswordForm}
                aria-labelledby="form-dialog-title"
                className="custom-dialog"
                maxWidth="sm"
                fullWidth
              >
                <DialogContent>
                  <div>
                    <h1 style={{ marginBottom: "3%", fontSize: "1.6rem" }}>
                      Change Password
                    </h1>
                    <hr
                      style={{
                        borderTop: "1px solid lightgrey",
                        marginBottom: "3%",
                        width: "100%",
                      }}
                    />
                    <p style={{ color: errors.email ? "red" : "black" }}>
                      Email *
                    </p>
                    <TextField
                      required
                      id="name"
                      value={email}
                      onChange={handleInputChange}
                      variant="outlined"
                      error={errors.email ? true : false}
                      helperText={errors.email || ""}
                      style={{
                        width: "80%",
                        marginTop: "3%",
                        marginBottom: "3%",
                        fontSize: "1.1rem",
                      }}
                      InputProps={{
                        style: {
                          fontSize: "1.1rem",
                        },
                      }}
                      FormHelperTextProps={{
                        style: {
                          fontSize: "1.1rem",
                        },
                      }}
                    />
                    <p style={{ color: errors.pin ? "red" : "black" }}>Pin *</p>
                    <TextField
                      required
                      id="name"
                      value={pin}
                      onChange={handlePinChange}
                      variant="outlined"
                      error={errors.pin ? true : false}
                      helperText={errors.pin || ""}
                      style={{
                        width: "80%",
                        marginTop: "3%",
                        marginBottom: "3%",
                        fontSize: "1.1rem",
                      }}
                      InputProps={{
                        style: {
                          fontSize: "1.1rem",
                        },
                      }}
                      FormHelperTextProps={{
                        style: {
                          fontSize: "1.1rem",
                        },
                      }}
                    />
                    <p style={{ color: errors.password ? "red" : "black" }}>
                      New Password *
                    </p>
                    <TextField
                      required
                      id="password"
                      type="password"
                      value={password}
                      onChange={handlePasswordChange}
                      variant="outlined"
                      error={errors.password ? true : false}
                      helperText={errors.password || ""}
                      style={{
                        width: "80%",
                        marginTop: "3%",
                        marginBottom: "3%",
                        fontSize: "1.1rem",
                      }}
                      InputProps={{
                        style: {
                          fontSize: "1.1rem",
                        },
                      }}
                      FormHelperTextProps={{
                        style: {
                          fontSize: "1.1rem",
                        },
                      }}
                    />
                    <p style={{ color: errors.password ? "red" : "black" }}>
                      Repeat Password *
                    </p>
                    <TextField
                      required
                      id="password"
                      type="password"
                      value={repeatPassword}
                      onChange={handleRepeatPasswordChange}
                      onCut={handleChange}
                      onCopy={handleChange}
                      onPaste={handleChange}
                      variant="outlined"
                      error={errors.repeatPassword ? true : false}
                      helperText={errors.repeatPassword || ""}
                      style={{
                        width: "80%",
                        marginTop: "3%",
                        marginBottom: "3%",
                        fontSize: "1.1rem",
                      }}
                      InputProps={{
                        style: {
                          fontSize: "1.1rem",
                        },
                      }}
                      FormHelperTextProps={{
                        style: {
                          fontSize: "1.1rem",
                        },
                      }}
                    />
                    <div
                      className="buttons"
                      style={{
                        flex: 1,
                        display: "flex",
                        flexDirection: "row",
                        justifyContent: "flex-end",
                      }}
                    >
                      <Button
                        variant="outlined"
                        style={{
                          color: "grey",
                          borderColor: "grey",
                          fontSize: "1rem",
                        }}
                        onClick={handleCloseChangePasswordForm}
                      >
                        Cancel
                      </Button>
                      <Button
                        variant="contained"
                        style={{ marginLeft: "3%", fontSize: "1rem", backgroundColor: "#a4d4cc", color: "black" }}
                        onClick={handleChangePassword}
                      >
                        Change Password
                      </Button>
                    </div>
                  </div>
                </DialogContent>
              </Dialog>
            </form>
          </div>

          <div className="imageDiv">
            <img src={image} className="imag"></img>

            <div className="textDiv">
              <h2 className="title">Welcome Back</h2>
              <p className="subtitle">
                Please log in to your account to get started
              </p>
            </div>

            <div className="footerDiv flx">
              <span className="text">Don't have an account?</span>
              <Link to={"/register"}>
                <button className="btn">Sign Up</button>
              </Link>
            </div>
          </div>
        </div>

        {SignInPopUp && (
          <SInPopUp
            setSignInPopUp={setSignInPopUp}
            isRegistered={isRegistered}
          />
        )}
      </div>

      <Stack spacing={2} sx={{ width: "100%" }}>
        <Snackbar
          open={open}
          autoHideDuration={2500}
          onClose={handleSnackClose}
          anchorOrigin={{ vertical: "bottom", horizontal: "left" }}
        >
          <Alert2
            onClose={handleSnackClose}
            severity="error"
            sx={{ width: "100%" }}
          >
            <h2 style={{ fontFamily: "Roboto", fontSize: "1rem" }}>
              One or more fields are empty/incorrect
            </h2>
          </Alert2>
        </Snackbar>
      </Stack>
      <Snackbar
        open={openSnackbar}
        autoHideDuration={10000}
        onClose={() => setOpenSnackbar(false)}
        anchorOrigin={{ vertical: "bottom", horizontal: "left" }}
      >
        <Alert
          onClose={() => setOpenSnackbar(false)}
          variant="filled"
          severity={isOperationSuccessful ? "success" : "error"}
          sx={{ fontSize: "100%" }}
        >
          {alertText}
        </Alert>
      </Snackbar>
    </div>
  );
}

export default Login;
