import axios from "axios";

let url='http://localhost:8080/api/user';

export async function emailSender(emailData){
    const response = await axios.post((url+"/send-email"),emailData);
    return response.data;
}