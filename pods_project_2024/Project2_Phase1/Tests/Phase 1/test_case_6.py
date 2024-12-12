import requests


bookingServiceURL = "http://localhost:8081"
userServiceURL = "http://localhost:8080"
walletServiceURL = "http://localhost:8082"

def main():
    test_user()


def test_user():
    try:
        # Test Get Theatres
        new_user = {"name": "aa", "email": "aa@gmail.com"}
        response = requests.post(userServiceURL + "/users", json=new_user)
        user_id = response.json()["id"]
        response_status =response.status_code
        print(f"User Creation: {response_status}")
        
        if response_status == 201:
            print("Create User Test Passed")
        else:
            print("Create User Test Failed")
        

        new_user1 = {"name": "aa", "email": "aa@gmail.com"}
        response1 = requests.post(userServiceURL + "/users", json=new_user1)
        response_status1 =response1.status_code
        print(f"Duplicate Email Test: {response_status1}")
        
        if response_status1 == 400:
            print("Duplicate Email Test Passed")
        else:
            print("Duplicate Email Test Failed")
        
        
        user_response = requests.get(userServiceURL + f"/users/{user_id}")
        user_status = user_response.status_code
        print(f"Get User Status: {user_status}")
        
        if user_status == 200:
            print("Get User Test Passed")
        elif user_status == 404:
            print("Get User Test Passed (No users found)")
        else:
            print("Get User Test Failed")
        
        
       
       
        delete_user_by_id = requests.delete(userServiceURL + f"/users/9")
        delete_user_by_id_status = delete_user_by_id.status_code
        print(f"Delete User By Id Status: {delete_user_by_id_status}")
        
        if delete_user_by_id_status == 200:
            print("Delete User By Id Test Passed")
        elif delete_user_by_id_status == 404:
            print("Delete User By Id Test Passed (No bookings found for the given show)")
        else:
            print("Delete User By Id Test Failed")
     
        # Test Delete All Bookings
        delete_all_users = requests.delete(userServiceURL + "/users")
        delete_all_users_status = delete_all_users.status_code
        print(f"Delete All Users Status: {delete_all_users_status}")
        
        if delete_all_users_status == 200:
            print("Delete All Users Test Passed")
        else:
            print("Delete All Users Test Failed")
        
    except Exception as e:
        print(f"Exception Occurred: {str(e)}")

if __name__ == "__main__":
    main()
