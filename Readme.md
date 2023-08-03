# Description
### This project is part of the Workshop course, which was done by 5 teammates, originally in our common repository.
Market is an online store. Similar to other online stores, it supports buying and selling, store managing and system managing for admins. Market also supports real-time messaging.

In our project we used Hibernate as ORM tool and Vaadin for UI.
Below is the Original readme file.

## Configuration

The configuration data for the database and initialization can be set in the `config.properties` file. Here is an example:

```properties
InitializePath=initData/data.json
DB_FIELD1=relative/path/to/DB.db
```

## Data Format

This is the format. It represents data with 2 fields:

1. `List<string>`: a list of admins to add
2. `List<RegisterUser>`: a list of registered users

Each user has the following properties:
- `List<Store>`: a list of stores

Each store has the following properties:
- `List<Item>`: a list of items
- `List<String> newOwnersToAdd`: a list of new owners to add
- `List<String> newManagersToAdd`: a list of new managers to add
- If user showed again then its for owner who is not the founder to add new owner.

- #### Please note that this is not a database, but an initialization structure.

- #### Admin and guest users are built-in in the system.

## Example (JSON Code)

```json
{
  "adminsList": ["Amit", "Tamir"],
  "registeredUserList": [
    {
      "username": "Kayatsa",
      "password": "kayatsasPass",
      "address": "addressOk",
      "bDay": "1999-07-11",
      "stores": [
        {
          "founderName": "Kayatsa",
          "storeName": "Kayatsa1 Store",
          "ownersList": ["Tamir", "Amit"],
          "managersList": ["Avi"],
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
```

