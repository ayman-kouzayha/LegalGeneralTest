# LegalGeneralTest

**Build**
---
To build the application using Windows Terminal, navigate to the project path using the 'cd' command and run the command:
* .\gradlew clean build

To run the application:
* .\gradlew bootrun

**A Postman export file can be found in the repo by the name: "Legal&General.postman_collection.json"**

**Init API**
----
Initializes the vending machine with the provided coins, the API will clear any existing coins and will start from fresh using provided data.

* **URL**

  http://localhost:8080/api/change/init/

* **Method:**

  `POST`

* **Data Params**

Example of the data need to be sent in the request body

[
{
"name": "Pound",
"value": 100,
"quantity": 2
},
{
"name": "Two Pounds",
"value": 200,
"quantity": 2
},
{
"name": "Two Pence",
"value": 2,
"quantity": 2
},
{
"name": "Pence",
"value": 1,
"quantity": 2
},
{
"name": "Ten Pence",
"value": 10,
"quantity": 2
},
{
"name": "Five Pence",
"value": 5,
"quantity": 2
},
{
"name": "Twenty Pence",
"value": 20,
"quantity": 2
},
{
"name": "Fifty Pence",
"value": 50,
"quantity": 2
}
]

* **Success Response:**

    * **Code:** 200 <br />
      **Content:** `"Request completed successfully"`

* **Error Response:**

    * **Code:** 400 BAD REQUEST <br />
      **Content:** `"Bad data!"`

  OR

    * **Code:** 500 INTERNAL SERVER ERROR <br />
      **Content:** `"Server error!"`


**Deposit API**
----
Deposits the provided coins into the server, the quantity will accumulate if the provided coin already exists, if not the API will create new entries for the new coins.

* **URL**
  
  http://localhost:8080/api/change/deposit/
  
* **Method:**

  `POST`

* **Data Params**

Example of the data need to be sent in the request body

[
{
"name": "Five Pence",
"value": 5,
"quantity": 2
}
]

* **Success Response:**

    * **Code:** 200 <br />
      **Content:** `"Request completed successfully"`

* **Error Response:**

    * **Code:** 400 BAD REQUEST <br />
      **Content:** `"Bad data!"`

  OR

    * **Code:** 500 INTERNAL SERVER ERROR <br />
      **Content:** `"Server error!"`


**Get Change API**
----
Deposits the provided coins into the server, the quantity will accumulate if the provided coin already exists, if not the API will create new entries for the new coins.

* **URL**

  http://localhost:8080/api/change/get/{change}

* **Method:**

  `GET`

* **URL Params**

   **Required:**

   `change=[integer]`

The amount of change the API should calculate its coins in Pences (example: for 25 pences change should be 25, for 1 pound change is 100)

* **Success Response:**

    * **Code:** 200 <br />
      **Content:** Array of the coin changes <br/>
      [
      {
      "name": "Pence",
      "value": 1,
      "quantity": 2
      }
  ]

* **Error Response:**

    * **Code:** 400 BAD REQUEST, if amount is zero or negative or machine was not initialized<br />
OR
    * **Code:** 404 NOT FOUND, If machine can't provide the change <br />

  OR

    * **Code:** 500 INTERNAL SERVER ERROR <br />
    