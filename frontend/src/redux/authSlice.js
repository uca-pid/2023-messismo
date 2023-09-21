import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';

const initialState = {
  msg: '',
  user: '',
  token: '',
  loading: false,
  error: '',
};

export const signInUser = createAsyncThunk('signinuser', async(body) => {
  const res = await fetch("ddddddddd", {
    method: "post",
    headers:{
      'Content-Type': "application/json",
    },
    body: JSON.stringify(body)
  })
  return await res.json();
})

export const signUpUser = createAsyncThunk('signupuser', async(body) => {
  const res = await fetch("dddddddd", {
    method: "post",
    headers:{
      'Content-Type': "application/json",
    },
    body: JSON.stringify(body)
  })
  return await res.json();
})

const authSlice = createSlice({
  name: 'auth',
  initialState,

  reducers: {

    addToken: (state, action) => {
      state.token = localStorage.getItem("token")
    },

    addUser: (state, action) => {
      state.user = localStorage.getItem("user")
    },

    signout: (state, action) => {
      state.token = null;
      localStorage.clear();
    },

  },

  extraReducers:{

    //v SignIn v//
    [signInUser.pending]: (state, action) => {
      state.loading = true
    },

    [signInUser.fulfilled]: (state, {payload:{error, msg, token, user}}) => {
      state.loading = false;
      if(error){
        state.error = error
      }
      else{
        state.msg = msg;
        state.token = token;
        state.user = user;

        localStorage.setItem('msg', msg)
        localStorage.setItem('token', token)
        localStorage.setItem('user', JSON.stringify(user))
      }
    },

    [signInUser.rejected]: (state, action) => {
      state.loading = true
    },
    //^ SignIn ^//

    //v SignUp v//
    [signUpUser.pending]: (state, action) => {
      state.loading = true
    },

    [signUpUser.fulfilled]: (state, {payload:{error, msg}}) => {
      state.loading = false;
      if(error){
        state.error = error
      }
      else{
        state.msg = msg
      }
    },

    [signUpUser.rejected]: (state, action) => {
      state.loading = true
    },
    //^ SignUp ^//
  }
  
});

export const { addToken, addUser, signout } = authSlice.actions;
export default authSlice.reducer;