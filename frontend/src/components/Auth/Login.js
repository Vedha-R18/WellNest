import React, { useState } from 'react';
import API from '../../api';
import '../../App.css';

export default function Login({ onLoginSuccess }) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = async () => {
    try {
      const res = await API.post('/auth/login', { email, password });
      console.log("Login response:", res.data);

      if (res.data && res.data.token) {
        localStorage.setItem('token', res.data.token);
        
        // Create user object with available data
        const user = {
          id: res.data.userId || 1, // Default to 1 if not provided
          email: email,
          name: res.data.name || 'User',
          role: res.data.role || 'EMPLOYEE',
          department: res.data.department || 'General'
        };
        
        if (onLoginSuccess) onLoginSuccess(user);
      } else {
        alert('Login failed');
      }
    } catch (err) {
      alert('Login failed');
      console.error(err);
    }
  };

  const handleCheckIn = (answers) => {
    fetch("/api/stress/submit", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ answers }),
    })
      .then((res) => res.json())
      .then((data) => {
        setHasCheckedIn(true);
        alert("Thank you for checking in!");
      });
  };

  return (
    <div className="form-container">
      <h2 className="form-title">Login to StressP</h2>
      <input 
        className="form-input" 
        value={email} 
        onChange={e => setEmail(e.target.value)} 
        placeholder="Email"
        type="email"
      />
      <input 
        className="form-input" 
        type="password" 
        value={password} 
        onChange={e => setPassword(e.target.value)} 
        placeholder="Password"
      />
      <button className="form-button" onClick={handleLogin}>Login</button>
    </div>
  );
}