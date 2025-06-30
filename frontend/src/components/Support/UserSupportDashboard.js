import React, { useEffect, useState } from "react";

export default function UserSupportDashboard() {
  const [dailyQuestions, setDailyQuestions] = useState([]);
  const [userQuestions, setUserQuestions] = useState([]);
  const [newQuestion, setNewQuestion] = useState("");
  const userId = localStorage.getItem("userId");

  useEffect(() => {
    fetch("/api/support/daily-questions")
      .then(res => res.json())
      .then(setDailyQuestions);
    fetch(`/api/support/user-questions/${userId}`)
      .then(res => res.json())
      .then(setUserQuestions);
  }, [userId]);

  const handleSubmitQuestion = async () => {
    if (!newQuestion.trim()) return;
    await fetch("/api/support/user-question", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ userId, question: newQuestion })
    });
    setNewQuestion("");
    // Refresh user questions
    fetch(`/api/support/user-questions/${userId}`)
      .then(res => res.json())
      .then(setUserQuestions);
  };

  return (
    <div className="user-support-dashboard">
      <h2>Support Center</h2>
      <div className="daily-questions-section">
        <h3>Today's Daily Questions</h3>
        {dailyQuestions.length === 0 ? (
          <div>No daily questions available.</div>
        ) : (
          <ul>
            {dailyQuestions.slice(0, 10).map(q => (
              <li key={q.id}>{q.question}</li>
            ))}
          </ul>
        )}
      </div>
      <div className="submit-question-section">
        <h3>Ask the Support Team</h3>
        <textarea
          value={newQuestion}
          onChange={e => setNewQuestion(e.target.value)}
          placeholder="Type your question..."
          style={{ width: "100%", minHeight: 60 }}
        />
        <button onClick={handleSubmitQuestion}>Submit Question</button>
      </div>
      <div className="user-questions-section">
        <h3>Your Support Questions & Answers</h3>
        {userQuestions.length === 0 ? (
          <div>You have not asked any questions yet.</div>
        ) : (
          <ul>
            {userQuestions.map(q => (
              <li key={q.id} style={{ marginBottom: 16 }}>
                <div><b>Q:</b> {q.question}</div>
                <div><b>A:</b> {q.answer ? q.answer : <i>Pending...</i>}</div>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
} 