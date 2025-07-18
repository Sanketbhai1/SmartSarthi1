import React, { useCallback, useEffect, useMemo, useState } from "react";
import { useReactTable, getCoreRowModel, getSortedRowModel, getPaginationRowModel, flexRender } from "@tanstack/react-table";
import debounce from "lodash.debounce";
import { FiArrowDown, FiArrowUp } from "react-icons/fi";
import { HiEye, HiDownload } from "react-icons/hi";
import { downloadFullFormPdfFromStore, downloadStpPhotoFromStore } from "../../services/operations/surveyAPI";
import { useSelector } from "react-redux";

const SurveyTable = ({ data }) => {
  const [sorting, setSorting] = useState([]);
  const [globalFilter, setGlobalFilter] = useState("");
  const [searchTerm, setSearchTerm] = useState("");
  const [tableData, setTableData] = useState(data);

  const { surveyData, loading } = useSelector((state) => state.survey);

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
    return tableData.filter((row) =>
      row.societyName?.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }, [tableData, searchTerm]);

  const columns = useMemo(
    () => [
      {
        header: "S.No",
        cell: ({ row, table }) =>
          row.index +
          1 +
          table.getState().pagination.pageIndex *
            table.getState().pagination.pageSize,
      },
      {
        accessorKey: "wardOffice",
        header: "Ward Office",
      },
      {
        accessorKey: "wardNo",
        header: "Ward No",
      },
      {
        accessorKey: "societyNo",
        header: "Society No",
      },
      {
        accessorKey: "societyName",
        header: "Society Name",
      },
      {
        accessorKey: "societyAddress",
        header: "Society Address",
      },
      {
        header: "Actions",
        cell: ({ row }) => (
          <div className="flex gap-2">
            <button
              onClick={() => downloadStpPhotoFromStore(surveyData, row.original)}
              className="p-2 rounded-full bg-blue-100 hover:bg-blue-200 transition-colors"
            >
              <HiEye size={16} />
            </button>
            <button
              onClick={() => downloadFullFormPdfFromStore(surveyData, row.original)}
              disabled={loading || !surveyData || surveyData.length === 0}
              className={`p-2 rounded-full transition-colors ${
                !surveyData || surveyData.length === 0
                  ? "bg-gray-300 cursor-not-allowed"
                  : "bg-green-100 hover:bg-green-200"
              }`}
            >
              <HiDownload size={16} />
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
    state: { sorting },
    onSortingChange: setSorting,
    getCoreRowModel: getCoreRowModel(),
    getSortedRowModel: getSortedRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
  });

  useEffect(() => {
    if (surveyData && surveyData.length > 0) {
      setTableData(surveyData);
    }
  }, [surveyData]);

  return (
    <div className="p-6 max-w-full">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-3xl font-bold text-[#2e7d32]">Societies</h2>
        <input
          type="text"
          placeholder="Search societies..."
          value={globalFilter}
          onChange={handleFilterChange}
          className="border border-gray-300 rounded-lg px-4 py-2 text-sm shadow-md focus:outline-none focus:ring-2 focus:ring-[#2e7d32] transition ease-in-out duration-300"
        />
      </div>

      <div className="overflow-x-auto rounded border border-[#2e7d32] shadow-2xl bg-white">
        <table className="min-w-full text-sm table-auto text-gray-800">
          <thead className="bg-gradient-to-r from-[#2e7d32] to-[#60ad5e] text-white rounded-t-2xl">
            {table.getHeaderGroups().map((headerGroup) => (
              <tr key={headerGroup.id}>
                {headerGroup.headers.map((header) => (
                  <th
                    key={header.id}
                    onClick={header.column.getToggleSortingHandler()}
                    className="px-6 py-4 text-left font-semibold tracking-wider uppercase cursor-pointer select-none text-sm"
                  >
                    <div className="flex items-center gap-2">
                      {flexRender(
                        header.column.columnDef.header,
                        header.getContext()
                      )}
                      {header.column.getIsSorted() === "asc" && (
                        <FiArrowUp size={14} />
                      )}
                      {header.column.getIsSorted() === "desc" && (
                        <FiArrowDown size={14} />
                      )}
                    </div>
                  </th>
                ))}
              </tr>
            ))}
          </thead>

          <tbody className="bg-white text-gray-800">
            {table.getRowModel().rows.map((row) => (
              <tr
                key={row.id}
                className="transition-all duration-300 hover:bg-[#f1f8f2] hover:shadow-inner"
              >
                {row.getVisibleCells().map((cell) => (
                  <td
                    key={cell.id}
                    className="px-6 py-4 text-left whitespace-nowrap"
                  >
                    {flexRender(cell.column.columnDef.cell, cell.getContext())}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="flex justify-between items-center mt-8 text-sm">
        <button
          className="px-6 py-2 border rounded-md hover:bg-[#1b5e20] text-white font-semibold shadow bg-[#43a047] transition-all duration-300 ease-in-out cursor-pointer"
          onClick={() => table.previousPage()}
          disabled={!table.getCanPreviousPage()}
        >
          Previous
        </button>

        <span className="text-gray-700 font-medium">
          Page {table.getState().pagination.pageIndex + 1} of{" "}
          {table.getPageCount()}
        </span>

        <button
          className="px-6 py-2 border rounded-md hover:bg-[#1b5e20] text-white font-semibold shadow bg-[#43a047] transition-all duration-300 ease-in-out cursor-pointer"
          onClick={() => table.nextPage()}
          disabled={!table.getCanNextPage()}
        >
          Next
        </button>
      </div>
    </div>
  );
};

export default SurveyTable;
