import { Route, Router, Routes } from "react-router-dom";
import "./App.css";
import AddRoom from "./components/Room/AddRoom";
import ExistingRooms from "./components/Room/ExistingRooms";
import Home from "./components/home/Home";
import EditRoom from "./components/Room/EditRoom";
import NavBar from "./components/layout/NavBar/NavBar";
import Footer from './components/layout/Footer/Footer'
import RoomListing from "./components/Room/RoomListing";
import Admin from "./components/admin/Admin";
import Checkout from "./components/home/Checkout";
import BookingSuccess from "./components/bookings/BookingSuccess";
import Bookings from "./components/bookings/Bookings";
import FindBooking from "./components/bookings/FindBooking";

function App() {
  return (
    <>
      <main>
          <NavBar/>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/edit-room/:roomId" element={<EditRoom />} />
            <Route path="/existing-rooms" element={<ExistingRooms />} />
            <Route path="/add-room" element={<AddRoom />} />
            <Route path="/book-room/:roomId" element={<Checkout />} />
            <Route path="/browse-all-rooms" element={<RoomListing />} />
            <Route path="/admin" element={<Admin/>}/>
            <Route path="/booking-success" element={<BookingSuccess />} />
            <Route path="/existing-bookings" element={<Bookings/>}/>
            <Route path="/find-booking" element={<FindBooking/>}/>
          </Routes>
        <Footer/>
      </main>
    </>
  );
}

export default App;
