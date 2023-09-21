import { createSlice } from '@reduxjs/toolkit';

const navSlice = createSlice({

  name: 'navigation',
  initialState: {
    clicked: false,
  },

  reducers: {

    toggleClicked: (state) => {
        state.clicked = !state.clicked;
    },

    resetClicked: (state) => {
        state.clicked = false;
    },

  },

});

export const { toggleClicked, resetClicked } = navSlice.actions;
export default navSlice.reducer;