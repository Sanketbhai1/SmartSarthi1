import React, { useCallback, useMemo, useState } from "react";
import {
  useReactTable,
  getCoreRowModel,
  getSortedRowModel,
  getPaginationRowModel,
  flexRender,
} from "@tanstack/react-table";
import { FiArrowDown, FiArrowUp, FiTrash } from "react-icons/fi";
import { HiPencil } from "react-icons/hi";
import debounce from "lodash.debounce";
import { useDispatch } from "react-redux";
import AddUserForm from "../Forms/AddUserForm";
import { deleteUser } from "../../services/operations/userAPI";

const UsersTable = ({ data }) => {
  const [showForm, setShowForm] = useState(false);
  const [selectedZone, setSelectedZone] = useState(null);

  const [sorting, setSorting] = useState([]);
  const [globalFilter, setGlobalFilter] = useState("");
  const [searchTerm, setSearchTerm] = useState("");
  const dispatch = useDispatch();

  const debouncedSearch = useCallback(
    debounce((value) => {
      setSearchTerm(value);
    }, 300),
    []
  );

  const handleFilterChange = (e) => {
    setGlobalFilter(e.target.value);
    debouncedSearch(e.target.value);
  };

  const filteredData = useMemo(() => {
    return data.filter((row) =>
      row.name?.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }, [data, searchTerm]);

  const columns = useMemo(
    () => [
      {
        header: "SNo",
        cell: ({ row, table }) =>
          row.index +
          1 +
          table.getState().pagination.pageIndex *
            table.getState().pagination.pageSize,
      },
      {
        accessorKey: "name",
        header: "Name",
        cell: (info) => info.getValue(),
      },
      {
        accessorKey: "roles",
        header: "Role",
        cell: (info) => info.getValue(),
      },
      {
        accessorKey: "status",
        header: "Status",
        cell: ({ getValue }) => (
          <span
            className={`px-4 py-1 rounded-full text-xs font-medium uppercase tracking-wide shadow-sm ${
              getValue() ? "bg-green-500 text-white" : "bg-red-400 text-white"
            }`}
          >
            {getValue() ? "ACTIVE" : "INACTIVE"}
          </span>
        ),
      },
      {
        accessorKey: "addedTime",
        header: "Added Time",
        cell: ({ getValue }) =>
          new Date(getValue()).toLocaleDateString("en-US", {
            year: "numeric",
            month: "short",
            day: "numeric",
          }),
      },
      {
        header: "Actions",
        cell: ({ row }) => (
          <div className="flex gap-2">
            <button
              onClick={() => {
                setSelectedZone(row.original);
                setShowForm(true);
              }}
              className="p-2 rounded-full bg-blue-100 hover:bg-blue-200 transition-colors"
            >
              <HiPencil size={16} />
            </button>
            <button
              onClick={() => {
                console.log("Deleting item with id:", row.original.id);
                dispatch(deleteUser(row.original.id));
              }}
              className="p-2 rounded-full bg-red-100 hover:bg-red-200 transition-colors"
            >
              <FiTrash size={16} />
            </button>
          </div>
        ),
      },
    ],
    []
  );  

  const table = useReactTable({
    data: filteredData,
    columns,
    state: {
      sorting,
    },
    onSortingChange: setSorting,
    getCoreRowModel: getCoreRowModel(),
    getSortedRowModel: getSortedRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
  });

  return (
    <div className="p-6 rounded-2xl shadow-lg bg-white max-w-full mx-auto border border-gray-200">
      {/* Header */}
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-3xl font-bold text-gray-800">Users</h2>
        <input
          type="text"
          placeholder="Search"
          value={globalFilter}
          onChange={handleFilterChange}
          className="border border-gray-300 rounded-lg px-4 py-2 text-sm shadow-sm focus:outline-none focus:ring-2 focus:ring-black"
        />
      </div>

      {/* Table */}
      <div className="overflow-x-auto rounded-xl border border-gray-200 shadow-sm">
        <table className="min-w-full text-sm">
          <thead className="bg-gradient-to-r from-gray-100 to-gray-200">
            {table.getHeaderGroups().map((headerGroup) => (
              <tr key={headerGroup.id}>
                {headerGroup.headers.map((header) => (
                  <th
                    key={header.id}
                    onClick={header.column.getToggleSortingHandler()}
                    className="px-6 py-4 text-left font-semibold text-gray-700 uppercase tracking-wider cursor-pointer select-none"
                  >
                    <div className="flex items-center gap-1">
                      {flexRender(
                        header.column.columnDef.header,
                        header.getContext()
                      )}
                      {{
                        asc: <FiArrowUp size={14} />,
                        desc: <FiArrowDown size={14} />,
                      }[header.column.getIsSorted()] ?? null}
                    </div>
                  </th>
                ))}
              </tr>
            ))}
          </thead>

          <tbody>
            {table.getRowModel().rows.map((row, rowIndex) => (
              <tr
                key={row.id}
                className="transition-colors duration-150 hover:bg-teal-50 border-b border-gray-200"
              >
                {row.getVisibleCells().map((cell) => (
                  <td
                    key={cell.id}
                    className="px-6 py-4 text-gray-700 whitespace-nowrap"
                  >
                    {flexRender(cell.column.columnDef.cell, cell.getContext())}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Pagination */}
      <div className="flex justify-between items-center mt-6 text-sm">
        <button
          className="px-5 py-2 border rounded-lg bg-gray-100 hover:bg-gray-400 transition disabled:opacity-50 cursor-pointer"
          onClick={() => table.previousPage()}
          disabled={!table.getCanPreviousPage()}
        >
          Previous
        </button>
        <span className="text-gray-600">
          Page {table.getState().pagination.pageIndex + 1} of{" "}
          {table.getPageCount()}
        </span>
        <button
          className="px-5 py-2 border rounded-lg bg-gray-100 hover:bg-gray-400 transition disabled:opacity-50 cursor-pointer"
          onClick={() => table.nextPage()}
          disabled={!table.getCanNextPage()}
        >
          Next
        </button>
      </div>

      {/* Drawer Form from center */}
      {showForm && (
        <div
          className="fixed inset-0 bg-black bg-opacity-40 z-50 flex justify-center items-center"
          onClose={() => setShowForm(false)}
        >
          <div
            className="w-full max-w-md  bg-white h-fit shadow-lg overflow-y-auto relative"
            onClick={(e) => e.stopPropagation()}
          >
            <AddUserForm
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

export default UsersTable;
