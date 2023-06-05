This is the format.
Its Data with 2 fields:
1.List<string> -> list of admins to add
2.List<RegisterUser>
Each user has List<Store>
Each store has List<Item> ,List<String newOwnersToAdd> ,List<String newManagersToAdd>.
This is not a database its initialize.
Admin and guess are builtin in the system.
Path is on Application, change it to change the path.

Example(json code):
{
  "adminsList": ["Amir","Tomer"],
  "registeredUserList":
  [
    {
      "username": "Sagi",
      "password": "sagisPass",
      "address": "addressOk",
      "bDay": "1999-07-11",
      "stores": [
        {
          "founderName": "Sagi",
          "storeName": "Sagi1 Store",
          "ownersList": ["Tomer","Amir"],
          "managersList": ["Yonatan"],
          "itemList": [
            {
              "itemName": "Eggs",
              "itemPrice": 4.5,
              "itemCategory": "Dairy",
              "weight": 5.0,
              "amount": 20
            }
          ]
        }
      ]
    }
  ]
}
