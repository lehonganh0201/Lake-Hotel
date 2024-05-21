import React, { useState } from 'react'

const RoomFilter = ({data, setFilteredData}) => {
    const [filter, setFilter] = useState("");
    const handleSelectChange = (e) =>{
        const selectedRoomType = e.target.value;
        setFilter(selectedRoomType);
        const filteredRooms = data.filter((room) =>(
            room.roomType.toLowerCase().includes(selectedRoomType.toLowerCase()))
        );
        setFilteredData(filteredRooms)
    }

    const clearFilter = () =>{
        setFilter("");
        setFilteredData(data);
    }

    const roomTypes = ["", ...new Set(data.map((room) => room.roomType))]

    return (
        <div className='input-group mb-3'>
            <span className='input-group-text' id='room-type-filter'>Filter Rooms</span>
            <select className='form-select' value={filter} onChange={handleSelectChange}>
                <option className='' value={""}>Select a room type</option>
                {roomTypes.map((type,index) => (
                    
                    <option value={type} key={index}>{type}</option>
                ))}
            </select>

            <button onClick={clearFilter} className='btn btn-hotel' type='button'>Clear</button>
        </div>
    )
}

export default RoomFilter
