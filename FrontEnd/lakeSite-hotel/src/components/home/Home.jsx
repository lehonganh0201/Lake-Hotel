import React from 'react'
import HeaderMain from '../layout/HeaderMain/HeaderMain'
import HotelServices from '../common/HotelServices'
import Parallax from '../common/Parallax'
import RoomCarousel from '../common/RoomCarousel'
import RoomSearch from '../common/RoomSearch'
import { useLocation } from 'react-router-dom'
const Home = () => {
  const location = useLocation();
  const message = location.state && location.state.message
  const currentUser = localStorage.getItem("userId");
  return (
    <section>
      {message && <p className='text-warning px-5'>{message}</p>}
      {currentUser && <h6 className='text-success text-center'>You are logged-In as {currentUser}</h6>}
        <HeaderMain/>
        <section className='container'>
          <RoomSearch/>
          <RoomCarousel/>
          <Parallax/>
          <RoomCarousel/>
          <HotelServices/>
          <Parallax/>
          <RoomCarousel/>
        </section>
    </section>
  )
}

export default Home
