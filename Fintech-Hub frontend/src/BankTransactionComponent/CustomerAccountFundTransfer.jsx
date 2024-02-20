import { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import { useLocation } from "react-router-dom";

const CustomerAccountFundTransfer = () => {
  const customer = JSON.parse(sessionStorage.getItem("active-customer"));

  let customerToken = sessionStorage.getItem("customer-jwtToken");
  let navigate = useNavigate();

  const [transferRequest, setTransferRequest] = useState({
    userId: customer.id,
    bankId: customer.bank.id,
    amount: "",
    toBankAccount: "",
    toBankIfsc: "",
    accountTransferPurpose: "",
  });

  const handleInput = (e) => {
    setTransferRequest({ ...transferRequest, [e.target.name]: e.target.value });
  };

  const saveAccount = (e) => {
    fetch("http://localhost:8080/api/bank/transaction/account/transfer", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: "Bearer " + customerToken,
      },
      body: JSON.stringify(transferRequest),
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
              navigate("/customer/bank/account/statement", { state: customer });
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
      <div className="mt-2 d-flex aligns-items-center justify-content-center">
        <div
          className="card form-card border-color custom-bg"
          style={{ width: "50rem" }}
        >
          <div className="card-header bg-color custom-bg-text text-center">
            <h5 className="card-title">Transfer Money</h5>
          </div>
          <div className="card-body text-color">
            <form className="row g-3">
              <div className="col-md-6 mb-3">
                <label htmlFor="quantity" className="form-label">
                  <b>Account Number</b>
                </label>
                <input
                  type="number"
                  className="form-control"
                  id="toBankAccount"
                  name="toBankAccount"
                  onChange={handleInput}
                  value={transferRequest.toBankAccount}
                />
              </div>

              <div className="col-md-6 mb-3">
                <label htmlFor="toBankIfsc" className="form-label">
                  <b>IFSC Code</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="toBankIfsc"
                  name="toBankIfsc"
                  onChange={handleInput}
                  value={transferRequest.toBankIfsc}
                />
              </div>

              <div className="col-md-6 mb-3">
                <label htmlFor="amount" className="form-label">
                  <b>Amount</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="amount"
                  name="amount"
                  onChange={handleInput}
                  value={transferRequest.amount}
                />
              </div>

              <div className="col-md-6 mb-3">
                <label htmlFor="description" className="form-label">
                  <b>Purpose</b>
                </label>
                <textarea
                  className="form-control"
                  id="accountTransferPurpose"
                  name="accountTransferPurpose"
                  rows="3"
                  onChange={handleInput}
                  value={transferRequest.accountTransferPurpose}
                  placeholder="reason for transfer..."
                />
              </div>

              <div className="d-flex aligns-items-center justify-content-center">
                <button
                  type="submit"
                  className="btn bg-color custom-bg-text col-md-4"
                  onClick={saveAccount}
                >
                  Transfer
                </button>
                <ToastContainer />
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CustomerAccountFundTransfer;
