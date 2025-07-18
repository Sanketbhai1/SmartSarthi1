import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { addZone, updateZone } from "../../services/operations/zoneAPI";

function ZonesForm({ onClose, initialData = null, isEditing = false }) {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const [zoneData, setZoneData] = useState({
    title: "",
    code: "",
    status: true,
  });

  useEffect(() => {
    if (isEditing && initialData) {
      setZoneData({
        title: initialData.title,
        code: initialData.code,
        status: initialData.status,
      });
    }
  }, [isEditing, initialData]);

  const { title, code, status } = zoneData;

  const handleOnChange = (e) => {
    const { name, type, value, checked } = e.target;
    let newValue = value;
    if (name === "title") {
      // Allow only uppercase A-Z and limit to 1 character
      newValue = value
        .toUpperCase()
        .replace(/[^A-Z]/g, "")
        .slice(0, 1);
    }
    setZoneData((prevData) => ({
      ...prevData,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (isEditing && initialData?.id) {
      dispatch(updateZone(initialData.id, zoneData, navigate));
    } else {
      dispatch(addZone(zoneData, navigate));
    }

    if (!isEditing) {
      setZoneData({
        title: "",
        code: "",
        status: true,
      });
    }
  };

  return (
    <form
      onSubmit={handleSubmit}
      className="bg-white rounded-2xl shadow-xl max-w-lg mx-auto p-8"
    >
      <h2 className="text-2xl font-semibold text-[#2f7d32] mb-6 border-b pb-2">
        {isEditing ? "Edit Zone" : "Add Zone"}
      </h2>

      {/* Input with floating label */}
      <div className="relative z-0 w-full mb-6 group">
        <input
          type="text"
          name="title"
          id="title"
          value={title}
          onChange={handleOnChange}
          pattern="[A-Z]"
          maxLength={1}
          required
          className="block py-2.5 px-0 w-full text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-[#2f7d32] peer"
        />
        <label
          htmlFor="title"
          className="absolute text-lg text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-[#2f7d32] peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-2 peer-focus:scale-75 peer-focus:-translate-y-6"
        >
          Zone Name
        </label>
      </div>

      <div className="relative z-0 w-full mb-6 group">
        <input
          type="text"
          name="code"
          id="code"
          value={code}
          onChange={handleOnChange}
          required
          className="block py-2.5 px-0 w-full text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-[#2f7d32] peer"
        />
        <label
          htmlFor="code"
          className="absolute text-lg text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-[#2f7d32] peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-2 peer-focus:scale-75 peer-focus:-translate-y-6"
        >
          Code
        </label>
      </div>

      {/* Toggle switch */}
      <div className="mb-6 flex items-center gap-3">
        <label className="text-gray-700 font-medium">Status</label>
        <label className="relative inline-flex items-center cursor-pointer">
          <input
            type="checkbox"
            name="status"
            checked={status}
            onChange={handleOnChange}
            className="sr-only peer"
          />
          <div className="w-11 h-6 bg-gray-200 rounded-full peer peer-checked:bg-[#2f7d32] after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:after:translate-x-full"></div>
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
          {isEditing ? "Update" : "Save"}
        </button>
      </div>
    </form>
  );
};

ZonesForm.propTypes = {
  onClose: PropTypes.func.isRequired,
  initialData: PropTypes.object,
  isEditing: PropTypes.bool,
};

export default ZonesForm