import React, { useEffect, useRef, useState } from "react";
import PublicLayout from "../../Layouts/PublicLayout";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const checkUserExists = async (name) => {
  try {
    const response = await fetch(`http://localhost:8080/court/api/${name}`);
    if (!response.ok) {
      return false; // User not found
    }
    return true; // User exists
  } catch (error) {
    // Log the error for debugging purposes (optional)
    console.error("Error checking user:", error.message);
    return false; // Consider this as a failed validation
  }
};

const Login = () => {
  const [captcha, setCaptcha] = useState(generateCaptcha());
  const [message, setMessage] = useState("");
  const canvasRef = useRef(null);
  const navigate = useNavigate(); // Get the navigate function
  // Define the validation schema
  const schema = z.object({
    username: z
      .string()
      .min(1, { message: "Required" })
      .refine((name) => name.toLowerCase() !== "justin", {
        message: "Name cannot be 'Justin'",
      })
      .refine(
        async (name) => {
          try {
            const response = await fetch(
              `http://localhost:8080/court/api/${name}`
            );
            if (!response.ok) {
              return false; // User not found
            }
            return true; // User exists
          } catch (error) {
            // Log the error for debugging purposes (optional)
            console.error("Error checking user:", error.message);
            return false; // Consider this as a failed validation
          }
        },
        {
          message: "User not found", // Error message if user does not exist
        }
      ),
    password: z.string().min(1, { message: "Required" }),
    captcha: z
      .string()
      .min(1, { message: "Required" })
      .refine(
        (captchaa) => {
          if (captchaa === captcha) {
            return true;
          } else {
            return false;
          }
        },
        {
          message: "incorrect captcha",
        }
      ),
  });

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({ resolver: zodResolver(schema) });

  function generateCaptcha() {
    const chars =
      "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    let captcha = "";
    for (let i = 0; i < 6; i++) {
      captcha += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return captcha;
  }

  const drawCaptcha = () => {
    const canvas = canvasRef.current;
    const context = canvas.getContext("2d");
    context.clearRect(0, 0, canvas.width, canvas.height);
    context.font = "30px Arial";
    context.fillText(captcha, 90, 35);
  };

  useEffect(() => {
    drawCaptcha();
  }, [captcha]);

  const handleReloadCaptcha = () => {
    setCaptcha(generateCaptcha());
  };

  const onSubmit = async (data) => {
    try {
      const response = await axios.post(
        "http://localhost:8080/court/login",
        data, // Your login data
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
        setMessage("Login successful!"); 
      }
    } catch (error) {
      if (error.response) {
        if (error.response.status === 401) {
          console.error("Incorrect login attempt:", error.response.data);
          setMessage("Incorrect login attempt."); 
          // Handle incorrect login, show a message to the user
        } else if (error.response.status === 423) {
          console.error("Account locked:", error.response.data);
          setMessage("Your account is locked.");
        } 
        else if (error.response.status === 409) {
          console.error("DUAL locked:", error.response.data);
          navigate("/dualogin");
        }else {
          setMessage("Unexpected error occurred.");
        }
        
      } else {
        console.error("Network or server error:", error.message);
      }
    }
    
  };

  return (
    <PublicLayout>
      <div
        className="mb-5"
        style={{
          maxWidth: "400px",
          margin: "auto",
          padding: "2rem",
          backgroundColor: "white",
          borderRadius: "8px",
          boxShadow: "0 0 10px rgba(0, 0, 0, 0.1)",
          marginTop: "15px",
        }}
      >
        <h2 className="text-center">Login</h2>
        {message && (
        <div className="message-box">
          {message}
        </div>
       )}
        <form onSubmit={handleSubmit(onSubmit)}>
          <div className="mb-3">
            <label htmlFor="username" className="form-label">
              Username
            </label>
            <input
              type="text"
              className={`form-control ${errors.username ? "is-invalid" : "is-valid"}`}
              id="username"
              {...register("username")}
            />
            {errors.username && (
              <p className="text-danger">{errors.username.message}</p>
            )}
          </div>
          <div className="mb-3">
            <label htmlFor="password" className="form-label">
              Password
            </label>
            <input
              type="password"
              className={`form-control ${errors.password ? "is-invalid" : "is-valid"}`}
              id="password"
              {...register("password")}
            />
            {errors.password && (
              <p className="text-danger">{errors.password.message}</p>
            )}
          </div>
          <div className="mb-3">
            <canvas
              ref={canvasRef}
              height={50}
              className="w-100"
              style={{
                border: "1px solid #ccc",
                backgroundColor: "darkgray",
                borderRadius: 5,
              }}
            />
            <div className="d-flex align-items-center mt-3">
              <input
                type="text"
                className={`form-control ${errors.captcha ? "is-invalid" : "is-valid"}`}
                id="captcha"
                {...register("captcha")}
              />
              <button
                type="button"
                className="p-1 px-2 ms-2"
                style={{ backgroundColor: "transparent" }}
                onClick={handleReloadCaptcha}
              >
                <i className="fas fa-sync text-dark fa-xl"></i>
              </button>
            </div>
            {errors.captcha && (
              <p className="text-danger">{errors.captcha.message}</p>
            )}
          </div>
          <button type="submit" className="btn btn-primary w-100 mt-3">
            Login
          </button>
        </form>
        <div className="text-center mt-3">
          <a href="#">Forgot your password?</a>
        </div>
      </div>
    </PublicLayout>
  );
};

export default Login;
