import React, { useState } from "react";
import Login from "./components/Auth/Login";
import CheckInForm from "./components/CheckIn/CheckInForm";
import EmployeeDashboard from "./components/Dashboard/EmployeeDashboard";
import MoodTracker from "./components/MoodTracking/MoodTracker";
import WellnessChallenges from "./components/Challenges/WellnessChallenges";
import SupportRequest from "./components/Support/SupportRequest";
import SupportTeamPanel from './components/Support/SupportTeamPanel';
import UserSupportDashboard from './components/Support/UserSupportDashboard';
import "./App.css";

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [hasCheckedIn, setHasCheckedIn] = useState(false);
  const [currentUser, setCurrentUser] = useState(null);
  const [currentView, setCurrentView] = useState('dashboard');

  // Called when login is successful
  const handleLoginSuccess = (user) => {
    setIsLoggedIn(true);
    setCurrentUser(user);
  };

  // Called when check-in is submitted
  const handleCheckIn = () => {
    setHasCheckedIn(true);
  };

  const handleLogout = () => {
    setIsLoggedIn(false);
    setHasCheckedIn(false);
    setCurrentUser(null);
    setCurrentView('dashboard');
  };

  if (!isLoggedIn) {
    return <Login onLoginSuccess={handleLoginSuccess} />;
  }

  if (!hasCheckedIn) {
    return <CheckInForm onSubmit={handleCheckIn} />;
  }

  const renderCurrentView = () => {
    switch (currentView) {
      case 'dashboard':
        return <EmployeeDashboard />;
      case 'mood':
        return <MoodTracker userId={currentUser.id} />;
      case 'challenges':
        return <WellnessChallenges userId={currentUser.id} />;
      case 'support':
        if (currentUser?.role === 'SUPPORT_TEAM') {
          return <SupportTeamPanel />;
        } else {
          return <UserSupportDashboard />;
        }
      default:
        return <EmployeeDashboard />;
    }
  };

  return (
    <div className="app-container">
      <nav className="main-navigation">
        <div className="nav-header">
          <h1 className="nav-title">StressP</h1>
          <div className="user-info">
            <span className="user-name">{currentUser?.name || 'User'}</span>
            <span className="user-role">{currentUser?.role || 'Employee'}</span>
          </div>
        </div>
        
        <div className="nav-links">
          <button 
            className={`nav-link ${currentView === 'dashboard' ? 'active' : ''}`}
            onClick={() => setCurrentView('dashboard')}
          >
            📊 Dashboard
          </button>
          {currentUser?.role !== 'SUPPORT_TEAM' && (
            <>
              <button 
                className={`nav-link ${currentView === 'mood' ? 'active' : ''}`}
                onClick={() => setCurrentView('mood')}
              >
                😊 Mood Tracker
              </button>
              <button 
                className={`nav-link ${currentView === 'challenges' ? 'active' : ''}`}
                onClick={() => setCurrentView('challenges')}
              >
                🏆 Challenges
              </button>
            </>
          )}
          <button 
            className={`nav-link ${currentView === 'support' ? 'active' : ''}`}
            onClick={() => setCurrentView('support')}
          >
            🤝 Support
          </button>
        </div>

        <div className="nav-footer">
          <button className="logout-btn" onClick={handleLogout}>
            🚪 Logout
          </button>
        </div>
      </nav>

      <main className="main-content">
        {renderCurrentView()}
      </main>
    </div>
  );
}

export default App;