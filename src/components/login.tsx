import React, { useState } from "react";
import "./../assets/css/login.css";
import { useNavigate } from "react-router-dom";

const Login: React.FC = () => {
  const [form, setForm] = useState({ email: "", password: "" });
  const [message, setMessage] = useState<any>("");
  const navigate = useNavigate();
  const API = 'http://localhost:8080';

    const handleLogin = async (e: any) => {
        e.preventDefault();
        try {
        const res = await fetch(`${API}/login`,{
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                email: form.email, 
                password: form.password      
            }),
        });

        const data = await res.json();
        console.log(data)
        if(data && data.role === 'ADMIN') {
            localStorage.setItem('token', data.token)
            navigate('/user')
        } else {
            navigate('/company')

        }
        } catch (error) {
        console.error("Login error:", error);
        setMessage({ type: "error", text: "Something went wrong!" });
    }
    }

  const handleChange = (e: any) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  return (
    <div className="login-container">
      <h2>Login To Website</h2>

      <form onSubmit={handleLogin}>
        <div className="form-group">
          <label>Email</label>
          <input
            type="text"
            name="email"
            placeholder="Enter your email"
            value={form.email}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label>Password</label>
          <input
            type="password"
            name="password"
            placeholder="Enter your password"
            value={form.password}
            onChange={handleChange}
            required
          />
        </div>
        <button type="submit">Login</button>
      </form>

      <div className="login-link">
        Don't have an account? <a href="/register">Register here</a>
      </div>

      {message && <div className={`message ${message.type}`}>{message.text}</div>}
    </div>
  );
};

export default Login;
