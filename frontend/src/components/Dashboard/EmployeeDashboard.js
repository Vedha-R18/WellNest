import React from "react";

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

const quotes = [
  "Keep going. Everything you need will come to you.",
  "You are stronger than you think.",
  "Take it one step at a time.",
  "Breathe. You've got this.",
  "Every day is a fresh start."
];

export default function EmployeeDashboard() {
  const randomTip = tips[Math.floor(Math.random() * tips.length)];
  const randomQuote = quotes[Math.floor(Math.random() * quotes.length)];

  React.useEffect(() => {
    const text = document.getElementById("breathingText");
    if (!text) return;
    let phase = 0;
    const phases = [
      { label: "Inhale", duration: 4000 },
      { label: "Hold", duration: 7000 },
      { label: "Exhale", duration: 8000 }
    ];
    let timeout;
    function nextPhase() {
      text.textContent = phases[phase].label;
      timeout = setTimeout(() => {
        phase = (phase + 1) % phases.length;
        nextPhase();
      }, phases[phase].duration);
    }
    nextPhase();
    return () => clearTimeout(timeout);
  }, []);

  return (
    <div className="dashboard-main-centered">
      <div className="dashboard-card dashboard-quote-card">
        <h3 className="dashboard-section-title">Motivational Quote</h3>
        <div className="dashboard-quote">"{randomQuote}"</div>
      </div>
      <div className="dashboard-card dashboard-tip-card">
        <h3 className="dashboard-section-title">Today's Stress-Relief Tip</h3>
        <div className="dashboard-tip">{randomTip}</div>
      </div>
      <div className="dashboard-card dashboard-breathing-card">
        <h3 className="dashboard-section-title">Try this: 4-7-8 Breathing</h3>
        <div className="dashboard-breathing">
          Inhale for 4 seconds, hold for 7 seconds, exhale for 8 seconds. Repeat 3 times.
        </div>
        <div className="breathing-animation">
          <div className="breathing-circle"></div>
          <div className="breathing-text" id="breathingText">Inhale</div>
        </div>
      </div>
      <div className="dashboard-card dashboard-resources-card">
        <h3 className="dashboard-section-title">Helpful Resources</h3>
        <ul>
          <li>
            <a href="https://www.mind.org.uk/information-support/tips-for-everyday-living/relaxation/" target="_blank" rel="noopener noreferrer">
              Relaxation Techniques (Mind UK)
            </a>
          </li>
          <li>
            <a href="https://www.headspace.com/meditation/meditation-for-stress" target="_blank" rel="noopener noreferrer">
              Meditation for Stress (Headspace)
            </a>
          </li>
          <li>
            <a href="https://www.youtube.com/watch?v=inpok4MKVLM" target="_blank" rel="noopener noreferrer">
              5-Minute Meditation (YouTube)
            </a>
          </li>
        </ul>
      </div>
    </div>
  );
}