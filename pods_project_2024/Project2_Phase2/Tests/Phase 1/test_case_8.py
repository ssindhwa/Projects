import requests

walletServiceURL = "http://localhost:8082"
userServiceURL = "http://localhost:8080"

def main():
    test_wallet()


def create_user(name, email):
    new_user = {"name": name, "email": email}
    response = requests.post(userServiceURL + "/users", json=new_user)
    return response

def delete_users():
    requests.delete(userServiceURL+f"/users")    

def test_wallet():
    try:
        delete_users()

        new_user = create_user("John","john@gmail.com")
        user_id = new_user.json()['id']
        get_wallet_response = requests.get(walletServiceURL + f"/wallets/{user_id}")
        get_wallet_status = get_wallet_response.status_code
        print(f"Get Wallet Status: {get_wallet_status}")
        
        if get_wallet_status == 200:
            print("Get Wallet Test Passed")
        elif get_wallet_status == 404:
            print("Get Wallet Test Passed (User doesn't have a wallet)")
        else:
            print("Get Wallet Test Failed")
        
        # Test Update Wallet (Debit)
        update_wallet_payload_debit = {"action": "debit", "amount": 50}
        update_wallet_response_debit = requests.put(walletServiceURL + f"/wallets/{user_id}", json=update_wallet_payload_debit)
        update_wallet_status_debit = update_wallet_response_debit.status_code
        print(f"Update Wallet (Debit) Status: {update_wallet_status_debit}")
        
        if update_wallet_status_debit == 200:
            print("Update Wallet (Debit) Test Passed")
        elif update_wallet_status_debit == 400:
            print("Update Wallet (Debit) Test Passed (Insufficient balance)")
        else:
            print("Update Wallet (Debit) Test Failed")
        
        # Test Update Wallet (Credit)
        update_wallet_payload_credit = {"action": "credit", "amount": 100}
        update_wallet_response_credit = requests.put(walletServiceURL + f"/wallets/{user_id}", json=update_wallet_payload_credit)
        update_wallet_status_credit = update_wallet_response_credit.status_code
        print(f"Update Wallet (Credit) Status: {update_wallet_status_credit}")
        
        if update_wallet_status_credit == 200:
            print("Update Wallet (Credit) Test Passed")
        else:
            print("Update Wallet (Credit) Test Failed")
        
        # Test Delete Wallet
        delete_wallet_response = requests.delete(walletServiceURL + f"/wallets/{user_id}")
        delete_wallet_status = delete_wallet_response.status_code
        print(f"Delete Wallet Status: {delete_wallet_status}")
        
        if delete_wallet_status == 200:
            print("Delete Wallet Test Passed")
        elif delete_wallet_status == 404:
            print("Delete Wallet Test Passed (User doesn't have a wallet)")
        else:
            print("Delete Wallet Test Failed")
        
        # Test Delete All Wallets
        delete_all_wallets_response = requests.delete(walletServiceURL + "/wallets")
        delete_all_wallets_status = delete_all_wallets_response.status_code
        print(f"Delete All Wallets Status: {delete_all_wallets_status}")
        
        if delete_all_wallets_status == 200:
            print("Delete All Wallets Test Passed")
        else:
            print("Delete All Wallets Test Failed")
        
    except Exception as e:
        print(f"Exception Occurred: {str(e)}")

if __name__ == "__main__":
    main()
