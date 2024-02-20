import AdminHeader from "./AdminHeader";
import NormalHeader from "./NormalHeader";
import CustomerHeader from "./CustomerHeader";
import BankHeader from "./BankHeader";

const RoleNav = () => {
  const customer = JSON.parse(sessionStorage.getItem("active-customer"));
  const admin = JSON.parse(sessionStorage.getItem("active-admin"));
  const bank = JSON.parse(sessionStorage.getItem("active-bank"));

  if (admin != null) {
    return <AdminHeader />;
  } else if (customer != null) {
    return <CustomerHeader />;
  } else if (bank != null) {
    return <BankHeader />;
  } else {
    return <NormalHeader />;
  }
};

export default RoleNav;
