import React from 'react'
import HeaderMain from '../layout/HeaderMain/HeaderMain'
import HotelServices from '../common/HotelServices'
import Parallax from '../common/Parallax'
import RoomCarousel from '../common/RoomCarousel'
import RoomSearch from '../common/RoomSearch'
const Home = () => {
  return (
    <section>
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
