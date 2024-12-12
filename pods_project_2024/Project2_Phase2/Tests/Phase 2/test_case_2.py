import requests
import random
import sys
from threading import Thread
from user import *
from wallet import *
bookingServiceURL = "http://localhost:8081"  # Replace with your booking service URL
  # Replace with your wallet service URL

booked_seats = 0

def get_show(show_id):
    response = requests.get(bookingServiceURL + f"/shows/{show_id}")
    return response


def get_bookings(user_id):
    response = requests.get(bookingServiceURL + f"/bookings/users/{user_id}")
    return response


def post_booking(user_id, show_id, seats_booked):
    payload = {"show_id":show_id, "user_id":user_id, "seats_booked":seats_booked}
    response = requests.post(bookingServiceURL + "/bookings", json = payload)
    return response
# Thread 1: Books seats
def t1(user_id, show_id):
    global booked_seats
    for i in range(10):
        seats_to_book = random.randint(1, 3)
        print(f"t1:{seats_to_book}")
        response = post_booking(user_id,show_id,seats_booked=seats_to_book)
        if response.status_code == 200:
            booked_seats += seats_to_book
            #print(f"t1:{booked_seats}")
           

# Thread 1: Books seats
def t2(user_id, show_id):
    global booked_seats
    for i in range(10):
        seats_to_book = random.randint(2, 4)
        print(f"t2:{seats_to_book}")
        response = post_booking(user_id,show_id,seats_booked=seats_to_book)
        if response.status_code == 200:
            booked_seats += seats_to_book
            #print(f"t2:{booked_seats}")
          

def main():
    try:
        # Delete existing users and wallets
        response = delete_users()
        if not check_response_status_code(response, 200):
            return False
    
        response = delete_wallets()
        if not check_response_status_code(response, 200):
            return False

        # Create a new user
        response = post_user('Anurag Kumar', 'ak47@iisc.ac.in')
        if not check_response_status_code(response, 201):
            return False
        user = response.json()

        # Credit the wallet with $10,000
        response = put_wallet(user['id'], 'credit', 100000)
        if not check_response_status_code(response, 200):
            return False

        # Get initial available seats
        show_id = 1  # Adjust show ID as needed
        initial_available_seats = get_show(show_id).json().get("seats_available",0)
        print(f"init: {initial_available_seats}")

        # Run threads to book and cancel seats
        thread1 = Thread(target=t1, args=(user['id'], show_id))
        thread2 = Thread(target=t2, args=(user['id'],show_id))

        thread1.start()
        thread2.start()

        thread1.join()
        thread2.join()

        # Check available seats after booking and canceling
        updated_available_seats = get_show(show_id).json().get("seats_available",0)
        print(f"up:{updated_available_seats}")
        difference = initial_available_seats-updated_available_seats
        print(f"diff:{difference}")
        print(f"booked:{booked_seats}")
        if difference == booked_seats:
            print("Booking operation is serialized.")
            return True
        else:
            print("Booking operation is not serialized.")
            return False

    except Exception as e:
        print("Error:", e)
        return False

if __name__ == "__main__":
    if main():
        sys.exit(0)
    else:
        sys.exit(1)
