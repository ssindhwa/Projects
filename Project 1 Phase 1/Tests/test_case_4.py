import requests

userServiceURL = "http://localhost:8080"
bookingServiceURL = "http://localhost:8081"
walletServiceURL = "http://localhost:8082"

def main():
    name = "John Doe"
    email = "johndoe@mail.com"
    show_id1 = 7
    show_id2 =4
    add_money_and_book_ticket(name, email, show_id1,show_id2)

def create_user(name, email):
    new_user = {"name": name, "email": email}
    response = requests.post(userServiceURL + "/users", json=new_user)
    return response

def create_wallet(user_id):
    requests.put(walletServiceURL+f"/wallets/{user_id}", json={"action":"credit", "amount":0})

def get_wallet(user_id):
    response = requests.get(walletServiceURL + f"/wallets/{user_id}")
    return response

def update_wallet(user_id, action, amount):
    response = requests.put(walletServiceURL + f"/wallets/{user_id}", json={"action":action, "amount":amount})
    return response

def get_show_details(show_id):
    response = requests.get(bookingServiceURL + f"/shows/{show_id}")
    return response   


def delete_show_user(user_id):
    response = requests.delete(bookingServiceURL+f"/bookings/users/{user_id}")
    return response

def delete_users():
    requests.delete(userServiceURL+f"/users")    

def add_money_and_book_ticket(name,email,show_id1,show_id2):
    try:
        delete_users()
        new_user = create_user(name,email) #create_user
        new_user_id = new_user.json()['id']
        update_wallet(new_user_id,"credit",1000) #update_wallet
        show1_details_before_booking = get_show_details(show_id1)
        show2_details_before_booking  = get_show_details(show_id2)
        old_wallet_balance = get_wallet(new_user_id).json()['balance'] 
        new_booking1 = {"show_id": show_id1, "user_id": new_user_id, "seats_booked": 1}
        new_booking2 = {"show_id": show_id2, "user_id": new_user_id, "seats_booked": 2}
        requests.post(bookingServiceURL + "/bookings", json=new_booking1)
        requests.post(bookingServiceURL + "/bookings", json=new_booking2)
        int_wallet_balance  = get_wallet(new_user_id).json()['balance'] 
        show1_details_after_booking = get_show_details(show_id1)
        show2_details_after_booking = get_show_details(show_id2)
        delete_show_user(new_user_id)
        show1_details_after_delete = get_show_details(show_id1)
        show2_details_after_delete = get_show_details(show_id2)
        fin_wallet_balance  = get_wallet(new_user_id).json()['balance'] 
        if(fin_wallet_balance==old_wallet_balance):
            checkbal = True
        if(show1_details_before_booking.json()['seats_available']==show1_details_after_delete.json()['seats_available']):
            checkshow1= True
        if(show2_details_before_booking.json()['seats_available']==show2_details_after_delete.json()['seats_available']):
            checkshow2= True
        if(old_wallet_balance - int_wallet_balance == (show1_details_after_booking.json()['price']* 1 + show2_details_after_booking.json()['price']* 2)):
            walcheck =True
        if(show1_details_before_booking.json()['seats_available'] - show1_details_after_booking.json()['seats_available'] == 1):
            show1seats=True
        if(show2_details_before_booking.json()['seats_available'] - show2_details_after_booking.json()['seats_available'] == 2):
            show2seats=True
        if(checkbal and checkshow1 and checkshow2 and walcheck and show1seats and show2seats):
            print("Test passed")
        else:
            print("Test failed")
    except:
        print("Some Exception Occurred")

if __name__ == "__main__":
    main()
