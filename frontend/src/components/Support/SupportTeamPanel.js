import React, { useEffect, useState } from "react";

export default function SupportTeamPanel() {
  const [dailyQuestions, setDailyQuestions] = useState(["", "", "", "", "", "", "", "", "", ""]);
  const [pendingQuestions, setPendingQuestions] = useState([]);
  const [answer, setAnswer] = useState("");
  const [selectedQuestionId, setSelectedQuestionId] = useState(null);
  const [todayCount, setTodayCount] = useState(0);
  const [pendingRequests, setPendingRequests] = useState([]);
  const supportUserId = localStorage.getItem("userId");
  const role = localStorage.getItem("role");

  // Fetch pending user questions
  useEffect(() => {
    fetch("/api/support/pending-questions")
      .then(res => res.json())
      .then(data => setPendingQuestions(Array.isArray(data) ? data : []));
    fetch("/api/support/daily-questions")
      .then(res => res.json())
      .then(data => setTodayCount(Array.isArray(data) ? data.length : 0));
  }, []);

  // Fetch pending support requests
  useEffect(() => {
    fetch("/api/support/pending-requests")
      .then(res => res.json())
      .then(data => setPendingRequests(Array.isArray(data) ? data : []));
    fetch("/api/support/pending-questions")
      .then(res => res.json())
      .then(data => setPendingQuestions(Array.isArray(data) ? data : []));
  }, []);

  // Upload daily questions
  const handleUploadDailyQuestions = async () => {
    const questions = dailyQuestions.filter(q => q.trim()).map(q => ({ question: q }));
    if (questions.length !== 10) {
      alert("Please enter exactly 10 questions.");
      return;
    }
    const res = await fetch(`/api/support/daily-questions?supportUserId=${supportUserId}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(questions)
    });
    if (!res.ok) {
      const msg = await res.text();
      alert(msg);
      return;
    }
    alert("Daily questions uploaded!");
    setDailyQuestions(["", "", "", "", "", "", "", "", "", ""]);
    // Refresh count
    fetch("/api/support/daily-questions")
      .then(res => res.json())
      .then(data => setTodayCount(Array.isArray(data) ? data.length : 0));
    // Optionally refresh pending questions
    fetch("/api/support/pending-questions")
      .then(res => res.json())
      .then(data => setPendingQuestions(Array.isArray(data) ? data : []));
  };

  // Answer a user question
  const handleAnswer = async () => {
    if (!selectedQuestionId || !answer.trim()) return;
    await fetch("/api/support/answer", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ questionId: selectedQuestionId, answer, supportUserId })
    });
    setAnswer("");
    setSelectedQuestionId(null);
    // Refresh pending questions
    fetch("/api/support/pending-questions")
      .then(res => res.json())
      .then(data => setPendingQuestions(Array.isArray(data) ? data : []));
  };

  return (
    <div className="support-team-panel">
      <h2>Support Team Panel</h2>
      <div className="daily-questions-upload">
        <h3>Upload 10 Daily Questions</h3>
        <div>Questions uploaded today: {todayCount} / 10</div>
        {dailyQuestions.map((q, i) => (
          <input
            key={i}
            type="text"
            value={q}
            placeholder={`Question ${i + 1}`}
            onChange={e => {
              const arr = [...dailyQuestions];
              arr[i] = e.target.value;
              setDailyQuestions(arr);
            }}
            style={{ display: "block", marginBottom: 8, width: "100%" }}
            disabled={todayCount >= 10}
          />
        ))}
        <button onClick={handleUploadDailyQuestions} disabled={todayCount >= 10}>Upload Daily Questions</button>
      </div>
      <div className="pending-questions-section">
        <h3>Pending User Questions</h3>
        {pendingQuestions.length === 0 ? (
          <div>No pending questions.</div>
        ) : (
          <ul>
            {pendingQuestions.map(q => (
              <li key={q.id} style={{ marginBottom: 16 }}>
                <div><b>Q:</b> {q.question}</div>
                {selectedQuestionId === q.id ? (
                  <div>
                    <textarea
                      value={answer}
                      onChange={e => setAnswer(e.target.value)}
                      placeholder="Type your answer..."
                      style={{ width: "100%", minHeight: 60 }}
                    />
                    <button onClick={handleAnswer}>Submit Answer</button>
                  </div>
                ) : (
                  <button onClick={() => setSelectedQuestionId(q.id)}>Answer</button>
                )}
              </li>
            ))}
          </ul>
        )}
      </div>
      <div className="pending-requests-section">
        <h3>Pending Support Requests</h3>
        {pendingRequests.length === 0 ? (
          <div>No pending support requests.</div>
        ) : (
          <ul>
            {pendingRequests.map(r => (
              <li key={r.id}>
                <div><b>Type:</b> {r.requestType}</div>
                <div><b>Description:</b> {r.description}</div>
                <div><b>Priority:</b> {r.priority}</div>
                <div><b>Status:</b> {r.status}</div>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
} 