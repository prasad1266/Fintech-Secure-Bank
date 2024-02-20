import { useState, useEffect } from "react";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const ViewAllBankAccounts = () => {
  let navigate = useNavigate();
  const [allAccounts, setAccounts] = useState([]);

  const [accountNumber, setAccountNumber] = useState("");

  const [tempAccountNumber, setTempAccountNumber] = useState("");

  const admin_jwtToken = sessionStorage.getItem("admin-jwtToken");

  const [updateBankAccountStatusRequest, setUpdateBankAccountStatusRequest] =
    useState({
      accountId: "",
      status: "",
    });

  const retrieveAllAccounts = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/bank/account/fetch/all",
      {
        headers: {
          Authorization: "Bearer " + admin_jwtToken, // Replace with your actual JWT token
        },
      }
    );
    console.log(response.data);
    return response.data;
  };

  const retrieveAllAccountsByBankAccount = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/bank/account/search/all?accountNumber=" +
        accountNumber,
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
    if (accountNumber !== "") {
      const getAllCustomersByName = async () => {
        const bankAccounts = await retrieveAllAccountsByBankAccount();
        if (bankAccounts) {
          setAccounts(bankAccounts.accounts);
        }
      };
      getAllCustomersByName();
    } else {
      const getAllAccounts = async () => {
        const bankAccounts = await retrieveAllAccounts();
        if (bankAccounts) {
          setAccounts(bankAccounts.accounts);
        }
      };

      getAllAccounts();
    }
  }, [accountNumber]);

  const searchBankAccountsByAccountNumber = (e) => {
    e.preventDefault();
    setAccountNumber(tempAccountNumber);
  };

  const viewAccountDetails = (customer) => {
    navigate("/customer/bank/account/detail", { state: customer });
  };

  const viewAccountStatement = (customer) => {
    navigate("/customer/bank/account/statement", { state: customer });
  };

  const openAccount = (accountId, e) => {
    updateBankAccountStatusRequest.accountId = accountId;
    updateBankAccountStatusRequest.status = "Open";

    fetch("http://localhost:8080/api/bank/account/update/status", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(updateBankAccountStatusRequest),
    })
      .then((result) => {
        console.log("result", result);
        result.json().then((res) => {
          console.log(res);

          if (res.success) {
            console.log("Got the success response");

            toast.success(res.responseMessage, {
              position: "top-center",
              autoClose: 1000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
              progress: undefined,
            });

            setTimeout(() => {
              window.location.reload(true);
            }, 1000); // Redirect after 3 seconds
          } else {
            console.log("Didn't got success response");
            toast.error("It seems server is down", {
              position: "top-center",
              autoClose: 1000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
              progress: undefined,
            });
            setTimeout(() => {
              window.location.reload(true);
            }, 1000); // Redirect after 3 seconds
          }
        });
      })
      .catch((error) => {
        console.error(error);
        toast.error("It seems server is down", {
          position: "top-center",
          autoClose: 1000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
        });
        setTimeout(() => {
          window.location.reload(true);
        }, 1000); // Redirect after 3 seconds
      });
  };

  const lockAccount = (accountId) => {
    updateBankAccountStatusRequest.accountId = accountId;
    updateBankAccountStatusRequest.status = "Lock";

    fetch("http://localhost:8080/api/bank/account/update/status", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(updateBankAccountStatusRequest),
    })
      .then((result) => {
        console.log("result", result);
        result.json().then((res) => {
          console.log(res);

          if (res.success) {
            console.log("Got the success response");

            toast.success(res.responseMessage, {
              position: "top-center",
              autoClose: 1000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
              progress: undefined,
            });

            setTimeout(() => {
              window.location.reload(true);
            }, 1000); // Redirect after 3 seconds
          } else {
            console.log("Didn't got success response");
            toast.error("It seems server is down", {
              position: "top-center",
              autoClose: 1000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
              progress: undefined,
            });
            setTimeout(() => {
              window.location.reload(true);
            }, 1000); // Redirect after 3 seconds
          }
        });
      })
      .catch((error) => {
        console.error(error);
        toast.error("It seems server is down", {
          position: "top-center",
          autoClose: 1000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
        });
        setTimeout(() => {
          window.location.reload(true);
        }, 1000); // Redirect after 3 seconds
      });
  };

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
            <h2>All Branch Accounts</h2>
          </div>
          <div
            className="card-body"
            style={{
              overflowY: "auto",
            }}
          >
            <div className="row">
              <div className="col">
                <form class="row g-3 align-items-center">
                  <div class="col-auto">
                    <label>
                      <b>Account Number</b>
                    </label>
                    <input
                      type="number"
                      class="form-control"
                      placeholder="Enter account no..."
                      onChange={(e) => setTempAccountNumber(e.target.value)}
                      value={tempAccountNumber}
                      required
                    />
                  </div>

                  <div class="col-auto">
                    <button
                      type="submit"
                      class="btn bg-color custom-bg-text btn-lg"
                      onClick={searchBankAccountsByAccountNumber}
                    >
                      Search
                    </button>
                  </div>
                </form>
              </div>
            </div>
            <div className="table-responsive mt-2">
              <table className="table table-hover text-color text-center">
                <thead className="table-bordered border-color bg-color custom-bg-text">
                  <tr>
                    <th scope="col">Customer Name</th>
                    <th scope="col">Branch Name</th>
                    <th scope="col">Account No.</th>
                    <th scope="col">Ifsc Code</th>
                    <th scope="col">Account Type</th>
                    <th scope="col">Complete Detail</th>
                    <th scope="col">Status</th>
                    <th scope="col">Statement</th>
                  </tr>
                </thead>
                <tbody>
                  {allAccounts.map((account) => {
                    return (
                      <tr>
                        <td>
                          <b>{account.user.name}</b>
                        </td>
                        <td>
                          <b>{account.bank.name}</b>
                        </td>
                        <td>
                          <b>{account.number}</b>
                        </td>
                        <td>
                          <b>{account.ifscCode}</b>
                        </td>
                        <td>
                          <b>{account.type}</b>
                        </td>

                        <td>
                          <button
                            onClick={() => viewAccountDetails(account.user)}
                            className="btn btn-sm bg-color custom-bg-text"
                          >
                            View Detail
                          </button>
                          <ToastContainer />
                        </td>
                        <td>
                          <b>{account.status}</b>
                        </td>

                        <td>
                          <button
                            onClick={() => viewAccountStatement(account.user)}
                            className="btn btn-sm bg-color custom-bg-text"
                          >
                            View
                          </button>
                          <ToastContainer />
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

export default ViewAllBankAccounts;
