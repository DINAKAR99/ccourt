import React from 'react'
import PublicLayout from '../../Layouts/PublicLayout'

const SessionExpired = () => {
    const onLogin = () => {
        // Handle login logic here
        console.log('Redirecting to login...');
      };
    
      const onHelp = () => {
        // Handle help logic here
        console.log('Opening help...');
      };
  return (
    <div>
         <PublicLayout>
         <div className="text-center "><br />
         <br />
         <div className="container text-center mt-5 p-4 border rounded shadow-lg">
      <div className="alert alert-danger">
        <h4 className="alert-heading">Your Session Expired</h4>
        <p className="mb-0">Please log in again</p>
      </div>
      <div className="my-4">
        <i className="bi bi-lock-fill" style={{ fontSize: '50px', color: '#dc3545' }}></i>
      </div>
      <div>
        {/* <button className="btn btn-primary btn-lg" onClick={onLogin}>
          Login Again
        </button>
        <button className="btn btn-secondary btn-lg ms-3" onClick={onHelp}>
          Help
        </button> */}
      </div>
    </div>
    </div>
            </PublicLayout>
            </div>
  )
}

export default SessionExpired