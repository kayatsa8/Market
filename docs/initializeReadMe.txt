This is the format.
Its List<RegisterUser>
Each user has List<Store>
Each store has List<Item> ,List<String newOwnersToAdd> ,List<String newManagersToAdd>.
This is not a database its initialize.
Admin and guess are builtin in the system.
Path is on Application, change it to change the path.

{
    "username": "Amir1",
    "password": "amirsPass",
    "address": "addressOk",
    "bDay": "1999-07-11",
    "stores": [
      {
        "founderName": "Amir1",
        "storeName": "amir1s Store",
        "itemList": [
          {
            "itemName": "Bread",
            "itemPrice": 5.0,
            "itemCategory": "Wheat",
            "weight": 1.0,
            "amount": 50
          }
        ]
      }
    ]
  }
,{},{}
}

