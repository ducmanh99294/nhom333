import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./components/login";
import Register from "./components/register";
import User from "./components/User";
import Company from "./components/Company";

// Component xử lý layout theo route
const AppContent: React.FC = () => {
  return (
    <>
    <Routes>
      <Route path="/login" element={<Login/>} />
      <Route path="/register" element={<Register/>} />
      <Route path="/user" element={<User/>} />
      <Route path="/company" element={<Company/>} />
    </Routes>
    </>
  );
};

const App: React.FC = () => {
  return (
    <Router>
      <AppContent />
    </Router>
  );
};

export default App;
