import React, { useState } from 'react';
import "../../App.css"; // Make sure your CSS is imported
import axios from 'axios';

const questions = [
  "I feel overwhelmed by my workload.",
  "I have trouble sleeping due to work stress.",
  "I feel anxious about meeting deadlines.",
  "I find it hard to relax after work.",
  "I feel irritable or moody at work.",
  "I have physical symptoms (headache, stomach issues) due to stress.",
  "I feel unsupported by my colleagues or manager.",
  "I struggle to concentrate on tasks.",
  "I feel unmotivated to start my workday.",
  "I worry about work even during my free time."
];

const scale = [
  "1 - Never",
  "2 - Rarely",
  "3 - Sometimes",
  "4 - Often",
  "5 - Always"
];

const tips = [
  "Take a short walk or stretch every hour.",
  "Practice deep breathing for 2 minutes.",
  "Write down three things you're grateful for.",
  "Listen to your favorite music for a few minutes.",
  "Try a quick guided meditation.",
  "Drink a glass of water and take a few deep breaths.",
  "Talk to a friend or colleague.",
  "Organize your workspace.",
  "Take a 5-minute break away from screens.",
  "Set a small, achievable goal for the next hour."
];

const randomTip = tips[Math.floor(Math.random() * tips.length)];

export default function CheckInForm({ onSubmit }) {
  const [answers, setAnswers] = useState(Array(questions.length).fill(1));

  const handleChange = (idx, value) => {
    const newAnswers = [...answers];
    newAnswers[idx] = parseInt(value, 10);
    setAnswers(newAnswers);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const score = answers.reduce((sum, val) => sum + val, 0);
    alert(`Your stress score is ${score} / 50`);
    if (onSubmit) onSubmit();

    axios.post('http://localhost:8080/api/stress/submit', {
      userId: 1,
      score: score
    })
    .then(response => {
      console.log('Stress score submitted successfully:', response.data);
    })
    .catch(error => {
      console.error('Error submitting stress score:', error);
    });
  };

  return (
    <form className="stress-checkin-container" onSubmit={handleSubmit}>
      <h2 className="stress-checkin-title">Stress Check-In</h2>
      {questions.map((q, idx) => (
        <div className="stress-question-group" key={idx}>
          <label className="stress-question-label">{q}</label>
          <select
            className="stress-select"
            value={answers[idx]}
            onChange={(e) => handleChange(idx, e.target.value)}
            required
          >
            {scale.map((label, val) => (
              <option key={val + 1} value={val + 1}>{label}</option>
            ))}
          </select>
        </div>
      ))}
      <button className="stress-submit-btn" type="submit">Submit</button>
    </form>
  );
} 