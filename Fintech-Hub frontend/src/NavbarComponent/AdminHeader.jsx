import { Link, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const AdminHeader = () => {
  let navigate = useNavigate();

  const user = JSON.parse(sessionStorage.getItem("active-admin"));
  console.log(user);

  const adminLogout = () => {
    toast.success("logged out!!!", {
      position: "top-center",
      autoClose: 1000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
    });
    sessionStorage.removeItem("active-admin");
    sessionStorage.removeItem("admin-jwtToken");
    window.location.reload(true);
    setTimeout(() => {
      navigate("/home");
    }, 2000); // Redirect after 3 seconds
  };

  return (
    <ul class="navbar-nav ms-auto mb-2 mb-lg-0 me-5">
      <li className="nav-item">
        <Link
          to="/user/bank/register"
          className="nav-link active"
          aria-current="page"
        >
          <b className="text-color">Register Branch Manager</b>
        </Link>
      </li>
      <li className="nav-item">
        <Link
          to="/admin/bank/register"
          className="nav-link active"
          aria-current="page"
        >
          <b className="text-color">Add Branch</b>
        </Link>
      </li>
      <li className="nav-item">
        <Link
          to="/admin/bank/all"
          className="nav-link active"
          aria-current="page"
        >
          <b className="text-color">View Branches</b>
        </Link>
      </li>
      <li className="nav-item">
        <Link
          to="/admin/bank/managers"
          className="nav-link active"
          aria-current="page"
        >
          <b className="text-color">Branch Managers</b>
        </Link>
      </li>

      {/* <li className="nav-item">
        <Link
          to="/admin/all/bank/customers"
          className="nav-link active"
          aria-current="page"
        >
          <b className="text-color">All Customers</b>
        </Link>
      </li> */}

      <li className="nav-item">
        <Link
          to="/admin/bank/account/all"
          className="nav-link active"
          aria-current="page"
        >
          <b className="text-color">Branch Accounts</b>
        </Link>
      </li>

      <li className="nav-item">
        <Link
          to="/admin/bank/customer/transaction/all"
          className="nav-link active"
          aria-current="page"
        >
          <b className="text-color">Branch Transactions</b>
        </Link>
      </li>

      <li class="nav-item">
        <Link
          to=""
          class="nav-link active"
          aria-current="page"
          onClick={adminLogout}
        >
          <b className="text-color">Logout</b>
        </Link>
        <ToastContainer />
      </li>
    </ul>
  );
};

export default AdminHeader;
