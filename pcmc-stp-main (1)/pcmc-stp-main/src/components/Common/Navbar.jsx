import React from "react";
import { NavbarLinks } from "../../data/navbar-links";
import { Link, useNavigate } from "react-router-dom";
import { BsChevronDown } from "react-icons/bs";
import { logout } from "../../services/operations/authAPI";
import { useDispatch } from "react-redux";

export default function Navbar() {

  const dispatch = useDispatch();
  const navigate = useNavigate();

  return (
    <div className=" flex justify-center items-center h-16 bg-[#2f7d32] text-white px-10 py-8 ">
      <div className="flex justify-between items-center w-full text-lg">
        <Link to="/dashboard">
          <h2 className=" cursor-pointer text-lg text-center">SMART SEWERAGE <br /> (OWQMS)</h2>
        </Link>
        <ul className=" lg:flex gap-x-6 hidden text-sm items-center">
          {NavbarLinks.map((link, index) => (
            <li key={index} className="relative group text-base rounded-md px-2 py-2 hover:text-[#2f7d32] hover:bg-white">
              {link.subLinks ? (
                <div className="flex text-base items-center gap-2 cursor-pointer rounded-md   ">
                  {link.icon}
                  <span>{link.title}</span>
                  <BsChevronDown className="mt-1" />

                  {/* Dropdown */}
                  <ul
                    className={` absolute top-full left-0 z-50 mt-2 w-max rounded-md bg-white shadow-lg opacity-0 invisible transform translate-y-2 transition-all duration-300 ease-in-out group-hover:opacity-100 group-hover:visible group-hover:translate-y-0 `}
                  >
                    {link.subLinks.map((subLink, subIndex) => (
                      <li key={subIndex}>
                        {subLink.action === "logout" ? (
                          <button
                            onClick={() => {
                              // Handle your logout logic here
                              dispatch(logout(navigate))
                            }}
                            className="w-full text-left px-4 py-2 rounded-md text-gray-700 hover:bg-white hover:text-[#2f7d32]"
                          >
                            {subLink.title}
                          </button>
                        ) : (
                          <Link
                            to={subLink.path}
                            className="block px-4 py-2 text-gray-700 rounded-md  hover:bg-gray-200 hover:text-[#2f7d32]"
                          >
                            {subLink.title}
                          </Link>
                        )}
                      </li>
                    ))}
                  </ul>
                </div>
              ) : (
                <Link to={link.path} className="flex items-center gap-2">
                  {link.icon}
                  <span>{link.title}</span>
                </Link>
              )}
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}
