import { useEffect, useState } from "react";
import "./App.css";
import { HashRouter, Route, Routes } from "react-router-dom";
import Login from "./pages/public/Login";
import Signup from "./pages/public/Signup";
import { MantineProvider } from "@mantine/core";
import "@mantine/core/styles.css";
import BarLoader from "react-spinners/BarLoader";
import Protected from "./pages/public/Protected";
import Dualogin from "./pages/public/Dualogin";
import SessionExpired from "./pages/public/SessionExpired";
import Home from "./pages/public/Home";
import About from "./pages/public/About";

function App() {
  const [loading, setLoading] = useState(false);
  useEffect(() => {
    setLoading(true);
    setTimeout(() => {
      setLoading(false);
      // Cleanup function to clear the timer if the component unmounts
      return () => clearTimeout(timer);
    }, 700);
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
              <Route path="/" element={<Home />} />
              <Route path="/b/a" element={<Signup />} />
              <Route path="/login" element={<Login />} />
              <Route path="/signup" element={<Signup />} />
              <Route path="/about" element={<About />} />
              <Route path="/protected" element={<Protected />} />
              <Route path="/dualogin" element={<Dualogin />} />
              <Route path="/sessionExpired" element={<SessionExpired />} />
              <Route path="*" element={<Home />} />
            </Routes>
          </MantineProvider>
        </>
      )}
    </>
  );
}

export default App;
