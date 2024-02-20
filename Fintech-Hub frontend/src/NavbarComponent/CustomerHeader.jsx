import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";

const CustomerHeader = () => {
  let navigate = useNavigate();

  const customer = JSON.parse(sessionStorage.getItem("active-customer"));

  const userLogout = () => {
    toast.success("logged out!!!", {
      position: "top-center",
      autoClose: 2000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
    });
    sessionStorage.removeItem("active-customer");
    sessionStorage.removeItem("customer-jwtToken");

    setTimeout(() => {
      navigate("/home");
    }, 1000); // Redirect after 3 seconds

    window.location.reload(true);
  };

  const handleTransactionHistoryClick = () => {
    navigate("/customer/bank/account/statement", { state: customer });
  };

  const viewBankAccount = () => {
    if (customer.isAccountLinked === "Yes") {
      navigate("/customer/bank/account/detail", { state: customer });
    } else {
      toast.error("Bank Account Not Linked, Contact Bank Administrator!!!!", {
        position: "top-center",
        autoClose: 1000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
    }
  };

  const moneyTransfer = () => {
    if (customer.isAccountLinked === "Yes") {
      navigate("/customer/account/transfer");
    } else {
      toast.error("Bank Account Not Linked, Contact Bank Administrator!!!!", {
        position: "top-center",
        autoClose: 1000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
    }
  };

  return (
    <ul class="navbar-nav ms-auto mb-2 mb-lg-0 me-5">
      <li class="nav-item">
        <div class="nav-link active" aria-current="page">
          <b className="text-color" onClick={moneyTransfer}>
            Money Transfer
          </b>
          <ToastContainer />
        </div>
      </li>

      <li class="nav-item">
        <div class="nav-link active" aria-current="page">
          <b className="text-color" onClick={viewBankAccount}>
          Branch Account
          </b>
          <ToastContainer />
        </div>
      </li>

      <li class="nav-item">
        <div class="nav-link active" aria-current="page">
          <b className="text-color" onClick={handleTransactionHistoryClick}>
            Transaction History
          </b>
        </div>
      </li>

      <li class="nav-item">
        <Link
          to=""
          class="nav-link active"
          aria-current="page"
          onClick={userLogout}
        >
          <b className="text-color">Logout</b>
        </Link>
        <ToastContainer />
      </li>
    </ul>
  );
};

export default CustomerHeader;
