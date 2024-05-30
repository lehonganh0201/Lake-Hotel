import React, { useState } from "react";
import { cancelBooking, getBookingByConfirmationCode } from "../utils/ApiFunctions";

const FindBooking = () => {
  const [confirmationCode, setConfirmationCode] = useState("");
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [isDeleted, setIsDeleted] = useState(false); 
  const [successMessage, setSuccessMessage] = useState("");
  const [bookingInfo, setBookingInfo] = useState({
    bookingId: "",
    room: {
      id: "",
      roomType:""
    },
    bookingConfirmationCode: "",
    roomNumber: "",
    checkInDate: "",
    checkOutDate: "",
    guestFullName: "",
    guestEmail: "",
    numOfAdults: "",
    numOfChildren: "",
    totalNumOfGuest: "",
  });

  const clearBookingInfo = {
    bookingId: "",
    room: {
      id: "",
    },
    bookingConfirmationCode: "",
    roomNumber: "",
    checkInDate: "",
    checkOutDate: "",
    guestFullName: "",
    guestEmail: "",
    numOfAdults: "",
    numOfChildren: "",
    totalNumOfGuest: "",
  };

  const handleInputChange = (e) =>{
    setError("");
    setConfirmationCode(e.target.value);
  }

  const handleFormSubmit = async (e) =>{
    e.preventDefault();
    setIsLoading(true);

    try {
        const result = await getBookingByConfirmationCode(confirmationCode);
        setBookingInfo(result);

    } catch (error) {
        setBookingInfo(clearBookingInfo);
        setError(error.message);
    } 

    setTimeout(()=>{
        setIsLoading(false);
    },2000);
  }

  const handleBookingCancellation = async (bookingId) =>{
    try {
        await cancelBooking(bookingId);
        setIsDeleted(true);
        setSuccessMessage("Booking has been cancelled successfully!");
        setBookingInfo(clearBookingInfo);
        setConfirmationCode("");
        setError("");
    } catch (error) {
        setError(error.message);
    }

    setTimeout(()=>{
        setSuccessMessage("");
        setIsDeleted(false);
    },3000);
  }




  return (
  <>
    <div className="container mt-5 d-flex flex-column justify-content-center align-items-center">
        <h2>Find My Booking</h2>
        <form onSubmit={handleFormSubmit} className="col-md-6">
            <div className="input-group mb-3">
                <input className="form-control" id="confirmationCode" name="confirmationCode" 
                    value={confirmationCode} onChange={handleInputChange} placeholder="Enter the booking confirmation code"
                />

                <button className="btn btn-hotel input-group-text">Find booking</button>
            </div>
        </form>

        {
            isLoading ? (
                <div>
                    <span className="loader"></span>
                </div>
            ) : error ? (
                <div className="text-danger">
                    {error}
                </div>
            ) : bookingInfo.bookingConfirmationCode ? (
                <div className="col-md-6 mt-4 mb-5">
                    <h3>Booking Information</h3>
                    <p>Booking Confirmation Code : {bookingInfo.bookingConfirmationCode}</p>
                    <p>Booking ID : {bookingInfo.bookingId}</p>
                    <p>Room Number : {bookingInfo.room.id}</p>
                    <p>Room Type : {bookingInfo.room.roomType}</p>
                    <p>Check-In Date : {bookingInfo.checkInDate}</p>
                    <p>Check-Out Date : {bookingInfo.checkOutDate}</p>
                    <p>Full Name : {bookingInfo.guestFullName}</p>
                    <p>Email Address : {bookingInfo.guestEmail}</p>
                    <p>Adults : {bookingInfo.numOfAdults}</p>
                    <p>Children : {bookingInfo.numOfChildren}</p>
                    <p>Total Guest : {bookingInfo.totalNumOfGuest}</p>

                    {!isDeleted && (
                        <button className="btn btn-danger" onClick={() => handleBookingCancellation(bookingInfo.bookingId)}>Cancel Booking</button>
                    )}
                </div>
            ) : (
                <div>
                    Find Booking ...
                </div>
            )
        }

        {isDeleted && (
            <div className="alert alert-success mt-3" role="alert">
                {successMessage}
            </div>
        )}
    </div>

  </>
  );
};

export default FindBooking;
