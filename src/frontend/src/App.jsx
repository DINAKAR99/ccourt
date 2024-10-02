import { useEffect, useState } from "react";
import "./App.css";
import { Route, Routes } from "react-router-dom";
import Login from "./pages/public/Login";
import Signup from "./pages/public/Signup";
import { MantineProvider } from "@mantine/core";
import "@mantine/core/styles.css";
import BarLoader from "react-spinners/BarLoader";

function App() {
  const [count, setCount] = useState(0);
  const [loading, setLoading] = useState(false);
  useEffect(() => {
    setLoading(true); 
    setTimeout(() => {
      setLoading(false);
    }, 1500);
  }, []);
  return (
    <>
      {loading ? (
        <div className="App">
          <BarLoader loading={loading} size={80} />
        </div>
      ) : (
        <>
          <MantineProvider>
            <Routes>
              <Route path="/signup" element={<Signup />} />
              <Route path="/" element={<Login />} />
              <Route path="/login" element={<Login />} />
              <Route path="/b/a" element={<Signup />} />
              <Route path="*" element={<Signup />} />
            </Routes>
          </MantineProvider>
        </>
      )}
    </>
  );
}

export default App;
