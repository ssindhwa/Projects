import requests

userServiceURL = "http://localhost:8080"
bookingServiceURL = "http://localhost:8081"
walletServiceURL = "http://localhost:8082"

def main():
    name = "John Doe1"
    email = "johndo1e@mail.com"
    show_id = 7
    add_money_and_book_ticket(name, email, show_id)

def create_user(name, email):
    new_user = {"name": name, "email": email}
    response = requests.post(userServiceURL + "/users", json=new_user)
    print(response)
    return response

def create_wallet(user_id):
    requests.put(walletServiceURL+f"/wallets/{user_id}", json={"action":"credit", "amount":0})

def get_wallet(user_id):
    response = requests.get(walletServiceURL + f"/wallets/{user_id}")
    print(response)
    return response

def update_wallet(user_id, action, amount):
    response = requests.put(walletServiceURL + f"/wallets/{user_id}", json={"action":action, "amount":amount})
    print(response)
    return response

def get_show_details(show_id):
    response = requests.get(bookingServiceURL + f"/shows/{show_id}")
    print(response)
    return response   


def delete_show(user_id,show_id):
    response = requests.delete(bookingServiceURL+f"/bookings/users/{user_id}/shows/{show_id}")
    print(response)
    return response

def delete_users():
    requests.delete(userServiceURL+f"/users")    

def add_money_and_book_ticket(name,email,show_id):
    try:
        delete_users()
        new_user = create_user(name,email) #create_user
        new_user_id = new_user.json()['id']
        print(new_user_id)
        update_wallet(new_user_id,"credit",1000) #update_wallet
        show_details_before_booking = get_show_details(show_id) #get_show_details
        old_wallet_balance = get_wallet(new_user_id).json()['balance'] #get_wallet
        print(old_wallet_balance)
        new_booking = {"show_id": show_id, "user_id": new_user_id, "seats_booked": 10}
        requests.post(bookingServiceURL + "/bookings", json=new_booking)
        delete_show(new_user_id,show_id) #delete_show
        show_details_after_booking = get_show_details(show_id)
        print(show_details_after_booking.json())
        print(show_details_before_booking.json())
        if(show_details_after_booking.json()['seats_available'] == show_details_before_booking.json()['seats_available'] and old_wallet_balance == get_wallet(new_user_id).json()['balance']):
            print("Test passed")
        else:
            print("Test failed")
    except:
        print("Some Exception Occurred")

if __name__ == "__main__":
    main()
