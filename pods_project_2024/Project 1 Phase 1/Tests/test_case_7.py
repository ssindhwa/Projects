import requests


bookingServiceURL = "http://localhost:8081"
userServiceURL = "http://localhost:8080"
walletServiceURL = "http://localhost:8082"

def main():
    test_theatre()


def create_user(name, email):
    new_user = {"name": name, "email": email}
    response = requests.post(userServiceURL + "/users", json=new_user)
    return response

def delete_users():
    requests.delete(userServiceURL+f"/users")    

def update_wallet(user_id, action, amount):
    response = requests.put(walletServiceURL + f"/wallets/{user_id}", json={"action":action, "amount":amount})
    return response

def test_theatre():
    try:
        # Test Get Theatres
        theatres_response = requests.get(bookingServiceURL + "/theatres")
        theatres_status = theatres_response.status_code
        print(f"Get Theatres Status: {theatres_status}")
        
        if theatres_status == 200:
            print("Get Theatres Test Passed")
        else:
            print("Get Theatres Test Failed")
        
        
        theatre_id = 1 
        shows_response = requests.get(bookingServiceURL + f"/shows/theatres/{theatre_id}")
        shows_status = shows_response.status_code
        print(f"Get Shows for Theatre Status: {shows_status}")
        
        if shows_status == 200:
            print("Get Shows for Theatre Test Passed")
        elif shows_status == 404:
            print("Get Shows for Theatre Test Passed (No shows found)")
        else:
            print("Get Shows for Theatre Test Failed")
        
        
        show_id = 1  
        show_response = requests.get(bookingServiceURL + f"/shows/{show_id}")
        show_status = show_response.status_code
        print(f"Get Show Details Status: {show_status}")
        
        if show_status == 200:
            print("Get Show Details Test Passed")
        else:
            print("Get Show Details Test Failed")
        
        delete_users()
        
        new_user = create_user("John","john@gmail.com")
        user_id = new_user.json()['id']
        bookings_response = requests.get(bookingServiceURL + f"/bookings/users/{user_id}")
        bookings_status = bookings_response.status_code
        print(f"Get Bookings for User Status: {bookings_status}")
        
        if bookings_status == 200:
            print("Get Bookings for User Test Passed")
        elif bookings_status == 404:
            print("Get Bookings for User Test Passed (No bookings found)")
        else:
            print("Get Bookings for User Test Failed")
        
        # Test Create Booking
        # Assuming valid show_id and user_id
        update_wallet(user_id,"credit",1000)
        booking_payload = {"show_id": 1, "user_id": user_id, "seats_booked": 2}
        booking_payload1 = {"show_id": 2, "user_id": user_id, "seats_booked": 2}
        create_booking_response = requests.post(bookingServiceURL + "/bookings", json=booking_payload)
        create_booking_response1 = requests.post(bookingServiceURL + "/bookings", json=booking_payload1)
        create_booking_status = create_booking_response.status_code
        print(f"Create Booking Status: {create_booking_status}")
        
        if create_booking_status == 200:
            print("Create Booking Test Passed")
        else:
            print("Create Booking Test Failed")
        
        # Test Delete User Show Bookings
        show_id = 1  # Assuming show_id 1 exists
        delete_user_show_bookings_response = requests.delete(bookingServiceURL + f"/bookings/users/{user_id}/shows/{show_id}")
        delete_user_show_bookings_status = delete_user_show_bookings_response.status_code
        print(f"Delete User Show Bookings Status: {delete_user_show_bookings_status}")
        
        if delete_user_show_bookings_status == 200:
            print("Delete User Show Bookings Test Passed")
        elif delete_user_show_bookings_status == 404:
            print("Delete User Show Bookings Test Passed (No bookings found for the given show)")
        else:
            print("Delete User Show Bookings Test Failed")


        # Test Delete User Bookings
        delete_user_bookings_response = requests.delete(bookingServiceURL + f"/bookings/users/{user_id}")
        delete_user_bookings_status = delete_user_bookings_response.status_code
        print(f"Delete User Bookings Status: {delete_user_bookings_status}")
        
        if delete_user_bookings_status == 200:
            print("Delete User Bookings Test Passed")
        elif delete_user_bookings_status == 404:
            print("Delete User Bookings Test Passed (No bookings found)")
        else:
            print("Delete User Bookings Test Failed")
     
        # Test Delete All Bookings
        delete_all_bookings_response = requests.delete(bookingServiceURL + "/bookings")
        delete_all_bookings_status = delete_all_bookings_response.status_code
        print(f"Delete All Bookings Status: {delete_all_bookings_status}")
        
        if delete_all_bookings_status == 200:
            print("Delete All Bookings Test Passed")
        else:
            print("Delete All Bookings Test Failed")
        
    except Exception as e:
        print(f"Exception Occurred: {str(e)}")

if __name__ == "__main__":
    main()
