import axios from 'axios';

//const API_URL = 'http://localhost:8080/api/auth/';
const API_URL = "http://localhost:8080/"

class AuthService {
  register_route = "register";
  login_route = "login";
  login(user) {
    return axios
      .post(API_URL + this.login_route, {
        username: user.username,
        password: user.password
      })
      .then(response => {
        if (response.data.accessToken) {
          localStorage.setItem('user', JSON.stringify(response.data));
        }

        return response.data;
      });
  }

  logout() {
    localStorage.removeItem('user');
  }

  register(user) {
    return axios.post(API_URL + this.register_route, {
      username: user.username,
      email: user.email,
      password: user.password
    })
    // .then(response => {
    //   if (response.data.accessToken) {
    //     localStorage.setItem('user', JSON.stringify(response.data));
    //   }

    //   return response.data;
    // });

  }
}

export default new AuthService();

