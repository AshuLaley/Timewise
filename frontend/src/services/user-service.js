import { myAxios } from "./helper";

export const signUp = (user) => {

    return myAxios
        .post('/api/v1/auth/user/register',user)
        .then((response) => response.data);

    
}


export const loginUser = (loginDetail)=>{
    return myAxios.post('/api/v1/auth/authenticate',loginDetail).then((response)=>response.data)
}