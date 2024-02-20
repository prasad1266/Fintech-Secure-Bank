import { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import { useLocation } from "react-router-dom";

const AddBankAccount = () => {
  const location = useLocation();
  const customer = location.state;

  const bank_jwtToken = sessionStorage.getItem("bank-jwtToken");

  const bank = JSON.parse(sessionStorage.getItem("active-bank"));

  let navigate = useNavigate();

  const [bankAccount, setBankAccount] = useState({
    name: "",
    ifscCode: "",
    type: "",
    bankId: bank.bank.id,
    userId: customer.id,
  });

  const handleInput = (e) => {
    setBankAccount({ ...bankAccount, [e.target.name]: e.target.value });
  };

  const saveAccount = (e) => {
    fetch("http://localhost:8080/api/bank/account/add", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: "Bearer " + bank_jwtToken,
      },
      body: JSON.stringify(bankAccount),
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
              navigate("/customer/bank/account/detail", { state: customer });
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
            <h5 className="card-title">Add Branch</h5>
          </div>
          <div className="card-body text-color">
            <form className="row g-3">
              <div className="col-md-6 mb-3">
                <label htmlFor="name" className="form-label">
                  <b>Branch Name</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  value={bank.bank.name}
                  readOnly
                />
              </div>

              <div className="col-md-6 mb-3">
                <label htmlFor="code" className="form-label">
                  <b>Branch Code</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  value={bank.bank.code}
                  readOnly
                />
              </div>

              <div className="col-md-6 mb-3">
                <label htmlFor="website" className="form-label">
                  <b>Customer Name</b>
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
                  <b>Customer Email</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  value={customer.email}
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
                <label htmlFor="quantity" className="form-label">
                  <b>Account Number</b>
                </label>
                <input
                  type="number"
                  className="form-control"
                  id="number"
                  name="number"
                  onChange={handleInput}
                  value={bankAccount.contact}
                />
              </div>

              <div className="col-md-6 mb-3">
                <label htmlFor="quantity" className="form-label">
                  <b>IFSC Code</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="ifscCode"
                  name="ifscCode"
                  onChange={handleInput}
                  value={bankAccount.ifscCode}
                />
              </div>

              <div class="col-md-6 mb-3">
                <label for="role" class="form-label">
                  <b>Account Type</b>
                </label>
                <select
                  onChange={handleInput}
                  className="form-control"
                  name="type"
                >
                  <option value="0">Select Account Type</option>

                  <option value="Saving"> Saving </option>
                  <option value="Current"> Current </option>
                </select>
              </div>

              <div className="d-flex aligns-items-center justify-content-center">
                <button
                  type="submit"
                  className="btn bg-color custom-bg-text col-md-4"
                  onClick={saveAccount}
                >
                  Add Account
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

export default AddBankAccount;
