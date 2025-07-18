"use client";
import React, { useState, useMemo, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchStp } from "../../services/operations/stpLiveAPI";
import MapImage from "../../assets/map/SCADA_Map.jpg"

export default function MapComponent() {
  const [selectedLetter, setSelectedLetter] = useState(null);
  const { stpData } = useSelector((state) => state.stp);
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(fetchStp()); 
  }, [dispatch]);

  const zoneData = useMemo(() => {
    if (!Array.isArray(stpData)) return {};
    return stpData.reduce((acc, item) => {
      const zone = item.title; // Should match the title from your API data
      if (!acc[zone]) {
        acc[zone] = [];
      }
      acc[zone].push(item);
      return acc;
    }, {});
  }, [stpData]);

  // Set the selected letter (zone) when a button is clicked
  const showInfo = (letter) => {
    setSelectedLetter(letter);
  };

  // Get the data for the selected zone
  const selectedData = zoneData[selectedLetter] || [];

  return (
    <div>
      {/* Map Section */}
      <div className="relative w-full mx-auto mt-4">
        <img
          src={MapImage}
          alt="PCMC Map"
          className="w-full h-full"
        />

        {/* Clickable Buttons for each zone */}
        {["A", "B", "C", "D", "E", "F", "G", "H"].map((letter) => {
          const positions = {
            A: "top-[48%] left-[38%]",
            B: "top-[44%] left-[20%]",
            C: "top-[40%] left-[52%]",
            D: "top-[68%] left-[23%]",
            E: "top-[40%] left-[66%]",
            F: "top-[27%] left-[40%]",
            G: "top-[65%] left-[30%]",
            H: "top-[64%] left-[48%]",
          };

          return (
            <button
              key={letter}
              onClick={() => showInfo(letter)}
              className={`absolute ${positions[letter]} bg-blue-600 hover:bg-blue-700 text-white text-xs px-2 py-1 rounded-full shadow-md`}
            >
              {letter}
            </button>
          );
        })}
      </div>

      {/* Table Display for selected zone */}
      {selectedLetter && selectedData.length > 0 && (
        <div className="lg:absolute lg:left-[24px] lg:right-[24px] mt-6 bg-gray-100 p-4 rounded border shadow">
          <h4 className="text-md font-semibold mb-2">
            Details for Zone: {selectedLetter}
          </h4>

          <div className="overflow-x-auto">
            <table className="min-w-full table-auto">
              <thead>
                <tr className="bg-gray-200">
                  <th className="px-4 py-2 text-left">Sr. No</th>
                  <th className="px-4 py-2 text-left">Location Name</th>
                  <th className="px-4 py-2 text-left">Zone</th>
                  <th className="px-4 py-2 text-left">Installed Capacity</th>
                  <th className="px-4 py-2 text-left">Tenaments</th>
                  <th className="px-4 py-2 text-left">Date</th>
                  <th className="px-4 py-2 text-left">Flow (m³/Hr)</th>
                  <th className="px-4 py-2 text-left">pH</th>
                  <th className="px-4 py-2 text-left">DO (mg/L)</th>
                  <th className="px-4 py-2 text-left">BOD (mg/L)</th>
                  <th className="px-4 py-2 text-left">COD (mg/L)</th>
                  <th className="px-4 py-2 text-left">TSS (mg/L)</th>
                </tr>
              </thead>
              <tbody>
                {selectedData.map((site, index) => {
                  const formattedDate = new Date(
                    site.addedTime
                  ).toLocaleDateString("en-GB", {
                    day: "2-digit",
                    month: "short",
                    year: "numeric",
                  });

                  return (
                    <tr
                      key={index}
                      className={`${
                        index % 2 === 0 ? "bg-gray-50" : "bg-white"
                      } border-b`}
                    >
                      <td className="px-4 py-2">{index + 1}</td>
                      <td className="px-4 py-2">{site.locationName}</td>
                      <td className="px-4 py-2">{site.title}</td>
                      <td className="px-4 py-2">{site.installedCapacity}</td>
                      <td className="px-4 py-2">{site.noOfTenaments}</td>
                      <td className="px-4 py-2">{formattedDate}</td>
                      <td className="px-4 py-2">{site.flow}</td>
                      <td className="px-4 py-2">{site.ph}</td>
                      <td className="px-4 py-2">{site.doLevel}</td>
                      <td className="px-4 py-2">{site.bod}</td>
                      <td className="px-4 py-2">{site.cod}</td>
                      <td className="px-4 py-2">{site.tss}</td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* Message when no data is available for the selected zone */}
      {selectedLetter && selectedData.length === 0 && (
        <div className="mt-4 text-center text-gray-500">
          No data available for Zone: {selectedLetter}
        </div>
      )}
    </div>
  );
}
