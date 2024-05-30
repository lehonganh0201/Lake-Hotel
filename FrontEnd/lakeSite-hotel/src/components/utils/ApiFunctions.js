import axios from "axios";

export const api = axios.create({
  baseURL: "http://localhost:8080",
});

export const getHeaders = () => {
  const token = localStorage.getItem("token");
  return {
    Authorization : `Bearer ${token}`,
    "Content-Type" : "application/json" 
  };
}

/*This function add a new room to the database */
export async function addRoom(photo, roomType, roomPrice) {
  const formData = new FormData();
  formData.append("photo", photo);
  formData.append("roomType", roomType);
  formData.append("roomPrice", roomPrice);

  const response = await api.post("/rooms/add/new-room", formData);
  if (response.status === 201) {
    return true;
  } else {
    return false;
  }
}

/*This function gets all room types from the database */
export async function getRoomTypes() {
  try {
    const response = await api.get("/rooms/room/types");
    return response.data;
  } catch (error) {
    throw new Error("Error fetching room types");
  }
}

/*This function gets all rooms from the database */
export async function getAllRooms() {
  try {
    const result = await api.get("/rooms/all-rooms");
    return result.data;
  } catch (error) {
    throw new Error("Error fetching rooms");
  }
}

/*This function delete room by the Room ID */
export async function deleteRoom(roomId) {
  try {
    const result = await api.delete(`/rooms/delete/room/${roomId}`);
    return result.data;
  } catch (error) {
    throw new Error(`Error deleting room ${error.message}`);
  }
}

/*This function update a room by the ID */
export async function updateRoom(roomId, roomData) {
  const formData = new FormData();
  formData.append("roomType", roomData.roomType);
  formData.append("roomPrice", roomData.roomPrice);
  formData.append("photo", roomData.photo);

  const response = await api.put(`/rooms/update/${roomId}`, formData);
  return response;
}

/*This function get room by Room ID */
export async function getRoomById(roomId) {
  try {
    const result = await api.get(`/rooms/room/${roomId}`);
    return result.data;
  } catch (error) {
    throw new Error(`Error fetching room ${error.message}`);
  }
}

/*This function save a new booking to the database */
export async function bookRoom(roomId, booking) {
  try {
    const response = await api.post(
      `/bookings/room/${roomId}/booking`,
      booking
    );
    return response.data;
  } catch (error) {
    if (error.message && error.message.data) {
      throw new Error(error.response.data);
    } else {
      throw new Error(`Error booking room : ${error.message}`);
    }
  }
}

/*This function get all bookings from the database */
export async function getAllBookings() {
  try {
    const result = await api.get("/bookings/all-bookings");
    return result.data;
  } catch (error) {
    throw new Error(`Error fetching bookings : ${error.message}`);
  }
}

/*This function get booking from database with confirmation code */
export async function getBookingByConfirmationCode(confirmationCode) {
  try {
    const result = await api.get(`/bookings/confirmation/${confirmationCode}`);
    return result.data;
  } catch (error) {
    if (error.response && error.response.data) {
      throw new Error(error.response.data);
    } else {
      throw new Error(`Error find booking : ${error.message}`);
    }
  }
}

/*This function cancels booking */
export async function cancelBooking(bookingId) {
  try {
    const result = await api.delete(`/bookings/booking/${bookingId}/delete`);
    return result.data;
  } catch (error) {
    throw new Error(`Error cancelling booking : ${error.message}`);
  }
}

/*This function gets all available rooms from database with a given date and room type */
export async function getAvailableRooms(checkInDate, checkOutDate, roomType) {
    try {
        const result = await api.get(`/rooms/search-rooms?checkInDate=${checkInDate}&checkOutDate=${checkOutDate}&roomType=${roomType}`);
        return result;
      } catch (error) {
        console.error("Error fetching available rooms:", error);
        throw error;
      }      
}

/* This function register a new user */
export async function registerUser(registration){
  try {
    const response = await api.post(`/auth/register`, registration);
    return response.data;
  } catch (error) {
    if(error.response && error.response.data){
      throw new Error(error.response.data);
    }
    else{
      throw new Error(`User registration error : ${error.message}`)
    }
  }
}

/* This function login a registered user */
export async function loginUser(login){
  try {
    const response = await api.post(`/auth/login`, login);
    if(response.status >= 200 && response.status < 300){
      return response.data;
    }
    else{
      return null;
    }
  } catch (error) {
    console.error(error);
    return null;
  }
}

/*  This is function to get the user profile */
export async function getUserProfile(userId, token){
  try {
    const response = await api.get(`/users/profile/${userId}`, {
      headers : getHeaders()
    });
    return response.data;
  } catch (error) {
    throw error;
  }
}

/* This isthe function to delete a user */
export async  function deleteUser(email){
  try {
    const response = await api.delete(`/users/delete/${email}`, {
      headers : getHeaders()
    });
    return response.data;
  } catch (error) {
    return error.message
  }
} 

/* This is the function to get a single user */
export async function getUser(email, token) {
	try {
		const response = await api.get(`/users/${email}`, {
			headers: getHeaders()
		})
		return response.data
	} catch (error) {
		throw error
	}
}

/* This is the function to get user bookings by the user id */
export async function getBookingsByUserId(userId, token) {
	try {
		const response = await api.get(`/bookings/user/${userId}/bookings`, {
			headers: getHeaders()
		})
		return response.data
	} catch (error) {
		console.error("Error fetching bookings:", error.message)
		throw new Error("Failed to fetch bookings")
	}
}