# BVA Analysis for Card


### Method under test: `Card()`

|              | State of the System | Expected output | 
|--------------|---------------------|-----------------|
| Step 1  |        Input of Card Type value to give to new card, or null/invalid card type         |        A new card with a stored type equal to argument or an Illegal Argument Exception on a null/indalid edge          |  
| Step 2  |          Enumerated card type or invalid/null card         |       A Card Instance / Illegal Argument Exception         |
| Step 3  |          Card w/ Defuse Type, null value     |      A card with card type Defuse and Illegal Argument Exception            |


|              | System under Test | Expected output | Implemented?              |
|--------------|---------------------|-----------------|---------------------------|
| Test Case 1  |        new Card(DEFUSE)        |       constructor returns normally, getTypeofCard() == DEFUSE          | white_check_mark: |
| Test Case 2  |         new Card(null)        |        IllegalArgumentException thrown; no Card produced       | :white_check_mark: |


### Method under test: `getTypeofCard()`

|              | Input | Expected output | 
|--------------|---------------------|-----------------|
| Step 1  |        None         |        Card Type given at construction          |  
| Step 2  |          N/A      |       A Card Instance / Illegal Argument Exception         |
| Step 3  |          N/A   |      Exploding Kitten, Defuse, See the Future, coming soon!          |

|              | State of the System | Expected output | Implemented?              |
|--------------|---------------------|-----------------|---------------------------|
| Test Case 1  |          new Card(Exploding_Kitten)           |      Exploding_Kitten           | :white_check_mark: |
| Test Case 2  |          new Card(Defuse)             |        Defuse         | :white_check_mark: |
| Test Case 3  |         new Card(See_The_Future              |         See_The_Future        | :white_check_mark: |