import { useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { login } from "../../services/operations/authAPI";
import { FaUser, FaLock, FaEye, FaEyeSlash } from "react-icons/fa";

export default function Login() {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [formData, setFormData] = useState({ username: "", password: "" });
  const [showPassword, setShowPassword] = useState(false);

  const { username, password } = formData;

  const handleOnChange = (e) => {
    setFormData((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleOnSubmit = (e) => {
    e.preventDefault();
    dispatch(login(username, password, navigate));
  };

  const togglePassword = () => setShowPassword((prev) => !prev);

  return (
    <div className="min-h-screen flex items-center justify-center bg-[#d0ebd1] relative overflow-hidden">
       {/* Background circles */}
       <div className="absolute top-[-200px] right-[-200px] w-[450px] h-[450px] rounded-full bg-[#c0e5c1] z-0"></div>
      <div className="absolute bottom-[-200px] left-[-200px] w-[450px] h-[450px] rounded-full bg-[#c0e5c1] z-0"></div>
      <div className="bg-white rounded-2xl shadow-lg p-8 w-full max-w-md">
        {/* Animated Leaf Icon */}
        <div className="flex justify-center mb-4">
          <div className="bg-green-100 p-4 rounded-full">
          <svg className="logo w-[60px] h-[60px] animate-pulse" viewBox="0 0 24 24">
                <path d="M17,8C8,10 5.9,16.17 3.82,21.34L5.71,22L6.66,19.7C7.14,19.87 7.64,20 8,20C19,20 22,3 22,3C21,5 14,5.25 9,6.25C4,7.25 2,11.5 2,13.5C2,15.5 3.75,17.25 3.75,17.25C7,8 17,8 17,8Z" fill="#2e7d32"/>
            </svg>
          </div>
        </div>

        {/* Title */}
        <h2 className="text-3xl font-semibold text-center text-green-800 mb-7">
          PCMC <br /> Environment Department <br />
          (Smart Sewerage)
        </h2>
        <p className="text-center text-gray-600 mb-6 text-sm">
          Login to access your account
        </p>

        {/* Login Form */}
        <form onSubmit={handleOnSubmit} className="space-y-4">
          {/* Username */}
          <div className="relative">
            <FaUser className="absolute top-1/2 left-3 transform -translate-y-1/2 text-green-500" />
            <input
              type="text"
              name="username"
              value={username}
              onChange={handleOnChange}
              placeholder="Username"
              required
              className="w-full pl-10 pr-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            />
          </div>

          {/* Password */}
          <div className="relative">
            <FaLock className="absolute top-1/2 left-3 transform -translate-y-1/2 text-green-500" />
            <input
              type={showPassword ? "text" : "password"}
              name="password"
              value={password}
              onChange={handleOnChange}
              placeholder="Password"
              required
              className="w-full pl-10 pr-10 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            />
            <span
              onClick={togglePassword}
              className="absolute top-1/2 right-3 transform -translate-y-1/2 text-green-500 cursor-pointer"
            >
              {showPassword ? <FaEyeSlash /> : <FaEye />}
            </span>
          </div>

          {/* Submit */}
          <button
            type="submit"
            className="w-full bg-green-700 hover:bg-green-800 text-white font-semibold py-2 rounded-md transition-colors"
          >
            LOGIN
          </button>
        </form>
      </div>
    </div>
  );
}
