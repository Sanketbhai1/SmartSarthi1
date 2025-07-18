"use client";
import React, { useEffect, useState } from "react";
import STPConfigureForm from "../components/Forms/StpConfigurationForm";
import { useDispatch, useSelector } from "react-redux";
import { fetchStp } from "../services/operations/stpLiveAPI";
import STPTable from "../components/Tables/STPTable";

export default function STPConfiguration() {
  const [showForm, setShowForm] = useState(false);
  const dispatch = useDispatch();

  const { stpData } = useSelector((state) => state.stp);

  useEffect(() => {
    dispatch(fetchStp());
  }, [dispatch]);

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      {/* Heading */}
      <div className="text-center">
        <h1 className="text-3xl font-semibold">Private STP Configuration</h1>
      </div>

      {/* Subheading and Button */}
      <div className="flex justify-between items-center mt-8 border-b pb-4">
        <h2 className="text-xl font-semibold text-teal-800">
          Private STP Locations
        </h2>
        <button
          onClick={() => setShowForm(true)}
          className="border border-teal-700 bg-[#2f7d32] hover:bg-[#27682a] text-white px-4 py-2 rounded-md"
        >
          ADD PRIVATE STP LOCATION
        </button>
      </div>

      {/* Form */}
      {showForm && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center px-4 py-6"
          onClick={() => setShowForm(false)}
        >
          <div
            className="max-h-full overflow-y-auto"
            onClick={(e) => e.stopPropagation()}
          >
            <STPConfigureForm onClose={() => setShowForm(false)} />
          </div>
        </div>
      )}

      {/* TABLE */}
      <STPTable data={stpData} />
    </div>
  );
}
