import React, { useEffect, useMemo, useState, useCallback } from "react";
import STPTable from "../components/Tables/STPTable";
import { useDispatch, useSelector } from "react-redux";
import { fetchStp } from "../services/operations/stpLiveAPI";
import { HiLocationMarker } from "react-icons/hi";
import debounce from "lodash.debounce";

const STPDashboard = () => {
  const [selectedLocation, setSelectedLocation] = useState(null);

  const { stpData } = useSelector((state) => state.stp);
  const dispatch = useDispatch();

  const [searchTerm, setSearchTerm] = useState("");
  const [tenamentRange, setTenamentRange] = useState("");
  const [capacityRange, setCapacityRange] = useState("");

  const handleTenamentChange = (e) => {
    setTenamentRange(e.target.value);
    setCapacityRange("");
  };

  // Handle Capacity dropdown change
  const handleCapacityChange = (e) => {
    setCapacityRange(e.target.value);
    setTenamentRange("");
  };

  const tenamentRanges = [
    { label: "All", value: "" },
    { label: "100-200", value: "100-200" },
    { label: "200-300", value: "200-300" },
    { label: "300-500", value: "300-500" },
    { label: "500 & above", value: "500+" },
  ];

  const capacityRanges = [
    { label: "All", value: "" },
    { label: "0-300", value: "0-300" },
    { label: "300-500", value: "300-500" },
    { label: "500 & above", value: "500+" },
  ];

  useEffect(() => {
    dispatch(fetchStp());
  }, [dispatch]);

  const locations = useMemo(
    () => [...new Set(stpData.map((item) => item.locationName))],
    [stpData]
  );

  // Handle debounced search
  const debouncedSearch = useCallback(
    debounce((value) => {
      setSearchTerm(value.toLowerCase());
    }, 300),
    []
  );

  useEffect(() => {
    return () => {
      debouncedSearch.cancel();
    };
  }, [debouncedSearch]);

  const handleSearchChange = (e) => {
    debouncedSearch(e.target.value);
  };

  // Filter data based on searchTerm
  const filteredData = useMemo(() => {
    let filtered = [...stpData];

    if (selectedLocation) {
      filtered = filtered.filter((d) => d.locationName === selectedLocation);
    }

    if (searchTerm.trim()) {
      const lowerSearch = searchTerm.toLowerCase();
      filtered = filtered.filter((d) => {
        const locationMatch = d.locationName
          ?.toLowerCase()
          .includes(lowerSearch);
        const titleMatch = d.title?.toLowerCase().includes(lowerSearch);
        const workingMatch =
          d.stpWorking !== undefined
            ? (d.stpWorking ? "yes" : "no").includes(lowerSearch)
            : false;
        return locationMatch || titleMatch || workingMatch;
      });
    }

    if (tenamentRange) {
      const [min, max] = tenamentRange.split("-");
      filtered = filtered.filter((d) => {
        const value = Number(d.noOfTenaments);
        return tenamentRange === "500+"
          ? value >= 500
          : value >= Number(min) && value <= Number(max);
      });
    }

    if (capacityRange) {
      const [min, max] = capacityRange.split("-");
      filtered = filtered.filter((d) => {
        const value = Number(d.installedCapacity);
        return capacityRange === "500+"
          ? value >= 500
          : value >= Number(min) && value <= Number(max);
      });
    }

    return filtered;
  }, [stpData, selectedLocation, searchTerm, tenamentRange, capacityRange]);

  return (
    <div className="flex min-h-screen">
      {/* Sidebar */}
      <div className="w-1/5 border-r border-gray-200 p-6 bg-gradient-to-b from-gray-50 to-white shadow-md">
        <h3 className="text-xl font-bold text-gray-800 mb-6">STP Locations</h3>
        <ul className="space-y-2">
          {locations.map((loc) => (
            <li key={loc}>
              <button
                className={`w-full flex items-center gap-3 px-4 py-3 rounded-lg transition text-sm font-medium ${
                  selectedLocation === loc
                    ? "bg-blue-600 text-white shadow-sm"
                    : "bg-white hover:bg-blue-50 text-gray-700"
                }`}
                onClick={() =>
                  setSelectedLocation(selectedLocation === loc ? null : loc)
                }
              >
                <HiLocationMarker className="text-lg" />
                {loc}
              </button>
            </li>
          ))}
        </ul>
      </div>

      {/* Main Content */}
      <div className="w-4/5 py-6">
        {/* Dashboard Title */}
        <div className="px-8 mb-8 text-center">
          <h1 className="text-4xl font-bold text-gray-800">
            Private STP Dashboard
          </h1>
          <p className="text-sm text-gray-500 mt-1">
            View and manage STP information
          </p>
        </div>

        {/* Search and Filters */}
        <div className="px-8 flex flex-col md:flex-row justify-between gap-6 mb-6 items-center">
          {/* Search Input */}
          <div className="w-full md:w-1/3">
            <label className="text-sm font-medium text-gray-700 mb-1 block">
              Search
            </label>
            <input
              type="search"
              placeholder="Search..."
              onChange={handleSearchChange}
              className="w-full px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm focus:outline-none focus:ring-2 focus:ring-blue-300"
            />
          </div>

          {/* Filters */}
          <div className="flex gap-6 w-full md:w-2/3 justify-end">
            {/* Tenaments Dropdown */}
            <div className="flex flex-col w-40">
              <label
                htmlFor="tenamentRange"
                className="text-sm font-medium text-gray-700 mb-1"
              >
                Tenaments Range
              </label>
              <select
                id="tenamentRange"
                value={tenamentRange}
                onChange={handleTenamentChange}
                className="px-3 py-2 border border-gray-300 rounded-md text-sm shadow-sm"
              >
                {tenamentRanges.map((range) => (
                  <option key={range.value} value={range.value}>
                    {range.label}
                  </option>
                ))}
              </select>
            </div>

            {/* Capacity Dropdown */}
            <div className="flex flex-col w-40">
              <label
                htmlFor="capacityRange"
                className="text-sm font-medium text-gray-700 mb-1"
              >
                Capacity Range
              </label>
              <select
                id="capacityRange"
                value={capacityRange}
                onChange={handleCapacityChange}
                className="px-3 py-2 border border-gray-300 rounded-md text-sm shadow-sm"
              >
                {capacityRanges.map((range) => (
                  <option key={range.value} value={range.value}>
                    {range.label}
                  </option>
                ))}
              </select>
            </div>
          </div>
        </div>

        {/* Table */}
        <STPTable data={filteredData} />
      </div>
    </div>
  );
};

export default STPDashboard;
