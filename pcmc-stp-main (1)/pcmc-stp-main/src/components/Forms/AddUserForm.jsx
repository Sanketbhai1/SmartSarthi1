import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import { addUser, updateUser } from "../../services/operations/userAPI";
import { useDispatch } from "react-redux";

const AddUserForm = ({ onClose, initialData = null, isEditing = false }) => {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    name: "",
    roles: "",
  });

  const dispatch = useDispatch();

  useEffect(() => {
    if (isEditing && initialData) {
      setFormData({
        username: initialData.username || "",
        password: "", // keep empty for security
        name: initialData.name || "",
        roles: initialData.roles || "",
      });
    }
  }, [isEditing, initialData]);

  const { username, password, name, roles } = formData;

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (isEditing && initialData?.id) {
      dispatch(updateUser(initialData.id, formData));
    } else {
      dispatch(addUser(formData));
    }

    // Clear the form only if adding new user
    if (!isEditing) {
      setFormData({
        username: "",
        password: "",
        name: "",
        roles: "",
      });
    }

    onClose(); // Close the form after submit
  };

  return (
    <form
      onSubmit={handleSubmit}
      className="bg-white rounded-2xl shadow-xl max-w-lg mx-auto p-8"
    >
      <h2 className="text-2xl font-semibold text-[#2f7d32] mb-6 border-b pb-2">
        {isEditing ? "Edit Admin User" : "Add Admin User"}
      </h2>

      {/* Username */}
      <div className="relative z-0 w-full mb-6 group">
        <input
          type="text"
          name="username"
          id="username"
          value={username}
          onChange={handleChange}
          required
          className="block py-2.5 px-0 w-full text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 focus:outline-none focus:ring-0 focus:border-[#2f7d32] peer"
        />
        <label
          htmlFor="username"
          className="absolute text-lg text-gray-500 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-placeholder-shown:translate-y-2 peer-placeholder-shown:scale-100 peer-focus:scale-75 peer-focus:-translate-y-6 peer-focus:text-[#2f7d32]"
        >
          Email (Username)
        </label>
      </div>

      {/* Password */}
      <div className="relative z-0 w-full mb-6 group">
        <input
          type="password"
          name="password"
          id="password"
          value={password}
          onChange={handleChange}
          required
          className="block py-2.5 px-0 w-full text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 focus:outline-none focus:ring-0 focus:border-[#2f7d32] peer"
        />
        <label
          htmlFor="password"
          className="absolute text-lg text-gray-500 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-placeholder-shown:translate-y-2 peer-placeholder-shown:scale-100 peer-focus:scale-75 peer-focus:-translate-y-6 peer-focus:text-[#2f7d32]"
        >
          {isEditing ? "New Password" : "Password"}
        </label>
      </div>

      {/* Full Name */}
      <div className="relative z-0 w-full mb-6 group">
        <input
          type="text"
          name="name"
          id="name"
          value={name}
          onChange={handleChange}
          required
          className="block py-2.5 px-0 w-full text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 focus:outline-none focus:ring-0 focus:border-[#2f7d32] peer"
        />
        <label
          htmlFor="name"
          className="absolute text-lg text-gray-500 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-placeholder-shown:translate-y-2 peer-placeholder-shown:scale-100 peer-focus:scale-75 peer-focus:-translate-y-6 peer-focus:text-[#2f7d32]"
        >
          Full Name
        </label>
      </div>

      {/* Roles */}
      <div className="relative z-0 w-full mb-6 group">
        <select
          name="roles"
          id="roles"
          value={roles}
          onChange={handleChange}
          required
          className="block py-2.5 px-0 w-full text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 focus:outline-none focus:ring-0 focus:border-[#2f7d32] peer"
        >
          <option value="" disabled>
            Select Role
          </option>
          <option value="SUPER_ADMIN">Super</option>
          <option value="ADMIN">Admin</option>
          <option value="USER">User</option>
          <option value="GUEST">Guest</option>
          <option value="CLIENT">Client</option>
          <option value="PRIVATE_STP">Private STP</option>
        </select>

        <label
          htmlFor="roles"
          className="absolute text-lg text-gray-500 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-[#2f7d32] peer-focus:scale-75 peer-focus:-translate-y-6"
        >
          Role
        </label>
      </div>

      {/* Buttons */}
      <div className="flex justify-end gap-4 mt-8">
        <button
          type="button"
          onClick={onClose}
          className="px-5 py-2 border border-gray-400 rounded-lg text-gray-700 hover:bg-gray-100 transition"
        >
          Cancel
        </button>
        <button
          type="submit"
          className="px-6 py-2 bg-[#2f7d32] text-white font-semibold rounded-lg hover:bg-green-800 transition"
        >
          {isEditing ? "Update" : "Submit"}
        </button>
      </div>
    </form>
  );
};

AddUserForm.propTypes = {
  onClose: PropTypes.func.isRequired,
  initialData: PropTypes.object,
  isEditing: PropTypes.bool,
};

export default AddUserForm;
