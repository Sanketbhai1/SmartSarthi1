import { useSelector } from "react-redux";
import { PieChart, Pie, Cell, Legend } from "recharts";

const COLORS = ["#28a745", "#dc3545"];
const DonutChart = ({ title, data }) => (
  <div className="bg-white shadow-md rounded-lg p-4 w-[250px] text-center border-t-2 border-indigo-100">
    <h2 className="text-xl font-semibold mb-2">{title}</h2>
    <PieChart width={200} height={200}>
      <Pie
        data={data}
        innerRadius={45}
        outerRadius={80}
        paddingAngle={2}
        dataKey="value"
      >
        {data.map((entry, index) => (
          <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
        ))}
      </Pie>
      <Legend />
    </PieChart>
  </div>
);

export default function StatusCharts() {
  const { stpData } = useSelector((state) => state.stp);

  const totalSTPs = stpData.length;
  const activeDevices = stpData.filter((item) => item.stpWorking).length;
  const inactiveDevices = totalSTPs - activeDevices;

  const activeData = [
    { name: "ACTIVE", value: activeDevices },
    { name: "INACTIVE", value: inactiveDevices },
  ];

  return (
    <div className="flex flex-col justify-evenly items-center p-4 mb-10">
      <h3 className="text-xl font-semibold text-gray-700 mb-4">STP Status Overview</h3>
      <DonutChart title="Active/Inactive Status" data={activeData} />
    </div>
  );
}
