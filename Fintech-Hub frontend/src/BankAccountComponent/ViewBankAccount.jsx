import { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import { useLocation } from "react-router-dom";

const ViewBankAccount = () => {
  const location = useLocation();
  const customer = location.state;
  const [bankAccount, setBankAccount] = useState({});

  const [amountToDeposit, setAmountToDeposit] = useState("");
  const [amountToWithdraw, setAmountToWithdraw] = useState("");
  const bank = JSON.parse(sessionStorage.getItem("active-bank"));

  const [statementDownloadRequest, setStatementDownloadRequest] = useState({
    startDate: "",
    endDate: "",
    accountId: "",
  });

  let jwtToken;

  let adminToken = sessionStorage.getItem("admin-jwtToken");
  let bankToken = sessionStorage.getItem("bank-jwtToken");
  let customerToken = sessionStorage.getItem("customer-jwtToken");

  if (adminToken) {
    jwtToken = adminToken;
  } else if (bankToken) {
    jwtToken = bankToken;
  } else if (customerToken) {
    jwtToken = customerToken;
  }

  const handleUserInput = (e) => {
    setStatementDownloadRequest({
      ...statementDownloadRequest,
      [e.target.name]: e.target.value,
    });
  };

  let navigate = useNavigate();

  const retrieveBankAccount = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/bank/account/fetch/user?userId=" + customer.id,
      {
        headers: {
          Authorization: "Bearer " + jwtToken, // Replace with your actual JWT token
        },
      }
    );
    return response.data;
  };

  const formatDateFromEpoch = (epochTime) => {
    const date = new Date(Number(epochTime));
    const formattedDate = date.toLocaleString(); // Adjust the format as needed

    return formattedDate;
  };

  useEffect(() => {
    const getBankAccount = async () => {
      const bankAccounts = await retrieveBankAccount();
      if (bankAccounts) {
        setBankAccount(bankAccounts.accounts[0]);
      }
    };

    getBankAccount();
  }, [customer]);

  const convertToEpochTime = (dateString) => {
    const selectedDate = new Date(dateString);
    const epochTime = selectedDate.getTime();
    return epochTime;
  };

  const downloadStatement = (e) => {
    e.preventDefault();

    fetch(
      "http://localhost:8080/api/bank/transaction/statement/download?accountId=" +
        bankAccount.id +
        "&startTime=" +
        convertToEpochTime(statementDownloadRequest.startDate) +
        "&endTime=" +
        convertToEpochTime(statementDownloadRequest.endDate),
      {
        method: "GET",
        headers: {
          Authorization: "Bearer " + jwtToken,
        },
      }
    )
      .then((response) => response.blob())
      .then((blob) => {
        // Create a temporary URL for the blob
        const url = window.URL.createObjectURL(blob);

        // Create a temporary <a> element to trigger the download
        const link = document.createElement("a");
        link.href = url;
        link.download = "bank_statement.pdf"; // Specify the desired filename here

        // Append the link to the document and trigger the download
        document.body.appendChild(link);
        link.click();

        // Clean up the temporary URL and link
        URL.revokeObjectURL(url);
        document.body.removeChild(link);
      })
      .catch((error) => {
        console.error("Download error:", error);
      });
  };

  const depositAmount = (e) => {
    console.log("Amount :" + amountToDeposit);

    fetch("http://localhost:8080/api/bank/transaction/deposit", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: "Bearer " + jwtToken,
      },
      body: JSON.stringify({
        sourceBankAccountId: bankAccount.id,
        amount: amountToDeposit,
      }),
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
    e.preventDefault();
  };

  const withdrawAmount = (e) => {
    console.log("Amount to withdraw :" + amountToWithdraw);
    fetch("http://localhost:8080/api/bank/transaction/withdraw", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: "Bearer " + jwtToken,
      },
      body: JSON.stringify({
        sourceBankAccountId: bankAccount.id,
        amount: amountToWithdraw,
      }),
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
    e.preventDefault();
  };

  return (
    <div>
      <div className="row ms-2 mt-2">
        <div className="col">
          <form class="row g-3 align-items-center">
            <div class="col-auto">
              <label>
                <b>Select Start Date</b>
              </label>
              <input
                type="datetime-local"
                class="form-control"
                name="startDate"
                placeholder="Start Date..."
                onChange={handleUserInput}
                value={statementDownloadRequest.startDate}
                required
              />
            </div>
            <div class="col-auto">
              <label>
                <b>Select End Date</b>
              </label>
              <input
                type="datetime-local"
                class="form-control"
                name="endDate"
                placeholder="Start Date..."
                onChange={handleUserInput}
                value={statementDownloadRequest.endDate}
                required
              />
            </div>

            <div class="col-auto">
              <button
                type="submit"
                class="btn bg-color custom-bg-text btn-lg"
                onClick={downloadStatement}
              >
                Download Statement
              </button>
              <ToastContainer />
            </div>
          </form>
        </div>
      </div>

      <div className="row mt-2">
        <div className="col-md-8 mt-4 ms-3">
          <div className="card form-card border-color custom-bg">
            <div className="card-header bg-color custom-bg-text text-center">
              <h5 className="card-title">Customer Branch Account Detail</h5>
            </div>
            <div className="card-body text-color">
              <div className="row g-3">
                <div className="col-md-6 mb-3">
                  <label htmlFor="name" className="form-label">
                    <b>Branch</b>
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    value={customer.bank.name}
                    readOnly
                  />
                </div>

                <div className="col-md-6 mb-3">
                  <label htmlFor="name" className="form-label">
                    <b>Branch Account No.</b>
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    value={bankAccount.number}
                    readOnly
                  />
                </div>

                <div className="col-md-6 mb-3">
                  <label htmlFor="code" className="form-label">
                    <b>Ifsc Code</b>
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    value={bankAccount.ifscCode}
                    readOnly
                  />
                </div>

                <div className="col-md-6 mb-3">
                  <label htmlFor="website" className="form-label">
                    <b>Customer</b>
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    value={customer.name}
                    readOnly
                  />
                </div>

                <div className="col-md-6 mb-3">
                  <label htmlFor="website" className="form-label">
                    <b>Customer Contact</b>
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    value={customer.contact}
                    readOnly
                  />
                </div>

                <div className="col-md-6 mb-3">
                  <label htmlFor="website" className="form-label">
                    <b>Creation Date</b>
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    value={formatDateFromEpoch(bankAccount.creationDate)}
                    readOnly
                  />
                </div>

                <div className="col-md-6 mb-3">
                  <label htmlFor="website" className="form-label">
                    <b>Available Balance</b>
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    value={bankAccount.balance}
                    readOnly
                  />
                </div>

                <div className="col-md-6 mb-3">
                  <label htmlFor="website" className="form-label">
                    <b>Account Status</b>
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    value={bankAccount.status}
                    readOnly
                  />
                </div>
              </div>
            </div>
          </div>
        </div>
        {/* ------- */}

        {(() => {
          if (bank !== null) {
            return (
              <div className="col mt-4">
                <div className="container">
                  <div className="card form-card border-color custom-bg">
                    <div className="card-header bg-color text-center custom-bg-text">
                      <h4 className="card-title">Branch Deposit</h4>
                    </div>
                    <div className="card-body">
                      <form>
                        <div className="mb-3 text-color">
                          <label for="emailId" class="form-label">
                            <b>Amount To Deposit</b>
                          </label>
                          <input
                            type="number"
                            className="form-control"
                            id="amount"
                            name="amount"
                            onChange={(e) => setAmountToDeposit(e.target.value)}
                            value={amountToDeposit}
                          />
                        </div>

                        <button
                          type="submit"
                          className="btn bg-color custom-bg-text"
                          onClick={depositAmount}
                        >
                          Deposit
                        </button>
                        <ToastContainer />
                      </form>
                    </div>
                  </div>

                  <div className="card form-card border-color custom-bg mt-4">
                    <div className="card-header bg-color text-center custom-bg-text">
                      <h4 className="card-title">Branch Withdraw</h4>
                    </div>
                    <div className="card-body">
                      <form>
                        <div className="mb-3 text-color">
                          <label for="emailId" class="form-label">
                            <b>Amount To Withdraw</b>
                          </label>
                          <input
                            type="number"
                            className="form-control"
                            id="amount"
                            name="amount"
                            onChange={(e) =>
                              setAmountToWithdraw(e.target.value)
                            }
                            value={amountToWithdraw}
                          />
                        </div>

                        <button
                          type="submit"
                          className="btn bg-color custom-bg-text"
                          onClick={withdrawAmount}
                        >
                          Withdraw
                        </button>
                        <ToastContainer />
                      </form>
                    </div>
                  </div>
                </div>
              </div>
            );
          }
        })()}

        {/* ------- */}
      </div>
    </div>
  );
};

export default ViewBankAccount;
