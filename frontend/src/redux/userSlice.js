import { createSlice } from '@reduxjs/toolkit';

const initialState = [
    { id: 101, username: 'JillayneHazlett', email: 'JillayneHazlett@moes.com', password: 'password', joined: '14/05/2023', type: 'admin', pending: 'no' },
    { id: 102, username: 'DoriceGemini', email: 'DoriceGemini@moes.com', password: 'password', joined: '30/09/2021', type: 'employee', pending: 'no' },
    { id: 103, username: 'StarlaBarrus', email: 'StarlaBarrus@moes.com', password: 'password', joined: '18/11/2018', type: 'employee', pending: 'no' },
    { id: 104, username: 'SindeeBlake', email: 'SindeeBlake@moes.com', password: 'password', joined: '11/04/2021', type: 'employee', pending: 'no' },
    { id: 105, username: 'ModestiaHashim', email: 'ModestiaHashim@moes.com', password: 'password', joined: '05/03/2019', type: 'admin', pending: 'yes' },
    { id: 106, username: 'KaylaKelula', email: 'KaylaKelula@moes.com', password: 'password', joined: '25/08/2022', type: 'employee', pending: 'yes' },
    { id: 107, username: 'AllisJena', email: 'AllisJena@moes.com', password: 'password', joined: '20/01/2019', type: 'admin', pending: 'yes' },
    { id: 108, username: 'ThaliaAde', email: 'ThaliaAde@moes.com', password: 'password', joined: '04/03/2020', type: 'employee', pending: 'yes' },
    { id: 109, username: 'DevinaGeoras', email: 'DevinaGeoras@moes.com', password: 'password', joined: '01/06/2018', type: 'employee', pending: 'yes' },
  ];

const userSlice = createSlice({
  name: 'users',
  initialState,
  reducers: {

    acceptUser: (state, action) => {
      const userFound = state.find(user => user.id === action.payload)
      if(userFound){
        userFound.pending = 'no'
      }
    },

    rejectUser: (state, action) => {
      const userFound = state.find(user => user.id === action.payload)
      if(userFound){
        state.splice(state.indexOf(userFound), 1)
      }
    },

    deleteUser: (state, action) => {
      const userFound = state.find(user => user.id === action.payload)
      if(userFound){
        state.splice(state.indexOf(userFound), 1)
      }
    }

  },
});

export const { acceptUser, rejectUser, deleteUser } = userSlice.actions
export default userSlice.reducer;
