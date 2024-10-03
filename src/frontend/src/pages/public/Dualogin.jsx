import React from 'react'
import { useNavigate } from 'react-router-dom';
import PublicLayout from '../../Layouts/PublicLayout';
import axios from 'axios';

const Dualogin = () => {
    const navigate = useNavigate(); // Get the navigate function

     const onSubmit = async () => {
    try {
      const response = await axios.get(
        "http://localhost:8080/court/dualsessionlogin",
          // Your login data
        {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
          withCredentials: true,
        }
      );
      if (response.status === 200) {
        console.log("Login successful:", response.data);
        navigate("/protected"); // Handle successful login, e.g., redirect or store token
       }
    }
      
    catch (error) {
      console.error('Error:', error);
    }}

    
  return (
    <PublicLayout>
        <div>Dualogin</div>
        <div>yopu are logged in somewhere else </div>
        <div><button onClick={onSubmit}>click here to login</button> </div>
        <h6>this action will logout other existing session </h6>
    </PublicLayout>
  )
}

export default Dualogin