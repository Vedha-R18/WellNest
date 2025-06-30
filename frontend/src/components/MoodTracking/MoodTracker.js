import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../../App.css';

const moodOptions = [
  { emoji: '😊', label: 'Happy', value: '😊' },
  { emoji: '😌', label: 'Calm', value: '😌' },
  { emoji: '😐', label: 'Neutral', value: '😐' },
  { emoji: '😔', label: 'Sad', value: '😔' },
  { emoji: '😤', label: 'Frustrated', value: '😤' },
  { emoji: '😰', label: 'Anxious', value: '😰' },
  { emoji: '😴', label: 'Tired', value: '😴' },
  { emoji: '🤗', label: 'Grateful', value: '🤗' }
];

export default function MoodTracker({ userId }) {
  const [selectedMood, setSelectedMood] = useState(null);
  const [notes, setNotes] = useState('');
  const [moodHistory, setMoodHistory] = useState([]);
  const [isSubmitted, setIsSubmitted] = useState(false);

  useEffect(() => {
    if (userId) {
      fetchMoodHistory();
    }
  }, [userId]);

  const fetchMoodHistory = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/api/mood/user/${userId}`);
      setMoodHistory(response.data.slice(0, 7)); // Last 7 entries
    } catch (error) {
      console.error('Error fetching mood history:', error);
    }
  };

  const handleMoodSubmit = async (e) => {
    e.preventDefault();
    if (!selectedMood) {
      alert('Please select a mood');
      return;
    }

    try {
      await axios.post('http://localhost:8080/api/mood/log', {
        userId: userId,
        mood: selectedMood,
        notes: notes
      });

      setIsSubmitted(true);
      setSelectedMood(null);
      setNotes('');
      fetchMoodHistory();

      setTimeout(() => setIsSubmitted(false), 3000);
    } catch (error) {
      console.error('Error logging mood:', error);
      alert('Error logging mood. Please try again.');
    }
  };

  const getMoodLabel = (moodEmoji) => {
    const mood = moodOptions.find(m => m.value === moodEmoji);
    return mood ? mood.label : moodEmoji;
  };

  return (
    <div className="mood-tracker-container">
      <h2 className="mood-tracker-title">How are you feeling today?</h2>
      
      {isSubmitted && (
        <div className="mood-submitted-message">
          ✅ Mood logged successfully!
        </div>
      )}

      <form onSubmit={handleMoodSubmit} className="mood-form">
        <div className="mood-options">
          {moodOptions.map((mood) => (
            <button
              key={mood.value}
              type="button"
              className={`mood-option ${selectedMood === mood.value ? 'selected' : ''}`}
              onClick={() => setSelectedMood(mood.value)}
            >
              <span className="mood-emoji">{mood.emoji}</span>
              <span className="mood-label">{mood.label}</span>
            </button>
          ))}
        </div>

        <div className="mood-notes">
          <label htmlFor="notes">Additional notes (optional):</label>
          <textarea
            id="notes"
            value={notes}
            onChange={(e) => setNotes(e.target.value)}
            placeholder="How was your day? Any specific feelings or events?"
            rows="3"
          />
        </div>

        <button 
          type="submit" 
          className="mood-submit-btn"
          disabled={!selectedMood}
        >
          Log My Mood
        </button>
      </form>

      {moodHistory.length > 0 && (
        <div className="mood-history">
          <h3>Your Recent Moods</h3>
          <div className="mood-history-grid">
            {moodHistory.map((log, index) => (
              <div key={index} className="mood-history-item">
                <span className="mood-history-emoji">{log.mood}</span>
                <div className="mood-history-details">
                  <span className="mood-history-label">{getMoodLabel(log.mood)}</span>
                  <span className="mood-history-date">
                    {new Date(log.createdAt).toLocaleDateString()}
                  </span>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
} 