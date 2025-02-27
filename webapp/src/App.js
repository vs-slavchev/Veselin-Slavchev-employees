import './App.css';
import EmployeeWorkAssignment from './EmployeeWorkAssignment';
import { useState } from 'react';

function App() {
  const [assignments, setAssignments] = useState(null);
  const [error, setError] = useState(null);

  const handleFileUpload = async (event) => {
    console.log('Uploading file...');
    const file = event.target.files[0];
    if (file) {
      const formData = new FormData();
      formData.append('file', file);

      try {
        const response = await fetch('http://localhost:8080/api/v1/employees/work/assignments', {
          method: 'POST',
          body: formData,
        });

        if (response.ok) {
          const data = await response.json();
          const assignments = data.map(item => new EmployeeWorkAssignment(
            item.employee1Id,
            item.employee2Id,
            item.projectId,
            item.daysWorked
          ));
          setAssignments(assignments);
          setError(null);
          console.log('File uploaded successfully', assignments);
        } else {
          const errorData = await response.json();
          setError(errorData.error);
          setAssignments(null);
          console.error('File upload failed', errorData);
        }
      } catch (error) {
        setError('Error uploading file');
        setAssignments(null);
        console.error('Error uploading file:', error);
      }
    }
  };

  return (
    <div className="App">
      <h1>Find longest working employee pairs</h1>
      <input type="file" accept=".csv" id="fileInput" style={{ display: 'none' }} onChange={handleFileUpload} />
      <button onClick={() => document.getElementById('fileInput').click()}>Upload CSV</button>

      {error && <div id="error" style={{ color: 'red' }}>Error: {error}</div>}

      {assignments != null && assignments.length > 0 && (
        <table>
          <thead>
            <tr>
              <th>Employee 1 ID</th>
              <th>Employee 2 ID</th>
              <th>Project ID</th>
              <th>Days worked</th>
            </tr>
          </thead>
          <tbody>
            {assignments.map((assignment, index) => (
              <tr key={index}>
                <td>{assignment.employee1Id}</td>
                <td>{assignment.employee2Id}</td>
                <td>{assignment.projectId}</td>
                <td>{assignment.daysWorked}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {assignments != null && assignments.length === 0 && !error && <div id="noData">No two employees have worked together</div>}

    </div>
  );
}

export default App;
