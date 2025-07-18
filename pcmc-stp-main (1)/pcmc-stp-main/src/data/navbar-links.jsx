import {
  FiHome,
  FiSettings,
  FiUser,
  FiBell,
  FiFileText,
  FiShield,
  FiBox,
  FiDatabase,
} from "react-icons/fi";

export const NavbarLinks = [
  {
    title: "Home",
    path: "/dashboard",
    icon: <FiHome />,
  },
  {
    title: "Private STP",
    path: "/private-stp",
    icon: <FiBox />,
  },
  {
    title: "Survey Data",
    path: "/survey",
    icon: <FiDatabase />,
  },
  {
    title: "Alarms",
    icon: <FiBell />,
    subLinks: [
      { title: "Manage Alarms", path: "/dashboard" },
      { title: "Alarm History", path: "/dashboard" },
    ],
  },
  {
    title: "Reports",
    path: "/dashboard",
    icon: <FiFileText />,
  },
  {
    title: "Licence",
    path: "/dashboard",
    icon: <FiShield />,
  },
  {
    title: "Setting",
    icon: <FiSettings />,
    subLinks: [
      { title: "Private STP Configuration ", path: "/setting/pvt-stp-config" },
      { title: "Manage Zone", path: "/setting/manage-zone" },
      { title: "Manage Users", path: "/setting/manage-user" },
      { title: "Manage Roles", path: "/setting/manage-user" },
    ],
  },
  {
    title: "User",
    icon: <FiUser />,
    type: "user",
    subLinks: [
      { title: "Sign Out", action: "logout" },
    ],
  },
];
