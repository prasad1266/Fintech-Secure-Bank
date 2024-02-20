import { useState, useEffect } from "react";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const ViewAllBankTransactions = () => {
  let navigate = useNavigate();
  const [allTransactions, setAllTransactions] = useState([]);

  const admin_jwtToken = sessionStorage.getItem("admin-jwtToken");

  const retrieveAllTransactions = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/bank/transaction/all",
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
    const getAllTransactions = async () => {
      const transactions = await retrieveAllTransactions();
      if (transactions) {
        setAllTransactions(transactions.bankTransactions);
      }
    };

    getAllTransactions();
  }, []);

  const convertToEpochTime = (dateString) => {
    const selectedDate = new Date(dateString);
    const epochTime = selectedDate.getTime();
    return epochTime;
  };

  const formatDateFromEpoch = (epochTime) => {
    const date = new Date(Number(epochTime));
    const formattedDate = date.toLocaleString(); // Adjust the format as needed

    return formattedDate;
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
            <h2>All Customer Transactions</h2>
          </div>
          <div
            className="card-body"
            style={{
              overflowY: "auto",
            }}
          >
            <div className="table-responsive mt-3">
              <table className="table table-hover text-color text-center">
                <thead className="table-bordered border-color bg-color custom-bg-text">
                  <tr>
                    <th scope="col">Transaction Id</th>
                    <th scope="col">Source Branch</th>
                    <th scope="col">Customer Name</th>
                    <th scope="col">Source Account</th>
                    <th scope="col">Transaction Type</th>
                    <th scope="col">Amount</th>
                    <th scope="col">Reciepent Branch</th>
                    <th scope="col">Reciepent Account</th>
                    <th scope="col">Narration</th>
                    <th scope="col">Transaction Time</th>
                  </tr>
                </thead>
                <tbody>
                  {allTransactions.map((transaction) => {
                    return (
                      <tr>
                        <td>
                          <b>{transaction.transactionId}</b>
                        </td>
                        <td>
                          <b>{transaction.bank.name}</b>
                        </td>
                        <td>
                          <b>{transaction.user.name}</b>
                        </td>
                        <td>
                          <b>{transaction.bankAccount.number}</b>
                        </td>
                        <td>
                          <b>{transaction.type}</b>
                        </td>
                        <td>
                          <b>{transaction.amount}</b>
                        </td>
                        <td>
                          {(() => {
                            if (transaction.type === "Account Transfer") {
                              return (
                                <b>
                                  {transaction.destinationBankAccount.bank.name}
                                </b>
                              );
                            } else {
                              return <b>---</b>;
                            }
                          })()}
                        </td>
                        <td>
                          {(() => {
                            if (transaction.type === "Account Transfer") {
                              return (
                                <b>
                                  {transaction.destinationBankAccount.number}
                                </b>
                              );
                            } else {
                              return <b>---</b>;
                            }
                          })()}
                        </td>
                        <td>
                          <b>{transaction.narration}</b>
                        </td>
                        <td>
                          <b>
                            {formatDateFromEpoch(transaction.transactionTime)}
                          </b>
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

export default ViewAllBankTransactions;
