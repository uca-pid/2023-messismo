import { createSlice } from '@reduxjs/toolkit';

const filtersSlice = createSlice({
  name: 'filters',
  initialState: {
    selectedCategories: [],
    minValue: '',
    maxValue: '',
    minStock: '',
    maxStock: '',
  },
  reducers: {
    setSelectedCategories: (state, action) => {
      state.selectedCategories = action.payload;
    },
    setMinValue: (state, action) => {
      state.minValue = action.payload;
    },
    setMaxValue: (state, action) => {
      state.maxValue = action.payload;
    },
    setMinStock: (state, action) => {
      state.minStock = action.payload;
    },
    setMaxStock: (state, action) => {
      state.maxStock = action.payload;
    },
  },
});

export const { setSelectedCategories, setMinValue, setMaxValue, setMinStock, setMaxStock } = filtersSlice.actions;

export default filtersSlice.reducer;
