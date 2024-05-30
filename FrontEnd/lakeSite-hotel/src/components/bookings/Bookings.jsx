import React, { useEffect, useState } from 'react'
import { cancelBooking, getAllBookings } from '../utils/ApiFunctions';
import Header from '../common/Header';
import BookingsTable from './BookingsTable';

const Bookings = () => {
    const [bookingInfo, setBookingInfo] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState("");
    
    useEffect(() => {
        setTimeout(() =>{
            getAllBookings().then((bookings) => {
                setBookingInfo(bookings);
                setIsLoading(false);
            }).catch((error)=>{
                setError(error);
                setIsLoading(false);
            })
        },1000)
    }, []);

    const handleBookingCancellation = async (bookingId) => {
        try {
            const response = await cancelBooking(bookingId);
            const data = await getAllBookings();
            setBookingInfo(data);
        } catch (error) {
            setError(error.message);
        }
    }

  return (
    <section style={{backgroundColor:'whitesmoke'}}>
      <Header title={"Existing Bookings"}/>
        {error && (
            <div className='text-danger'>{error}</div>
        )}
        {isLoading ? (
            <div>
                <span className="loader"></span>
            </div>
        ) :(
            <BookingsTable bookingInfo={bookingInfo} 
            handleBookingCancellation={handleBookingCancellation}/>
        )}
    </section>
  )
}

export default Bookings
