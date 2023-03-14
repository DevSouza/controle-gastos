import axios, { AxiosError } from 'axios';
import { parseCookies, setCookie } from 'nookies';
import { useNavigate } from 'react-router-dom';

type FailedRequestQueueProps = {
  onSuccess: (token: string) => void;
  onFailure: (err: AxiosError) => void;
};

export const http = axios.create({
  baseURL: import.meta.env.VITE_GATEWAY_URL,
});

let isRefreshing = false;
let failedRequestsQueue: FailedRequestQueueProps[] = [];

export function setupInterceptorClient(signOut: () => void) {
  let cookies = parseCookies();

  http.interceptors.response.use(response => response, (error : AxiosError) => {
    // TODO: alterar retorno quando o refresh token estiver expirado.
    if(error.response?.status === 401) {
      const { code } = error.response.data as any;
  
      if(code === "token.expired") {
        cookies = parseCookies();
  
        const refreshToken  = cookies[import.meta.env.VITE_COOKIE_REFRESH_TOKEN];
        const originalConfig = error.config;
  
        if(!isRefreshing) {
          isRefreshing = true;
  
          http.post('/auth/refreshToken', {
            refreshToken: refreshToken
          }).then( response => {
            const { accessToken, tokenType } = response.data;
    
            setCookie(undefined, import.meta.env.VITE_COOKIE_TOKEN_TYPE, tokenType, {
              maxAge: 60 * 60 * 24 * 30, // 30 days
              path: '/',
            });
            setCookie(undefined, import.meta.env.VITE_COOKIE_ACCESS_TOKEN, accessToken, {
              maxAge: 60 * 60 * 24 * 30, // 30 days
              path: '/',
            });
            
            http.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;
            
            failedRequestsQueue.forEach(request => request.onSuccess(accessToken));
            failedRequestsQueue = [];
          })
          .catch(err => {
            failedRequestsQueue.forEach(request => request.onFailure(err));
            failedRequestsQueue = [];
            
            signOut();
          })
          .finally(() => {
            isRefreshing = false;
          });
        }
  
        return new Promise((resolve, reject) => {
          failedRequestsQueue.push({
            onSuccess: (token: string) => {
              if(!originalConfig?.headers) {
                return;
              }
  
              originalConfig.headers['Authorization'] = `Bearer ${token}`;
  
              resolve(http(originalConfig));
            },
            onFailure: (err: AxiosError) => {
              reject(err);
            }
          })
        });
      } else {
        if(signOut instanceof Function) {
          signOut();
        }
      }
    }
  
    return Promise.reject(error);
  });
  http.interceptors.request.use((config) => {
    const tokenType = cookies[import.meta.env.VITE_COOKIE_TOKEN_TYPE];
    const accessToken = cookies[import.meta.env.VITE_COOKIE_ACCESS_TOKEN];

    if(tokenType && accessToken){
      config.headers = {
        ...config.headers,
        Authorization: `${tokenType} ${accessToken}`
      };
    }
    return config;
  }, (error) => Promise.reject(error));

}