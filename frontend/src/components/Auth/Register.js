import React, { useState } from 'react';
import API from '../../api';


export default function Register() {
  const [form, setForm] = useState({ email: '', password: '', name: '', role: 'EMPLOYEE', department: '' });
  const handleChange = e => setForm({ ...form, [e.target.name]: e.target.value });
  const handleRegister = async () => {
    try {
      await API.post('/auth/register', form);
      alert('Registration successful!');
    } catch {
      alert('Registration failed');
    }
  };
  return (
    <div>
      <h2>Register</h2>
      <input name="name" value={form.name} onChange={handleChange} placeholder="Name"/>
      <input name="email" value={form.email} onChange={handleChange} placeholder="Email"/>
      <input name="password" type="password" value={form.password} onChange={handleChange} placeholder="Password"/>
      <input name="department" value={form.department} onChange={handleChange} placeholder="Department"/>
      <select name="role" value={form.role} onChange={handleChange}>
        <option value="EMPLOYEE">Employee</option>
        <option value="MANAGER">Manager</option>
        <option value="ADMIN">Admin</option>
      </select>
      <button onClick={handleRegister}>Register</button>
    </div>
  );
} 