import { useState, useEffect } from "react";
import "react-toastify/dist/ReactToastify.css";
import { ToastContainer, toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

const UserRegister = () => {
  const navigate = useNavigate();

  const bank = JSON.parse(sessionStorage.getItem("active-bank"));

  const [user, setUser] = useState({
    name: "",
    email: "",
    password: "",
    contact: "",
    street: "",
    city: "",
    pincode: "",
    roles: "",
    age: "",
    gender: "",
    bankId: "",
  });

  const [isPasswordValid, setIsPasswordValid] = useState(true);

  useEffect(() => {
    if (document.URL.indexOf("customer") !== -1) {
      setUser((prevUser) => ({ ...prevUser, roles: "CUSTOMER" }));
    } else if (document.URL.indexOf("bank") !== -1) {
      setUser((prevUser) => ({ ...prevUser, roles: "BANK" }));
    }
  }, []);

  const handleUserInput = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };

  const validatePassword = () => {
    const regex = /^(?=.[a-z])(?=.[A-Z])(?=.\d)(?=.[@$!%?&])[A-Za-z\d@$!%?&]{8,}$/;
    setIsPasswordValid(regex.test(user.password));
  };

  const isNameValid = /^[a-zA-Z]+$/.test(user.name);
  const isContactValid = /^[6-9]\d{9}$/.test(user.contact); // Assuming India-based contact number
  const isAgeValid =
    /^[1-9]\d*$/.test(user.age) && user.age >= 18 && user.age <= 120 && user.age <= 2024 - 1900; // Not beyond 2024

  const isCityPincodeValid = user.city.trim() !== "" && /^[1-9]\d{5}$/.test(user.pincode); // Assuming 6-digit pincode

  const saveUser = (e) => {
    e.preventDefault();

    validatePassword();

    if (
      !isNameValid ||
      !isPasswordValid ||
      !isContactValid ||
      !isAgeValid ||
      !isCityPincodeValid
    ) {
      toast.error("Invalid input. Please check the requirements.", {
        position: "top-center",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
      return;
    }

    let jwtToken;

    if (user.roles === "CUSTOMER") {
      user.bankId = bank.bank.id;
      jwtToken = sessionStorage.getItem("bank-jwtToken");
    } else if (user.roles === "BANK") {
      jwtToken = sessionStorage.getItem("admin-jwtToken");
    }

    fetch("http://localhost:8080/api/user/register", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: "Bearer " + jwtToken,
      },
      body: JSON.stringify(user),
    })
      .then((result) => {
        result.json().then((res) => {
          if (res.success) {
            toast.success(res.responseMessage, {
              position: "top-center",
              autoClose: 1000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
              progress: undefined,
            });

            if (user.roles === "CUSTOMER") {
              setTimeout(() => {
                navigate("/bank/customer/all");
              }, 1000);
            } else if (user.roles === "BANK") {
              setTimeout(() => {
                navigate("/admin/bank/register");
              }, 1000);
            }
          } else {
            toast.error("It seems the server is down", {
              position: "top-center",
              autoClose: 1000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
              progress: undefined,
            });
          }
        });
      })
      .catch((error) => {
        console.error(error);
        toast.error("It seems the server is down", {
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
        }, 1000);
      });
    e.preventDefault();
  };

  return (
    <div>
      <div className="mt-2 d-flex aligns-items-center justify-content-center ms-2 me-2 mb-2">
        <div
          className="card form-card border-color text-color custom-bg"
          style={{ width: "50rem" }}
        >
          <div className="card-header bg-color custom-bg-text text-center">
            <h5 className="card-title">Register {user.roles}</h5>
          </div>
          <div className="card-body">
            <form className="row g-3" onSubmit={saveUser}>
              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="title" className="form-label">
                  <b>Name</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="name"
                  name="name"
                  onChange={handleUserInput}
                  value={user.name}
                  required
                />
                {!isNameValid && (
                  <p style={{ color: "red" }}>
                    Please enter a valid name with only characters.
                  </p>
                )}
              </div>

              <div className="col-md-6 mb-3 text-color">
                <b>
                  <label className="form-label">Email Id</label>
                </b>
                <input
                  type="email"
                  className="form-control"
                  id="email"
                  name="email"
                  onChange={handleUserInput}
                  value={user.email}
                  required
                />
              </div>
              <div className="col-md-6 mb-3">
                <label htmlFor="quantity" className="form-label">
                  <b>Password</b>
                </label>
                <input
                  type="password"
                  className="form-control"
                  id="password"
                  name="password"
                  onChange={handleUserInput}
                  onBlur={validatePassword}
                  value={user.password}
                  required
                />
                {!isPasswordValid && (
                  <p style={{ color: "red" }}>
                    Password must contain at least one capital letter, one
                    small letter, one digit, one special character, and be at
                    least 8 characters long.
                  </p>
                )}
              </div>
              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="sex" className="form-label">
                  <b>User Gender</b>
                </label>
                <select
                  onChange={handleUserInput}
                  className="form-control"
                  name="gender"
                  required
                >
                  <option value="0">Select Gender</option>
                  <option value="Male">Male</option>
                  <option value="Female">Female</option>
                </select>
              </div>

              <div className="col-md-6 mb-3">
                <label htmlFor="contact" className="form-label">
                  <b>Contact No</b>
                </label>
                <input
                  type="number"
                  className="form-control"
                  id="contact"
                  name="contact"
                  onChange={handleUserInput}
                  value={user.contact}
                  required
                />
                {!isContactValid && (
                  <p style={{ color: "red" }}>Please enter a valid 10-digit contact number.</p>
                )}
              </div>
              <div className="col-md-6 mb-3">
                <label htmlFor="contact" className="form-label">
                  <b>Age</b>
                </label>
                <input
                  type="number"
                  className="form-control"
                  id="age"
                  name="age"
                  onChange={handleUserInput}
                  value={user.age}
                  required
                />
                {!isAgeValid && (
                  <p style={{ color: "red" }}>Please enter a valid age between 18 and 120.</p>
                )}
              </div>

              <div className="col-md-6 mb-3">
                <label htmlFor="description" className="form-label">
                  <b> Street</b>
                </label>
                <textarea
                  className="form-control"
                  id="street"
                  name="street"
                  rows="3"
                  onChange={handleUserInput}
                  value={user.street}
                  required
                />
              </div>
              <div className="col-md-6 mb-3">
                <label htmlFor="price" className="form-label">
                  <b>City</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="city"
                  name="city"
                  onChange={handleUserInput}
                  value={user.city}
                  required
                />
                {!isCityPincodeValid && (
                  <p style={{ color: "red" }}>Please enter a valid city and pincode.</p>
                )}
              </div>
              <div className="col-md-6 mb-3">
                <label htmlFor="pincode" className="form-label">
                  <b>Pincode</b>
                </label>
                <input
                  type="number"
                  className="form-control"
                  id="pincode"
                  name="pincode"
                  onChange={handleUserInput}
                  value={user.pincode}
                  required
                />
                {!isCityPincodeValid && (
                  <p style={{ color: "red" }}>Please enter a valid city and pincode.</p>
                )}
              </div>

              <div className="d-flex aligns-items-center justify-content-center">
                <input
                  type="submit"
                  className={`btn bg-color custom-bg-text ${
                    !isNameValid || !isPasswordValid || !isContactValid || !isAgeValid || !isCityPincodeValid
                      ? "disabled"
                      : ""
                  }`}
                  value="Register User"
                  disabled={!isNameValid || !isPasswordValid || !isContactValid || !isAgeValid || !isCityPincodeValid}
                />
              </div>
              <ToastContainer />
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserRegister;