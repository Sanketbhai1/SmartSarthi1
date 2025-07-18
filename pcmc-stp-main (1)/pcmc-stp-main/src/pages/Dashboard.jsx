import React from "react";
import MapComponent from "../components/Dashboard/MapComponent";
import StatusCharts from "../components/Dashboard/StatusCharts";
import STPStats from "../components/Dashboard/STPStats";

export default function Dashboard() {
  return (
    <div>
      <div className="flex">
        {/* <!-- Main Content --> */}
        <main className="flex-1 p-6">
          {/* <!-- Header --> */}
          <div className="mb-8 text-center ">
            <h2 className="text-4xl font-bold mb-1.5">Dashboard</h2>
            <p className="text-gray-500 text-sm">
              Overview of water quality monitoring system
            </p>
          </div>

          {/* <!-- ACTIVE/INACTIVE STATUS --> */}
          <div className=" md:flex justify-around items-start">
            {/* <!-- PieChart --> */}
            <StatusCharts />

            {/* <!-- Stat Cards --> */}
            <STPStats />
          </div>

          {/* <!-- Map and Alerts --> */}
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-4">
            <div className="bg-white p-4 rounded shadow lg:col-span-2">
              <h3 className="text-xl font-semibold text-gray-800 mb-2">
                PCMC Area Map
              </h3>
              <div className="flex justify-end mt-2 space-x-4 text-sm">
                <span className="text-green-600 font-medium">● Active</span>
                <span className="text-red-600 font-medium">● Inactive</span>
              </div>
              <MapComponent />
            </div>

            <div className="bg-white p-4 rounded shadow">
              <div className="flex justify-between items-center mb-2">
                <h3 className=" text-xl font-semibold text-gray-800 mb-2">
                  Recent Alerts
                </h3>
                <a
                  href="#"
                  className="text-md font-bold text-green-800 hover:text-green-600 transition duration-200 hover:underline"
                >
                  View All
                </a>
              </div>
              <ul className="space-y-2 text-sm">
                <li className="bg-red-100 border-l-4 border-red-500 p-2">
                  <strong>pH Level Critical</strong>
                  <br />
                  Dapodi STP pH level reached 9.8
                  <span className="text-xs text-gray-500">2h ago</span>
                </li>
                <li className="bg-yellow-100 border-l-4 border-yellow-500 p-2">
                  <strong>Flow Rate Warning</strong>
                  <br />
                  Chikhali Phase-1 STP flow rate below threshold
                  <span className="text-xs text-gray-500">5h ago</span>
                </li>
                <li className="bg-gray-100 border-l-4 border-gray-500 p-2">
                  <strong>Device Offline</strong>
                  <br />
                  Chikhali Phase-1 STP not responding
                  <span className="text-xs text-gray-500">8h ago</span>
                </li>
                <li className="bg-orange-100 border-l-4 border-orange-500 p-2">
                  <strong>Temperature High</strong>
                  <br />
                  Akurdi STP temperature reached 35°C
                  <span className="text-xs text-gray-500">12h ago</span>
                </li>
                <li className="bg-red-50 border-l-4 border-red-400 p-2">
                  <strong>Maintenance Required</strong>
                  <br />
                  Kasarwadi Phase-2 STP scheduled maintenance
                  <span className="text-xs text-gray-500">due</span>
                </li>
              </ul>
            </div>
          </div>
        </main>
      </div>
    </div>
  );
}
