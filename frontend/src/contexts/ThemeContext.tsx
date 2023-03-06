import { createContext, ReactElement, useContext } from "react";

interface ThemeContextProps {
  children: ReactElement;
}

type ThemeContextData = {}

const ThemeContext = createContext({} as ThemeContextData )

export function ThemeContextProvider({ children }: ThemeContextProps) {

  return (
    <ThemeContext.Provider value={{}}>
      {children}
    </ThemeContext.Provider>
  )
}

export const useTheme = () => useContext(ThemeContext)