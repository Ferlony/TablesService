import axios from 'axios';
import authHeader from './user.service';

const API_URL = 'http://localhost:8080/';

class UserService {
  // getPublicContent() {
  //   return axios.get(API_URL + 'home', { headers: authHeader() });
  // }

  postPublicContent(data){
    return axios.post(API_URL + 'home', {data, headers: authHeader()})
  }

  // getUserBoard() {
  //   return axios.get(API_URL + 'user', { headers: authHeader() });
  // }

  getModeratorBoard() {
    return axios.get(API_URL + 'mod', { headers: authHeader() });
  }

  getAdminBoard() {
    return axios.get(API_URL + 'admin', { headers: authHeader() });
  }

  postTableToBD(data){
    return axios.post(API_URL + 'savetable', {data,  headers: authHeader()});
  }

  // getTables(){
  //   return axios.get(API_URL + 'admin/tables', {headers: authHeader()});
  // }

  // getUsers(){
  //   return axios.get(API_URL + 'admin/users', {headers: authHeader()});
  // }

  postChangeUserField(data){
    return axios.post(API_URL + "admin/changeusers", {data})
  }

  postUploadFile(data){
    return axios.post(API_URL + "admin/upload", data, {headers: authHeader(),  'Content-Type': 'multipart/form-data'})
  }
}

export default new UserService();

