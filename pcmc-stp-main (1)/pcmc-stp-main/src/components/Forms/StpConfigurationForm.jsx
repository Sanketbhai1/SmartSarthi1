import { useState, useEffect } from "react";
import PropTypes from "prop-types";
import { useDispatch } from "react-redux";
import { addStp, updateStp } from "../../services/operations/stpLiveAPI";

const fieldLabels = {
  locationName: "STP Location Name",
  flow: "Flow Rate (MLD)",
  doLevel: "DO Level (mg/L)",
  bod: "BOD (mg/L)",
  cod: "COD (mg/L)",
  tss: "TSS (mg/L)",
  ph: "pH Level",
  noOfTenaments: "No. of Tenements",
  installedCapacity: "Installed Capacity (MLD)",
  title: "Zone Name",
  addedTime: "Date of Addition",
  stpWorking: "Working",
  stpOms: "OMS Available",
};

const initialFormState = {
  locationName: "",
  flow: "",
  doLevel: "",
  bod: "",
  cod: "",
  tss: "",
  ph: "",
  noOfTenaments: "",
  installedCapacity: "",
  title: "",
  addedTime: "",
  stpWorking: true,
  stpOms: true,
};

const STPConfigureForm = ({
  onClose,
  initialData = null,
  isEditing = false,
}) => {
  const dispatch = useDispatch();
  const [stpData, setStpData] = useState(initialFormState);
  const [internalId, setInternalId] = useState(null);

  useEffect(() => {
    if (isEditing && initialData) {
      const { id, ...formValues } = initialData;
      setStpData(formValues);
      setInternalId(id);
    }
  }, [initialData, isEditing]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setStpData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (isEditing && internalId) {
      dispatch(updateStp(stpData, internalId));
    } else {
      dispatch(addStp(stpData));
    }

    if (!isEditing) {
      setStpData(initialFormState);
      setInternalId(null);
    }

    onClose();
  };

  const getInputType = (key) => {
    if (["flow", "doLevel", "bod", "cod", "tss", "ph"].includes(key))
      return "number";
    if (key === "addedTime") return "date";
    return "text";
  };

  const getMinAddedTime = () => {
    if (!stpData.addedTime) return null;

    const selectedDate = new Date(stpData.addedTime);
    selectedDate.setDate(selectedDate.getDate() - 7); // 7 days before current value

    return selectedDate.toISOString().split("T")[0]; // YYYY-MM-DD format
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 z-50 flex items-center justify-center">
      <div className="bg-white w-[90%] max-w-4xl max-h-[90vh] overflow-y-auto rounded-xl shadow-lg p-8 relative">
        <button
          onClick={onClose}
          className="absolute top-4 right-5 text-gray-600 hover:text-red-600 text-2xl font-bold"
        >
          &times;
        </button>

        <h2 className="text-2xl font-semibold text-center text-[#2f7d32] mb-6 border-b pb-2">
          {isEditing ? "Edit STP Configuration" : "Add STP Configuration"}
        </h2>

        <form onSubmit={handleSubmit}>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {Object.entries(stpData).map(([key, value]) => (
              <div className="relative z-0 group" key={key}>
                {["stpWorking", "stpOms"].includes(key) ? (
                  <>
                    <label
                      htmlFor={key}
                      className="block text-sm text-gray-500  mb-1"
                    >
                      {fieldLabels[key] || key}
                    </label>
                    <select
                      id={key}
                      name={key}
                      value={value}
                      onChange={(e) =>
                        setStpData((prev) => ({
                          ...prev,
                          [key]: e.target.value === "true",
                        }))
                      }
                      className="w-full p-2 border-b-2 border-gray-300 text-gray-900 bg-white rounded focus:outline-none focus:ring-2 focus:ring-[#2f7d32]"
                    >
                      <option value="true">Yes</option>
                      <option value="false">No</option>
                    </select>
                  </>
                ) : (
                  <>
                    <input
                      id={key}
                      type={getInputType(key)}
                      name={key}
                      value={value}
                      onChange={handleChange}
                      placeholder=" "
                      required
                      min={
                        key === "addedTime" && isEditing
                          ? getMinAddedTime()
                          : undefined
                      }
                      className="block px-0 pt-4 pb-1 w-full text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-[#2f7d32] peer"
                    />
                    <label
                      htmlFor={key}
                      className="absolute text-lg text-gray-500 duration-300 transform -translate-y-5 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-[#2f7d32] peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-2 peer-focus:scale-75 peer-focus:-translate-y-5"
                    >
                      {fieldLabels[key] || key}
                    </label>
                  </>
                )}
              </div>
            ))}
          </div>

          <div className="mt-8 flex justify-end gap-4">
            <button
              type="button"
              onClick={onClose}
              className="px-5 py-2 border border-gray-400 rounded-lg text-gray-700 hover:bg-gray-100 transition"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="px-6 py-2 bg-[#2f7d32] text-white font-semibold rounded-lg hover:bg-green-800 transition"
            >
              {isEditing ? "Update" : "Save"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

STPConfigureForm.propTypes = {
  onClose: PropTypes.func.isRequired,
  isEditing: PropTypes.bool,
  initialData: PropTypes.object,
};

export default STPConfigureForm;
