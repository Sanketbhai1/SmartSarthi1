import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchZones } from "../services/operations/zoneAPI";
import ZoneTable from "../components/Tables/ZoneTable";
import ZonesForm from "../components/Forms/ZonesForm";

const ManageZone = () => {
  const [showForm, setShowForm] = useState(false);
  const [selectedZone, setSelectedZone] = useState(null);

  const dispatch = useDispatch();

  const { zoneData } = useSelector((state) => state.zone);

  useEffect(() => {
    dispatch(fetchZones());
  }, [dispatch]);

  return (
    <div className="relative min-h-screen bg-gray-50 p-6">
      <div className="flex justify-between items-center mb-4 px-5">
        <h1 className="text-3xl text-teal-800 font-semibold">Manage Zones</h1>
        <button
          onClick={() => setShowForm(true)}
          className="border border-teal-700 bg-[#2f7d32] hover:bg-[#27682a] text-white px-4 py-2 rounded-md"
        >
          Add Zone
        </button>
      </div>

      {/* Drawer Form from center */}
      {showForm && (
        <div
          className="fixed inset-0 bg-black bg-opacity-40 z-50 flex justify-center items-center"
          onClick={() => setShowForm(false)}
        >
          <div
            className="w-full max-w-md  bg-white h-fit shadow-lg overflow-y-auto relative"
            onClick={(e) => e.stopPropagation()}
          >
            <ZonesForm
              onClose={() => setShowForm(false)}
              isEditing={Boolean(selectedZone)}
              initialData={selectedZone}
            />
          </div>
        </div>
      )}

      <div>
        <ZoneTable data={zoneData} />
      </div>
    </div>
  );
};

export default ManageZone;
