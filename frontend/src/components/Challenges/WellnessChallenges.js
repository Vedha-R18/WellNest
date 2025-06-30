import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../../App.css';

export default function WellnessChallenges({ userId }) {
  const [activeChallenges, setActiveChallenges] = useState([]);
  const [userChallenges, setUserChallenges] = useState([]);
  const [leaderboard, setLeaderboard] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (userId) {
      fetchData();
    }
  }, [userId]);

  const fetchData = async () => {
    try {
      const [challengesRes, userChallengesRes, leaderboardRes] = await Promise.all([
        axios.get('http://localhost:8080/api/challenges/active'),
        axios.get(`http://localhost:8080/api/challenges/user/${userId}`),
        axios.get('http://localhost:8080/api/challenges/leaderboard')
      ]);

      setActiveChallenges(challengesRes.data);
      setUserChallenges(userChallengesRes.data);
      setLeaderboard(leaderboardRes.data.slice(0, 10)); // Top 10
    } catch (error) {
      console.error('Error fetching challenges data:', error);
    } finally {
      setLoading(false);
    }
  };

  const joinChallenge = async (challengeId) => {
    try {
      await axios.post('http://localhost:8080/api/challenges/join', {
        userId: userId,
        challengeId: challengeId
      });
      
      alert('Successfully joined the challenge!');
      fetchData();
    } catch (error) {
      console.error('Error joining challenge:', error);
      alert('Error joining challenge. Please try again.');
    }
  };

  const updateProgress = async (challengeId, currentProgress) => {
    try {
      await axios.post('http://localhost:8080/api/challenges/update-progress', {
        userId: userId,
        challengeId: challengeId,
        completedDays: currentProgress + 1
      });
      
      fetchData();
    } catch (error) {
      console.error('Error updating progress:', error);
      alert('Error updating progress. Please try again.');
    }
  };

  const getUserChallenge = (challengeId) => {
    return userChallenges.find(uc => uc.challenge.id === challengeId);
  };

  if (loading) {
    return <div className="loading">Loading challenges...</div>;
  }

  return (
    <div className="challenges-container">
      <h2 className="challenges-title">Wellness Challenges</h2>
      
      <div className="challenges-grid">
        <div className="challenges-section">
          <h3>Active Challenges</h3>
          <div className="challenges-list">
            {activeChallenges.map((challenge) => {
              const userChallenge = getUserChallenge(challenge.id);
              const isJoined = !!userChallenge;
              const progress = userChallenge ? userChallenge.completedDays : 0;
              const isCompleted = userChallenge ? userChallenge.isCompleted : false;

              return (
                <div key={challenge.id} className="challenge-card">
                  <div className="challenge-header">
                    <h4>{challenge.title}</h4>
                    <span className="challenge-type">{challenge.challengeType}</span>
                  </div>
                  
                  <p className="challenge-description">{challenge.description}</p>
                  
                  <div className="challenge-details">
                    <span>Target: {challenge.targetDays} days</span>
                    <span>Reward: {challenge.pointsReward} points</span>
                  </div>

                  {isJoined ? (
                    <div className="challenge-progress">
                      <div className="progress-bar">
                        <div 
                          className="progress-fill" 
                          style={{ width: `${(progress / challenge.targetDays) * 100}%` }}
                        ></div>
                      </div>
                      <span className="progress-text">
                        {progress} / {challenge.targetDays} days completed
                      </span>
                      
                      {!isCompleted && (
                        <button 
                          className="progress-btn"
                          onClick={() => updateProgress(challenge.id, progress)}
                        >
                          Mark Today Complete
                        </button>
                      )}
                      
                      {isCompleted && (
                        <div className="challenge-completed">
                          🎉 Challenge Completed! +{challenge.pointsReward} points earned
                        </div>
                      )}
                    </div>
                  ) : (
                    <button 
                      className="join-challenge-btn"
                      onClick={() => joinChallenge(challenge.id)}
                    >
                      Join Challenge
                    </button>
                  )}
                </div>
              );
            })}
          </div>
        </div>

        <div className="leaderboard-section">
          <h3>Leaderboard</h3>
          <div className="leaderboard-list">
            {leaderboard.map((user, index) => (
              <div key={user.id} className="leaderboard-item">
                <div className="leaderboard-rank">#{index + 1}</div>
                <div className="leaderboard-user">
                  <span className="user-name">{user.user?.name || 'Anonymous'}</span>
                  <span className="user-department">{user.user?.department}</span>
                </div>
                <div className="leaderboard-points">{user.totalPoints} pts</div>
              </div>
            ))}
          </div>
        </div>
      </div>

      <div className="achievements-section">
        <h3>Your Achievements</h3>
        <div className="achievements-grid">
          <div className="achievement-item">
            <span className="achievement-icon">🏆</span>
            <span className="achievement-text">Challenges Completed: {userChallenges.filter(uc => uc.isCompleted).length}</span>
          </div>
          <div className="achievement-item">
            <span className="achievement-icon">🔥</span>
            <span className="achievement-text">Current Streak: {userChallenges.length > 0 ? Math.max(...userChallenges.map(uc => uc.completedDays)) : 0} days</span>
          </div>
          <div className="achievement-item">
            <span className="achievement-icon">⭐</span>
            <span className="achievement-text">Total Points: {userChallenges.reduce((sum, uc) => sum + uc.pointsEarned, 0)}</span>
          </div>
        </div>
      </div>
    </div>
  );
} 