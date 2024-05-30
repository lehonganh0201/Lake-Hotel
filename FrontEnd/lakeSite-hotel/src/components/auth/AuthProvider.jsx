import { jwtDecode } from 'jwt-decode';
import React, { createContext, useContext, useState } from 'react'

export const AuthContext = createContext({
    user : null,
    handleLogin : (token) => {},
    handleLogout : () => {}
});

export const AuthProvider = ({children}) => {
    const [user, setUser] = useState(null);

    const handleLogin = (token) =>{
        const decodedToken = jwtDecode(token);
        localStorage.setItem("token", token);
        localStorage.setItem("userId",decodedToken.sub);
        localStorage.setItem("userRole",decodedToken.role);
        setUser(decodedToken);
    }

    const handleLogout = () =>{
        localStorage.removeItem("token");
        localStorage.removeItem("userId");
        localStorage.removeItem("userRole");
        setUser(null);
    }
  return (
    <AuthContext.Provider value={{user, handleLogin, handleLogout}}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () =>{
    return useContext(AuthContext);
}
