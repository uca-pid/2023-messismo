import { createSlice } from '@reduxjs/toolkit';

const filtersSlice = createSlice({
  name: 'dashboards',
  initialState: {
    selectedDate: "",
    
  },
  reducers: {
    setSelectedDate: (state, action) => {
      state.selectedDate = action.payload;
    },
  },
});

export const { setSelectedDate } = filtersSlice.actions;

export default filtersSlice.reducer;
