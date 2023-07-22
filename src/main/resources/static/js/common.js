// request拦截器，将用户token放入头中
axios.interceptors.request.use(
    config => {
        let token = sessionStorage.getItem("token");
        alert(token);
        if(token)
        {
            config.headers['Authorization'] = token;
        }
        return config;
    },
    error => {
        console.log(error);
        return Promise.reject(error);
    }
)