import { useState, useEffect } from "react";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const ViewAllBanks = () => {
  let navigate = useNavigate();
  const [allBanks, setAllBanks] = useState([]);

  const admin_jwtToken = sessionStorage.getItem("admin-jwtToken");

  const retrieveAllBanks = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/bank/fetch/all",
      {
        headers: {
          Authorization: "Bearer " + admin_jwtToken, // Replace with your actual JWT token
        },
      }
    );
    console.log(response.data);
    return response.data;
  };

  useEffect(() => {
    const getAllBanks = async () => {
      const allBanks = await retrieveAllBanks();
      if (allBanks) {
        setAllBanks(allBanks.banks);
      }
    };

    getAllBanks();
  }, []);

  return (
    <div>
      <div className="mt-2">
        <div
          className="card form-card ms-5 me-5 mb-5 custom-bg border-color "
          style={{
            height: "45rem",
          }}
        >
          <div className="card-header custom-bg-text text-center bg-color">
            <h2>All Branches</h2>
          </div>
          <div
            className="card-body"
            style={{
              overflowY: "auto",
            }}
          >
            <div className="table-responsive">
              <table className="table table-hover text-color text-center">
                <thead className="table-bordered border-color bg-color custom-bg-text">
                  <tr>
                    <th scope="col">Branches</th>
                    <th scope="col">Branches Code</th>
                    <th scope="col">Address</th>
                    <th scope="col">Phone Number</th>
                    <th scope="col">Email</th>
                    <th scope="col">Website</th>
                    <th scope="col">Country</th>
                    <th scope="col">Currency</th>
                  </tr>
                </thead>
                <tbody>
                  {allBanks.map((bank) => {
                    return (
                      <tr>
                        <td>
                          <b>{bank.name}</b>
                        </td>
                        <td>
                          <b>{bank.code}</b>
                        </td>
                        <td>
                          <b>{bank.address}</b>
                        </td>
                        <td>
                          <b>{bank.phoneNumber}</b>
                        </td>
                        <td>
                          <b>{bank.email}</b>
                        </td>
                        <td>
                          <b>{bank.website}</b>
                        </td>
                        <td>
                          <b>{bank.country}</b>
                        </td>
                        <td>
                          <b>{bank.currency}</b>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ViewAllBanks;
