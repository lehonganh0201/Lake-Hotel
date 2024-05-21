import React from 'react'

const RoomPaginator = ({currentPage, totalPages, onPageChange}) => {
    const pageNumbers = Array.from({length: totalPages}, (_ , i) =>{
        return i+1
    });


  return (
    <nav>
      <ul className='pagenation justify-content-center d-flex'>
        {pageNumbers.map((pageNumber) =>(
            <li key={pageNumber}
            className={`page-item ${currentPage === pageNumber ? "active" : ""}`}>
                <button className='btn btn-primary mx-1' onClick={() => {
                    onPageChange(pageNumber);
                }}>
                    {pageNumber}
                </button>
            </li>
        ))}
      </ul>
    </nav>
  )
}

export default RoomPaginator
