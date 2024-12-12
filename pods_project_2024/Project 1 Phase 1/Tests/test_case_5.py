import requests

userServiceURL = "http://localhost:8080"
bookingServiceURL = "http://localhost:8081"
walletServiceURL = "http://localhost:8082"

def main():
    name1 = "John Doe"
    email1 = "johndoe@mail.com"
    name2= "Jane Doe"
    email2 = "janedoe@gmail.com"
    show_id1 = 7
    show_id2 =4
    add_money_and_book_ticket(name1, email1, name2,email2,show_id1,show_id2)

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


def delete_bookings():
    response = requests.delete(bookingServiceURL+f"/bookings")
    return response

def delete_users():
    requests.delete(userServiceURL+f"/users")    

def add_money_and_book_ticket(name1, email1, name2,email2,show_id1,show_id2):
    try:
        delete_users()
        new_user1 = create_user(name1,email1) #create_user
        new_user_id1 = new_user1.json()['id']
        new_user2 = create_user(name2,email2) #create_user
        new_user_id2 = new_user2.json()['id']
        update_wallet(new_user_id1,"credit",1000)
        update_wallet(new_user_id2,"credit",500)  #update_wallet
        show1_details_before_booking = get_show_details(show_id1)
        show2_details_before_booking  = get_show_details(show_id2)
        old_wallet_balance1 = get_wallet(new_user_id1).json()['balance'] 
        old_wallet_balance2 = get_wallet(new_user_id2).json()['balance'] 
        new_booking1 = {"show_id": show_id1, "user_id": new_user_id1, "seats_booked": 1}
        new_booking2 = {"show_id": show_id2, "user_id": new_user_id2, "seats_booked": 2}
        requests.post(bookingServiceURL + "/bookings", json=new_booking1)
        requests.post(bookingServiceURL + "/bookings", json=new_booking2)
        int_wallet_balance1  = get_wallet(new_user_id1).json()['balance'] 
        int_wallet_balance2 = get_wallet(new_user_id2).json()['balance'] 
        show1_details_after_booking = get_show_details(show_id1)
        show2_details_after_booking = get_show_details(show_id2)
        delete_bookings()
        show1_details_after_delete = get_show_details(show_id1)
        show2_details_after_delete = get_show_details(show_id2)
        fin_wallet_balance1  = get_wallet(new_user_id1).json()['balance'] 
        fin_wallet_balance2  = get_wallet(new_user_id2).json()['balance'] 
        if(fin_wallet_balance1==old_wallet_balance1 and fin_wallet_balance2==old_wallet_balance2):
            checkbal = True
        if(show1_details_before_booking.json()['seats_available']==show1_details_after_delete.json()['seats_available']):
            checkshow1= True
        if(show2_details_before_booking.json()['seats_available']==show2_details_after_delete.json()['seats_available']):
            checkshow2= True
        if(old_wallet_balance1 - int_wallet_balance1 == (show1_details_after_booking.json()['price']* 1) and old_wallet_balance2 - int_wallet_balance2 == (show2_details_after_booking.json()['price']* 2)):
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
