import { createContext, ReactNode, useContext, useEffect, useState } from "react";
import { setCookie, parseCookies, destroyCookie } from 'nookies';
import { useNavigate } from "react-router-dom";
import jwtDecode from "jwt-decode";
import { LoadingPage } from "../pages/LoadingPage";
import { http } from "../services/api";

type User = {
  userId: string;
  email: string;
  username: string;
}

type SignInCredentials = {
  username: string;
  password: string;
}

type AuthContextData = {
  signIn(credentials: SignInCredentials): Promise<void>;
  signOut: () => void;
  isAuthenticated: boolean;
  user: User | undefined;
}

type AuthProviderProps = {
  children: ReactNode;
}

const AuthContext = createContext({} as AuthContextData);

export function AuthProvider({ children } : AuthProviderProps) {
  const cookies = parseCookies();
  const [user, setUser] = useState<User>();
  const isAuthenticated = !!user;
  const [loading, setLoading] = useState(true);

  const navigate = useNavigate();

  useEffect(() => {
    async function loadStoragedData() {

      try {
        const accessToken = cookies[import.meta.env.VITE_COOKIE_ACCESS_TOKEN];
        
        if(accessToken) {
          const { username, email, sub } = jwtDecode<{username: string, email: string, sub: string}>(accessToken);
          setUser({ username, email, userId: sub });
        }
        
        setLoading(false);
      } catch(err) {
        signOut();
        setLoading(false);
      }
    }

    loadStoragedData();
  }, []);

  function signOut() {
    destroyCookie(undefined, import.meta.env.VITE_COOKIE_TOKEN_TYPE);
    destroyCookie(undefined, import.meta.env.VITE_COOKIE_ACCESS_TOKEN);
    destroyCookie(undefined, import.meta.env.VITE_COOKIE_REFRESH_TOKEN);
  
    setUser(undefined);

    navigate('/');
  }

  async function signIn({username, password}: SignInCredentials){
    try {
      const responseAuth = await http.post('/auth/signin', {
        username,
        password
      });

      const {
        tokenType,
        accessToken,
        refreshToken,
      } = responseAuth.data;
      
      setCookie(undefined, import.meta.env.VITE_COOKIE_TOKEN_TYPE, tokenType, {
        maxAge: 60 * 60 * 24 * 30, // 30 days
        path: '/',
      });
      
      setCookie(undefined, import.meta.env.VITE_COOKIE_ACCESS_TOKEN, accessToken, {
        maxAge: 60 * 60 * 24 * 30, // 30 days
        path: '/',
      });

      setCookie(undefined, import.meta.env.VITE_COOKIE_REFRESH_TOKEN, refreshToken, {
        maxAge: 60 * 60 * 24 * 30, // 30 days
        path: '/',
      });

      http.defaults.headers.common['Authorization'] = `${tokenType} ${accessToken}`;

      const { email, sub } = jwtDecode<{ email: string, sub: string }>(accessToken);
      setUser({ username, email, userId: sub });

      navigate('/dashboard');
    } catch (err) {
      throw err;
    } finally {
      setLoading(false);
    }
  }

  if(loading) {
    return <LoadingPage />;
  }
  
  return (
    <AuthContext.Provider value={{signIn, signOut, isAuthenticated, user}}>
      { children }
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}