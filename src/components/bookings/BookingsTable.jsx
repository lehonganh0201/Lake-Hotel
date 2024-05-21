import { parseISO } from 'date-fns';
import React, { useEffect, useState } from 'react'
import DateSlider from '../common/DateSlider';

const BookingsTable = ({bookingInfo, handleBookingCancellation}) => {
    const [filteredBookings, setFilteredBookings] = useState(bookingInfo);

    const filterBookings = (startDate, endDate) =>{
        let filtered = bookingInfo;
        if(startDate && endDate){
            filtered = bookingInfo.filter((booking)=>{
                const bookingStartDate = parseISO(booking.checkInDate);
                const bookingEndDate = parseISO(booking.checkOutDate);
                return bookingStartDate >= startDate && bookingEndDate <= endDate && bookingEndDate > startDate;
            })
        }
        setFilteredBookings(filtered);
    }

    useEffect(() => {
        setFilteredBookings(bookingInfo);
    }, [bookingInfo]);


  return (
    <section className='p-4'>
        <DateSlider onDateChange={filterBookings} 
            onFilterChange={filterBookings}
        />
        <table className='table table-hover table-bordered shadow text-center'>
            <thead>
                <tr>
                    <th>S/N</th>
                    <th>Booking Id</th>
                    <th>Room Id</th>
                    <th>Room Type</th>
                    <th>Check-In Date</th>
                    <th>Check-Out Date</th>
                    <th>Guest Name</th>
                    <th>Guest Email</th>
                    <th>Adults</th>
                    <th>Children</th>
                    <th>Total Guest</th>
                    <th>Confirmation Code</th>
                    <th colSpan={2}>Actions</th>
                </tr>
            </thead>

            <tbody className='text-center'>
                {filteredBookings.map((booking, index) => (
                    <tr key={booking.bookingId}>
                        <th>{index+1}</th>
                        <th>{booking.bookingId}</th>
                        <th>{booking.room.id}</th>
                        <th>{booking.room.roomType}</th>
                        <th>{booking.checkInDate}</th>
                        <th>{booking.checkOutDate}</th>
                        <th>{booking.guestFullName}</th>
                        <th>{booking.guestEmail}</th>
                        <th>{booking.numOfAdults}</th>
                        <th>{booking.numOfChildren}</th>
                        <th>{booking.totalNumOfGuest}</th>
                        <th>{booking.bookingConfirmationCode}</th>
                        <td>
                            <button className='btn btn-danger btn-sm'
                            onClick={() => handleBookingCancellation(booking.id)}>
                                Cancel
                            </button>
                        </td>
                    </tr>
                ))}
            </tbody>
        </table>
        {filteredBookings.length === 0 && (
            <p>No booking found for the selected dates</p>
        )}
    </section>
  )
}

export default BookingsTable
