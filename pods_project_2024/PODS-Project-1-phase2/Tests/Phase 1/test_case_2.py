import requests

userServiceURL = "http://localhost:8080"
bookingServiceURL = "http://localhost:8081"
walletServiceURL = "http://localhost:8082"

def main():
    name = "John Doe"
    email = "johndoe@mail.com"
    show_id = 7
    walletAmount = 1000
    add_money_and_check_detail(name, email, show_id, walletAmount)

def create_user(name, email):
    new_user = {"name": name, "email": email}
    response = requests.post(userServiceURL + "/users", json=new_user)
    return response

def get_wallet(user_id):
    response = requests.get(walletServiceURL + f"/wallets/{user_id}")
    return response

def update_wallet(user_id, action, amount):
    response = requests.put(walletServiceURL + f"/wallets/{user_id}", json={"action":action, "amount":amount})
    return response

def get_show_details(show_id):
    response = requests.get(bookingServiceURL + f"/shows/{show_id}")
    return response   


def delete_show(user_id,show_id):
    response = requests.delete(bookingServiceURL+f"/bookings/users/{user_id}/shows/{show_id}")
    return response

def delete_users():
    requests.delete(userServiceURL+f"/users")  


def add_money_and_check_detail(name, email, show_id, walletAmount):
    try:
        delete_users()
        new_user = create_user(name,email)
        new_user_id = new_user.json()['id']
        update_wallet(new_user_id,"credit",walletAmount)
        show_details_before_booking = get_show_details(show_id)
        old_wallet_balance = get_wallet(new_user_id).json()['balance']
        new_booking = {"show_id": show_id, "user_id": new_user_id, "seats_booked": 10}
        requests.post(bookingServiceURL + "/bookings", json=new_booking)
        show_details_after_booking = get_show_details(show_id)
        new_wallet_balance = get_wallet(new_user_id).json()['balance']
        if((old_wallet_balance - new_wallet_balance == show_details_after_booking.json()['price']* 10) and (show_details_before_booking.json()['seats_available'] - show_details_after_booking.json()['seats_available'] == 10)):
            print("Test Passed")
        else:
            print("Test Failed")
    except:
        print("Some Exception Occurred")

if __name__ == "__main__":
    main()
