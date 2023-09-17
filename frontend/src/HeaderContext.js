import React, { createContext, useContext, useState } from 'react';

const HeaderContext = createContext();

export const HeaderProvider = ({ children }) => {
  const [open, setOpen] = useState(true);

  return (
    <HeaderContext.Provider value={{ open, setOpen }}>
      {children}
    </HeaderContext.Provider>
  );
};

export const useHeaderContext = () => {
  return useContext(HeaderContext);
};