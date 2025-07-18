import React, { useMemo, useState } from "react";
import {
  useReactTable,
  getCoreRowModel,
  getPaginationRowModel,
  flexRender,
} from "@tanstack/react-table";
import STPConfigureForm from "../Forms/StpConfigurationForm";
import { deleteStp } from "../../services/operations/stpLiveAPI";
import { useDispatch } from "react-redux";

const fieldLabels = {
  locationName: "STP Location Name",
  title: "Zone",
  installedCapacity: "Installed Capacity",
  noOfTenaments: "No. of Tenaments",
  addedTime: "Date",
  flow: "Flow Rate (KLD)",
  ph: "pH (6.5-9.0)",
  doLevel: "DO (mg/L)",
  bod: "BOD (10.0) (mg/L)",
  cod: "COD (50.0) (mg/L)",
  tss: "TSS (20.0) (mg/L)",
  stpWorking: "Working",
  stpOms: "OMS Available",
};

const standardLimits = {
  ph: 9.0,
  bod: 10.0,
  cod: 50.0,
  tss: 20.0,
  doLevel: 2,
};

const STPTable = ({ data }) => {
  const [showForm, setShowForm] = useState(false);
  const [selectedZone, setSelectedZone] = useState(null);
  const dispatch = useDispatch();

  const columns = useMemo(() => {
    const keys = Object.keys(fieldLabels);
    const dynamicColumns = keys.map((key) => ({
      accessorKey: key,
      header: fieldLabels[key],
      cell: (info) => {
        const value = info.getValue();
        const highLimit = standardLimits[key];

        if (typeof value === "boolean") {
          return <span>{value ? "Yes" : "No"}</span>;
        }

        if (value === null || value === undefined || value === "") {
          return "-";
        }

        let bgClass = "";

        if (highLimit !== undefined && !isNaN(Number(value))) {
          const numericValue = parseFloat(value);
          if (numericValue > highLimit) {
            bgClass = "bg-red-600 text-white"; // Above high range = red
          } else {
            bgClass = "bg-orange-500 text-white"; // Below high range = orange
          }
        }

        return <span className={`px-2 py-1 rounded ${bgClass}`}>{value}</span>;
      },
    }));

    return [
      {
        header: "SNo",
        cell: ({ row, table }) =>
          row.index +
          1 +
          table.getState().pagination.pageIndex *
            table.getState().pagination.pageSize,
      },
      ...dynamicColumns,
      {
        header: "Actions",
        cell: ({ row }) => (
          <div className="flex gap-2">
            <button
              onClick={() => {
                setSelectedZone(row.original); // 👈 Pass full row data
                setShowForm(true);
              }}
              className="px-3 py-1 bg-blue-500 text-white rounded hover:bg-blue-600 text-xs"
            >
              Edit
            </button>
            <button
              onClick={() => {
                console.log("Deleting zone with id:", row.original.id);
                dispatch(deleteStp(row.original.id));
              }}
              className="px-3 py-1 bg-red-500 text-white rounded hover:bg-red-600 text-xs"
            >
              Delete
            </button>
          </div>
        ),
      },
    ];
  }, []);

  const table = useReactTable({
    data,
    columns,
    getCoreRowModel: getCoreRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
  });

  return (
    <div className="flex-1 px-6 rounded-xl min-h-screen">
      {/* Table Scroll Container */}
      <div className="overflow-x-auto border border-[#2f7d32]">
        <table className="min-w-full divide-y divide-gray-200 text-sm">
          <thead className="bg-[#e6f4e8] text-[#2f7d32]">
            {table.getHeaderGroups().map((headerGroup) => (
              <tr key={headerGroup.id}>
                {headerGroup.headers.map((header) => (
                  <th
                    key={header.id}
                    className="px-6 py-4 text-left font-bold uppercase tracking-wide sticky top-0 bg-[#e6f4e8] z-10"
                  >
                    {flexRender(
                      header.column.columnDef.header,
                      header.getContext()
                    )}
                  </th>
                ))}
              </tr>
            ))}
          </thead>
          <tbody className="divide-y divide-gray-100">
            {table.getRowModel().rows.map((row) => (
              <tr
                key={row.id}
                className="hover:bg-blue-50 transition-colors duration-150"
              >
                {row.getVisibleCells().map((cell) => (
                  <td
                    key={cell.id}
                    className="px-6 py-4 whitespace-nowrap text-gray-800"
                  >
                    {flexRender(cell.column.columnDef.cell, cell.getContext())}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Pagination (fixed below the table) */}
      <div className="flex justify-between items-center mt-4 text-sm text-gray-600">
        <button
          onClick={() => table.previousPage()}
          disabled={!table.getCanPreviousPage()}
          className="bg-white text-[#2f7d32] border border-[#2f7d32] px-4 py-2 rounded-md hover:bg-[#f1faf2] transition-colors focus:outline-none focus:ring-2 focus:ring-[#a0d4aa] shadow cursor-pointer"
        >
          ← Previous
        </button>
        <span>
          Page {table.getState().pagination.pageIndex + 1} of{" "}
          {table.getPageCount()}
        </span>
        <button
          onClick={() => table.nextPage()}
          disabled={!table.getCanNextPage()}
          className="bg-white text-[#2f7d32] border border-[#2f7d32] px-4 py-2 rounded-md hover:bg-[#f1faf2] transition-colors focus:outline-none focus:ring-2 focus:ring-[#a0d4aa] shadow cursor-pointer"
        >
          Next →
        </button>
      </div>

      {/* Modal Drawer */}
      {showForm && (
        <div
          className="fixed inset-0 bg-black bg-opacity-40 z-50 flex justify-center items-center"
          onClick={() => setShowForm(false)}
        >
          <div
            className="w-[90%] max-w-4xl bg-white h-fit shadow-lg overflow-y-auto relative rounded-lg"
            onClick={(e) => e.stopPropagation()}
          >
            <STPConfigureForm
              onClose={() => setShowForm(false)}
              isEditing={Boolean(selectedZone)}
              initialData={selectedZone}
            />
          </div>
        </div>
      )}
    </div>
  );
};

export default STPTable;
