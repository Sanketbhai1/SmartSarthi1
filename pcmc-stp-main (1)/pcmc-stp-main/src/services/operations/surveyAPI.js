import toast from "react-hot-toast";
import { apiConnector } from "../apiconnector";
import { surveyEndpoints } from "../apis";
import { setLoading, setSurveyData } from "../../slices/surveySlice";
import jsPDF from "jspdf";
import autoTable from "jspdf-autotable";
import logo from "../../assets/formPdfLogos/formlogo.jpg"
import logo2 from "../../assets/formPdfLogos/formlogo2.jpg"

const { GET_SURVEY_API, GET_SURVEY_FORM_API, GET_STP_PHOTO_API } = surveyEndpoints;

export function fetchSurveyData() {
  return async (dispatch) => {
    const toastId = toast.loading("Loading...");
    const storedSurveyData = localStorage.getItem("surveyData");
    if (storedSurveyData) {
      // If data exists in localStorage, directly set it in the store
      dispatch(setSurveyData(JSON.parse(storedSurveyData)));
      toast.dismiss(toastId); // Dismiss the loading toast if data is from localStorage
      return; // No need to fetch again
    }

    try {
      dispatch(setLoading(true));
      const response = await apiConnector(
        "GET",
        GET_SURVEY_API,
        null,
        {},
        {
          withCredentials: true,
        }
      );

      console.log("SURVEY response:", response);

      // Step 3: Save fetched data to Redux and localStorage
      dispatch(setSurveyData(response.data)); // Ensure you're dispatching the action correctly
      localStorage.setItem("surveyData", JSON.stringify(response.data)); // Persist to localStorage
    } catch (error) {
      console.error("SURVEY API ERROR:", error);
      toast.error("Failed to fetch survey data! Please try again later.");
    } finally {
      toast.dismiss(toastId);
      dispatch(setLoading(false));
    }
  };
}



// 
// 
// 
// 
// STP PHOTO DOWNLOAD FROM BACKEND
// 
export async function downloadStpPhoto(row) {
  try {
    setLoading(true); // Start loader

    const response = await apiConnector(
      "GET",
      `${GET_STP_PHOTO_API}/${row.id}`,
      null,
      { responseType: "blob" }
    );

    const blob = new Blob([response.data]);
    const url = window.URL.createObjectURL(blob);
    window.open(url, "_blank");

    // Clean up memory after some time
    setTimeout(() => window.URL.revokeObjectURL(url), 1000);
  } catch (error) {
    console.error("Failed to download STP Photo", error);
    alert("Failed to open STP Photo. Please try again.");
  } finally {
    setLoading(false); // Stop loader
  }
}


// 
// 
// 
// 
// STPFORM PHOTO DOWNLOAD FROM BACKEND
// 


export async function downloadFullStpForm(row) {
  try {
    const token = sessionStorage.getItem("token"); // Get the token from sessionStorage

    // Check if token exists
    if (!token) {
      throw new Error("No authorization token found.");
    }

    const response = await apiConnector(
      "GET",
      `${GET_SURVEY_FORM_API}/${row.id}`,
      null,
      {
        headers: {
          Authorization: `Bearer ${token}`, // Use the token from sessionStorage
        },
        responseType: "blob",
      }
    );

    // Create a URL for the blob object
    const blobUrl = window.URL.createObjectURL(new Blob([response.data]));

    // Create a temporary <a> link to download
    const link = document.createElement('a');
    link.href = blobUrl;

    // File name (optional: you can make it dynamic based on form details)
    link.setAttribute('download', `STP_Form_${row.id}.pdf`);
    
    document.body.appendChild(link);
    link.click();

    // Clean up
    link.remove();
    window.URL.revokeObjectURL(blobUrl);

    console.log(" STP Form downloaded successfully");
  } catch (error) {
    console.error(" Failed to download STP Form", error);
    alert("Error downloading STP Form!");
  }
}



// 
// 
// 
// 
// 
// STP PHOTO DOWNLOAD FROM FRONTEND
// 
function base64ToBlob(base64, mimeType = "image/jpeg") {
  const byteCharacters = atob(base64);
  const byteArrays = [];

  for (let offset = 0; offset < byteCharacters.length; offset += 512) {
    const slice = byteCharacters.slice(offset, offset + 512);
    const byteNumbers = new Array(slice.length);

    for (let i = 0; i < slice.length; i++) {
      byteNumbers[i] = slice.charCodeAt(i);
    }

    const byteArray = new Uint8Array(byteNumbers);
    byteArrays.push(byteArray);
  }

  return new Blob(byteArrays, { type: mimeType });
}

export function downloadStpPhotoFromStore(stpPhotoData, row) {
  const form = stpPhotoData.find((form) => form.id === row.id);

  if (!form || !form.stpPhoto) {
    alert("No STP Photo available for this entry.");
    return;
  }

  const blob = base64ToBlob(form.stpPhoto, "image/jpeg");
  const url = window.URL.createObjectURL(blob);

  const link = document.createElement("a");
  link.href = url;
  link.download = `STP_Photo_${row.id}.jpg`;
  link.click();

  setTimeout(() => {
    window.URL.revokeObjectURL(url);
  }, 100);
}



// 
// 
// 
//
// STP FORM DOWNLOAD FROM FRONTEND
//

export function downloadFullFormPdfFromStore(formData, row) {
  const form = formData.find((form) => form.id === row.id);
  if (!form) {
    alert("Form data not found!");
    return;
  }

  const image1 = new Image();
  const image2 = new Image();
  image1.src = logo;
  image2.src = logo2;

  image1.onload = () => {
    image2.onload = () => {
      const doc = new jsPDF();
      const pageWidth = doc.internal.pageSize.getWidth();

      // Add top header logos
      doc.addImage(image1, "PNG", 10, 10, 30, 20);
      doc.addImage(image2, "PNG", pageWidth - 40, 10, 30, 20);

      // Title
      doc.setFontSize(20);
      doc.setTextColor(40, 40, 40);
      doc.setFont("helvetica", "bold");
      doc.text("STP Form Details", pageWidth / 2, 25, { align: "center" });

      // Subtitle (Optional)
      doc.setFontSize(11);
      doc.setFont("helvetica", "italic");
      doc.setTextColor(100);
      doc.text(`Generated on: ${new Date().toLocaleDateString()}`, pageWidth / 2, 31, { align: "center" });

      const body = [
        ["Society Name", form.societyName],
        ["Ward No", form.wardNo],
        ["Date", form.date],
        ["Ward Office", form.wardOffice],
        ["Society No", form.societyNo],
        ["Society Address", form.societyAddress],
        ["Manager Name", form.managerName],
        ["Manager Mobile No", form.managerMobileNo],
        ["Chairman Name", form.chairmanName],
        ["Chairman Mobile No", form.chairmanMobileNo],
        ["Latitude", form.latitude],
        ["Longitude", form.longitude],
        ["STP Capacity (KL)", form.stpCapacity],
        ["Technology", form.technology],
        ["Total Tenements", form.totalTenements],
        ["Occupied Tenements", form.occupiedTenements],
        ["Water Connection Diameter", form.waterConnectionDiameter],
        ["Water Reused", form.waterReused ? "Yes" : "No"],
        ["Separate Electricity", form.separateElectricity ? "Yes" : "No"],
        ["MSEB Beneficiary", form.msebBeneficiary ? "Yes" : "No"],
        ["Online Monitoring", form.onlineMonitoring ? "Yes" : "No"],
        ["API ID", form.apiId],
        ["STP Operational", form.stpOperational ? "Yes" : "No"],
        ["Non Operational Days", form.nonOperationalDays],
        ["Electricity Units", form.electricityUnits],
        ["Treated Water Uses", (form.treatedWaterUses || []).join(", ")],
        ["Remaining Water Discharge (KL)", form.remainingWaterDischarge],
        ["Nearby Storm Water Line", form.nearByStormWaterLine ? "Yes" : "No"],
        ["Appointed Agency", form.appointedAgency ? "Yes" : "No"],
        ["Agency Name", form.agencyName],
        ["Other Reasons", form.otherReasons],
        ["RWH System", form.rwhSystem ? "Yes" : "No"],
        ["RWH Pits Count", form.rwhPitsCount],
        ["OWC Operational", form.owcOperational ? "Yes" : "No"],
        ["Operational Issues", form.operationalIssues],
      ];

      autoTable(doc, {
        startY: 38,
        head: [["Field", "Value"]],
        body,
        theme: "grid",
        styles: {
          font: "helvetica",
          fontSize: 11,
          cellPadding: 4,
          textColor: 33,
        },
        headStyles: {
          fillColor: [44, 62, 80], // dark header
          textColor: [255, 255, 255],
          fontSize: 12,
          fontStyle: "bold",
        },
        alternateRowStyles: { fillColor: [245, 245, 245] },
        tableLineColor: [180, 180, 180],
        tableLineWidth: 0.2,
      });

      // Optional: Footer (e.g. page number or a tagline)
      const pageHeight = doc.internal.pageSize.getHeight();
      doc.setFontSize(10);
      doc.setTextColor(150);
      doc.text("Generated by STP Monitoring System", pageWidth / 2, pageHeight - 10, { align: "center" });

      const cleanSocietyName = (form.societyName || "Society").replace(/\s+/g, "_");
      doc.save(`Ward-${form.wardNo}_${cleanSocietyName}_STP_Form.pdf`);
    };
  };
}

