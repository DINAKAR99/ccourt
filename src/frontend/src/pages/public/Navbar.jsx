// Navbar.jsx
import axios from "axios";
import React from "react";
import { Link } from "react-router-dom";

const Navbar = () => {
  const handleClick = async () => {
    try {
      const params = new URLSearchParams();
      params.append('username', data.username);
      params.append('password', data.password);
  
      const response = await axios.get(
        "http://localhost:8080/court/logout",
          
        {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
          withCredentials: true,
        }
      );
    
      if (response.status === 200) {
        console.log("Logut successful:", response.data);
        navigate("/"); // Handle successful login, e.g., redirect or store token
       }
    } catch (error) {
      
    }
  };
  return (
    <nav
      className="navbar navbar-expand-lg navbar-light"
      style={{
        backgroundColor: "white",
        boxShadow: "0 4px 40px rgba(0, 0, 0, 0.4)",
      }}
    >
      <div className="container-fluid">
        <Link className="navbar-brand" to="/">
          MyApp
        </Link>
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
          aria-controls="navbarNav"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav ms-auto">
            <li className="nav-item dropdown">
              <Link
                className="nav-link dropdown-toggle"
                to="#"
                id="servicesDropdown"
                role="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
              >
                Services
              </Link>
              <ul className="dropdown-menu" aria-labelledby="servicesDropdown">
                <li>
                  <Link className="dropdown-item" to="/service1">
                    Service 1
                  </Link>
                </li>
                <li>
                  <Link className="dropdown-item" to="/service2">
                    Service 2
                  </Link>
                </li>
                <li>
                  <Link className="dropdown-item" to="/service3">
                    Service 3
                  </Link>
                </li>
              </ul>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/about">
                About
              </Link>
            </li>
             
            <li className="nav-item">
              <Link className="nav-link" to="/signup">
                Signup
              </Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/login">
                Login
              </Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" onClick={handleClick}>
                Logout
              </Link>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
