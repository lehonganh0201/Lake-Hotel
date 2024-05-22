import React from 'react'
import { useAuth } from './AuthProvider'
import { Link, useNavigate } from 'react-router-dom';

const Logout = () => {
    const auth = useAuth();
    const navigate = useNavigate();

    const handleLogout = () =>{
        auth.handleLogout();
        window.location.reload();
        navigate("/", {state : "You have been logged out!"});
    }
    return (
      <>
        <li>
          <Link className="dropdown-item" to={"/profile"}>
            Profile
          </Link>
        </li>
        <li>
          <hr className="dropdown-divider" />
        </li>
        <button className="dropdown-item" onClick={handleLogout}>
          Logout
        </button>
      </>
    )
  
}

export default Logout
