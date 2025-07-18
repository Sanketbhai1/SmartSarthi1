import { useState } from "react";
import { useSelector } from "react-redux";
import CountUp from "react-countup";

export default function STPStats() {
  const [alerts, setAlerts] = useState(0);

  const { stpData } = useSelector((state) => state.stp);

  const totalSTPs = stpData.length;
  const activeDevices = stpData.filter((item) => item.stpWorking).length;
  const inactiveDevices = totalSTPs - activeDevices;

  const stats = [
    { label: "Total STPs", value: totalSTPs },
    { label: "Active Devices", value: activeDevices },
    { label: "Inactive Devices", value: inactiveDevices },
    { label: "Alerts Raised", value: alerts },
  ];

  return (
    <div className=" md:w-1/2 p-4">
      <h3 className="text-xl font-semibold text-gray-700 mb-4">Key Statistics</h3>
      <div className="grid grid-cols-2 gap-6 mb-8">
        {stats.map((stat, index) => (
          <div
            key={index}
            className="bg-white rounded-xl p-5 shadow-md hover:shadow-xl transition-shadow duration-300 border-t-2 border-indigo-100"
          >
            <p className="text-sm text-gray-500 mb-1">{stat.label}</p>
            <h3 className="text-3xl font-bold text-gray-800">
              <CountUp end={stat.value} duration={1.2} />
            </h3>
          </div>
        ))}
      </div>
    </div>
  );
}
