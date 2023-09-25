import { createSlice } from '@reduxjs/toolkit';

const initialState = [
    { id: 101, username: 'JillayneHazlett', email: 'JillayneHazlett@moes.com', password: 'password', role: 'ADMIN' },
    { id: 102, username: 'DoriceGemini', email: 'DoriceGemini@moes.com', password: 'password', role: 'EMPLOYEE' },
    { id: 103, username: 'StarlaBarrus', email: 'StarlaBarrus@moes.com', password: 'password', role: 'EMPLOYEE' },
    { id: 104, username: 'SindeeBlake', email: 'SindeeBlake@moes.com', password: 'password', role: 'EMPLOYEE' },
    { id: 105, username: 'ModestiaHashim', email: 'ModestiaHashim@moes.com', password: 'password', role: 'MANAGER' },
    { id: 106, username: 'KaylaKelula', email: 'KaylaKelula@moes.com', password: 'password', role: 'EMPLOYEE' },
    { id: 107, username: 'AllisJena', email: 'AllisJena@moes.com', password: 'password', role: 'MANAGER' },
    { id: 108, username: 'ThaliaAde', email: 'ThaliaAde@moes.com', password: 'password', role: 'EMPLOYEE' },
    { id: 109, username: 'DevinaGeoras', email: 'DevinaGeoras@moes.com', password: 'password', role: 'EMPLOYEE' },
  ];

const userSlice = createSlice({
  name: 'users',
  initialState,
  reducers: {

    // acceptUser: (state, action) => {
    //   const userFound = state.find(user => user.id === action.payload)
    //   if(userFound){
    //     userFound.pending = 'no'
    //   }
    // },

    // rejectUser: (state, action) => {
    //   const userFound = state.find(user => user.id === action.payload)
    //   if(userFound){
    //     state.splice(state.indexOf(userFound), 1)
    //   }
    // },

    // deleteUser: (state, action) => {
    //   const userFound = state.find(user => user.id === action.payload)
    //   if(userFound){
    //     state.splice(state.indexOf(userFound), 1)
    //   }
    // }

    validateUser: (state, action) => {
      const userFound = state.find(user => user.id === action.payload)
      if(userFound){
        userFound.role = 'VALIDATEDEMPLOYEE'
      }
    },

    upgradeUser: (state, action) => {
      const userFound = state.find(user => user.id === action.payload)
      if(userFound){
        userFound.role = 'MANAGER'
      }
    },

  },
});

// export const { acceptUser, rejectUser, deleteUser } = userSlice.actions
export const { validateUser, upgradeUser } = userSlice.actions
export default userSlice.reducer;
