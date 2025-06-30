import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../../App.css';

export default function SupportRequest({ userId }) {
  const [requestType, setRequestType] = useState('ANONYMOUS');
  const [priority, setPriority] = useState('MEDIUM');
  const [description, setDescription] = useState('');
  const [userRequests, setUserRequests] = useState([]);
  const [isSubmitted, setIsSubmitted] = useState(false);

  useEffect(() => {
    if (userId) {
      fetchUserRequests();
    }
  }, [userId]);

  const fetchUserRequests = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/api/support/user/${userId}`);
      setUserRequests(response.data);
    } catch (error) {
      console.error('Error fetching user requests:', error);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!description.trim()) {
      alert('Please provide a description of your request');
      return;
    }

    try {
      await axios.post('http://localhost:8080/api/support/request', {
        userId: userId,
        requestType: requestType,
        description: description,
        priority: priority
      });

      setIsSubmitted(true);
      setDescription('');
      fetchUserRequests();

      setTimeout(() => setIsSubmitted(false), 3000);
    } catch (error) {
      console.error('Error creating support request:', error);
      alert('Error creating support request. Please try again.');
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'PENDING': return '#ffaa00';
      case 'IN_PROGRESS': return '#0066ff';
      case 'RESOLVED': return '#44ff44';
      case 'ESCALATED': return '#ff4444';
      default: return '#666666';
    }
  };

  const getPriorityColor = (priority) => {
    switch (priority) {
      case 'LOW': return '#44ff44';
      case 'MEDIUM': return '#ffaa00';
      case 'HIGH': return '#ff6600';
      case 'CRITICAL': return '#ff4444';
      default: return '#666666';
    }
  };

  return (
    <div className="support-request-container">
      <h2 className="support-request-title">Request Support</h2>
      
      {isSubmitted && (
        <div className="support-submitted-message">
          ✅ Support request submitted successfully!
        </div>
      )}

      <div className="support-content">
        <div className="support-form-section">
          <h3>Submit a New Request</h3>
          <form onSubmit={handleSubmit} className="support-form">
            <div className="form-group">
              <label htmlFor="requestType">Request Type:</label>
              <select
                id="requestType"
                value={requestType}
                onChange={(e) => setRequestType(e.target.value)}
                className="support-select"
              >
                <option value="ANONYMOUS">Anonymous Request</option>
                <option value="MANAGER">Manager Support</option>
                <option value="HR">HR Support</option>
                <option value="PEER">Peer Support</option>
              </select>
            </div>

            <div className="form-group">
              <label htmlFor="priority">Priority Level:</label>
              <select
                id="priority"
                value={priority}
                onChange={(e) => setPriority(e.target.value)}
                className="support-select"
              >
                <option value="LOW">Low</option>
                <option value="MEDIUM">Medium</option>
                <option value="HIGH">High</option>
                <option value="CRITICAL">Critical</option>
              </select>
            </div>

            <div className="form-group">
              <label htmlFor="description">Description:</label>
              <textarea
                id="description"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                placeholder="Please describe your situation and what kind of support you need..."
                rows="5"
                className="support-textarea"
                required
              />
            </div>

            <button type="submit" className="support-submit-btn">
              Submit Request
            </button>
          </form>
        </div>

        <div className="support-history-section">
          <h3>Your Support Requests</h3>
          <div className="support-requests-list">
            {userRequests.length > 0 ? (
              userRequests.map((request) => (
                <div key={request.id} className="support-request-item">
                  <div className="request-header">
                    <span className="request-type">{request.requestType}</span>
                    <span 
                      className="priority-badge"
                      style={{ backgroundColor: getPriorityColor(request.priority) }}
                    >
                      {request.priority}
                    </span>
                    <span 
                      className="status-badge"
                      style={{ backgroundColor: getStatusColor(request.status) }}
                    >
                      {request.status}
                    </span>
                  </div>
                  
                  <p className="request-description">{request.description}</p>
                  
                  <div className="request-meta">
                    <span className="request-date">
                      Submitted: {new Date(request.createdAt).toLocaleDateString()}
                    </span>
                    {request.assignedTo && (
                      <span className="assigned-to">Assigned to: {request.assignedTo}</span>
                    )}
                    {request.resolvedAt && (
                      <span className="resolved-date">
                        Resolved: {new Date(request.resolvedAt).toLocaleDateString()}
                      </span>
                    )}
                  </div>
                </div>
              ))
            ) : (
              <p className="no-requests">No support requests yet.</p>
            )}
          </div>
        </div>
      </div>

      <div className="support-info">
        <h3>Support Information</h3>
        <div className="support-info-grid">
          <div className="info-item">
            <h4>🕵️ Anonymous Requests</h4>
            <p>Your identity will be kept confidential. Only HR and management will see these requests.</p>
          </div>
          <div className="info-item">
            <h4>⚡ Response Times</h4>
            <p>Critical: Within 24 hours<br/>
               High: Within 48 hours<br/>
               Medium: Within 1 week<br/>
               Low: Within 2 weeks</p>
          </div>
          <div className="info-item">
            <h4>🤝 Available Support</h4>
            <p>• Mental health resources<br/>
               • Work-life balance guidance<br/>
               • Conflict resolution<br/>
               • Professional development</p>
          </div>
          <div className="info-item">
            <h4>🚨 Emergency Support</h4>
            <p>If you're experiencing a crisis, please contact:<br/>
               • Employee Assistance Program: 1-800-XXX-XXXX<br/>
               • Crisis Hotline: 988<br/>
               • Emergency Services: 911</p>
          </div>
        </div>
      </div>
    </div>
  );
} 