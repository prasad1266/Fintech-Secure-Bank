import { Link, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const BankHeader = () => {
  let navigate = useNavigate();

  const user = JSON.parse(sessionStorage.getItem("active-bank"));
  console.log(user);

  const bankLogout = () => {
    toast.success("logged out!!!", {
      position: "top-center",
      autoClose: 1000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
    });
    sessionStorage.removeItem("active-bank");
    sessionStorage.removeItem("bank-jwtToken");
    window.location.reload(true);
    setTimeout(() => {
      navigate("/home");
    }, 2000); // Redirect after 3 seconds
  };

  return (
    <ul class="navbar-nav ms-auto mb-2 mb-lg-0 me-5">
      <li className="nav-item">
        <Link
          to="/user/customer/register"
          className="nav-link active"
          aria-current="page"
        >
          <b className="text-color">Register Customer</b>
        </Link>
      </li>

      <li className="nav-item">
        <Link
          to="/bank/account/all"
          className="nav-link active"
          aria-current="page"
        >
          <b className="text-color">Branch Accounts </b>
        </Link>
      </li>

      <li className="nav-item">
        <Link
          to="/bank/customer/all"
          className="nav-link active"
          aria-current="page"
        >
          <b className="text-color">Branch Customers</b>
        </Link>
      </li>

      <li className="nav-item">
        <Link
          to="/bank/customer/account/transactions"
          className="nav-link active"
          aria-current="page"
        >
          <b className="text-color">Customer Transactions</b>
        </Link>
      </li>

      <li class="nav-item">
        <Link
          to=""
          class="nav-link active"
          aria-current="page"
          onClick={bankLogout}
        >
          <b className="text-color">Logout</b>
        </Link>
        <ToastContainer />
      </li>
    </ul>
  );
};

export default BankHeader;
