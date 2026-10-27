# BVA Analysis for Deck


### Method under test: `Deck()`

|              | State of the System | Expected output | 
|--------------|---------------------|-----------------|
| Step 1  |        None       |        A Deck of appropriate size   |  
| Step 2  |          N/A     |       A Deck Instance       |
| Step 3  |      N/A     |     Deck w/ 56 cards, First Card Exploding    |


|              | System under Test | Expected output | Implemented?              |
|--------------|---------------------|-----------------|---------------------------|
| Test Case 1  |        new Deck()        |      Deck Size == 56 cards         | :white_check_mark: |
| Test Case 2  |         new Deck()        |        Deck Size == 56 cards, First Card Exploding Kitten   | :white_check_mark: |
| Test Case 3  |         new Deck()        |        Deck Size == 56 cards, Last Card Defuse      | :white_check_mark: |


### Method under test: `shuffleDeck()`

|              | Input | Expected output | 
|--------------|---------------------|-----------------|
| Step 1  |        None         |       A deck of cards that has been shuffled    |  
| Step 2  |          N/A      |       A Shuffled Deck Instance       |
| Step 3  |          N/A   |       Deck of cards in new order        |

|              | State of the System | Expected output | Implemented?              |
|--------------|---------------------|-----------------|---------------------------|
| Test Case 1  |          empty Deck          |      empty deck unchanged      | :white_check_mark: |
| Test Case 2  |          Deck w/ 1 card             |        same single card         | :white_check_mark: |
| Test Case 3  |         Deck w/ 2 cards          |         same cards, potentially different order       | :white_check_mark: |
| Test Case 4  |         Deck w/ 56 cards containing duplicate cards       |         same cards, different order, duplicates present        | :white_check_mark: |

### Method under test: `drawCard()`

|              | Input | Expected output | 
|--------------|---------------------|-----------------|
| Step 1  |        None         |        The top card from the Deck is drawn          |  
| Step 2  |          N/A      |       A Card Instance / Illegal State Exception         |
| Step 3  |          N/A   |      A Defuse Card, Illegal State Exception        |

|              | State of the System | Expected output | Implemented?              |
|--------------|---------------------|-----------------|---------------------------|
| Test Case 1  |          Empty Deck         |     Illegal State Exception        | :white_check_mark: |
| Test Case 2  |          Deck w/ 1 card, Top Card Defuse            |        Defuse, Deck is now empty         | :white_check_mark: |
| Test Case 3  |         Deck w/ 2 card, [Defuse, Exploding_Kitten], Top Card Defuse             |         Defuse, Deck [Exploding_Kitten]        | :white_check_mark:|
| Test Case 4  |         Deck w/ 56 cards, Defuse at top              |         Defuse, Deck remains same w/ Defuse removed       | :white_check_mark:|

### Method under test: `getDeckSize()`

|              | Input | Expected output | 
|--------------|---------------------|-----------------|
| Step 1  |        None         |        The size (amount of cards) of the Deck given based on construction        |  
| Step 2  |          N/A      |      An Integer Value that tells us the amount of cards in the deck        |
| Step 3  |          N/A   |      0, 1, 16, 37      |

|              | State of the System | Expected output | Implemented?              |
|--------------|---------------------|-----------------|---------------------------|
| Test Case 1  |          Deck is empty          |      0           |:white_check_mark:|
| Test Case 2  |          Deck w/ 1 card           |        1         |  :white_check_mark: |
| Test Case 3  |         Deck w/ 3 cards           |         16        | :white_check_mark: |
| Test Case 4  |         Deck w/ 56 cards     |         37      | :white_check_mark:|

### Method under test: `peekTopofDeck()`

|              | Input | Expected output | 
|--------------|---------------------|-----------------|
| Step 1  |        Amount of cards that are able to peeked         |        A list containing all of the top number of cards from deck      |  
| Step 2  |        List <Card>, Integer Representing How Many Cards to peek at     |       List/ Illegal State Exception / Illegal Argument Exception         |
| Step 3  |          Deck size: 0, 1, 16, 37, Peek: -1, 0, 1, 3, 36   |      List/ Illegal State Exception / Illegal Argument Exception        |

|              | State of the System | Expected output | Implemented?              |
|--------------|---------------------|-----------------|---------------------------|
| Test Case 1  |          deck size: 0, Peek: 0          |      Illegal State Exception         | :white_check_mark: |
| Test Case 2  |          deck size: 0, Peek: -1           |       Illegal Argument Exception       |  :white_check_mark:  |
| Test Case 3  |         deck size: 1, Peek: 0          |         Empty List       |  :white_check_mark: |
| Test Case 4  |         deck size: 2, Peek: 1           |         List of 1 Top Card       | :white_check_mark:  |
| Test Case 5  |         deck size: 56, Peek: 3             |         List of 3 Top Cards     |  :white_check_mark:  |
| Test Case 6  |         deck size: 56,Peek: 55       |         List of 55 Top Cards      | :white_check_mark: |
| Test Case 7  |         deck size: 56,Peek: 56       |         List of 56 Top Cards      |  :white_check_mark:  |
| Test Case 8  |        deck size: 56, Peek: 57            |         Illegal State Exception        |  :white_check_mark:  |



### Method under test: `insertCardAt()`

|              | State of the System | Expected output | Implemented?              |
|--------------|---------------------|-----------------|---------------------------|
| Test Case 1  |          Deck Size: 56 cards, Exploding Kitten Card at index 0         |      Deck with Exploding Kitten at Index 0          | :white_check_mark: |
| Test Case 2  |          Deck Size: 56 cards, Exploding Kitten Card at index 1         |      Deck with Exploding Kitten at Index 1          | :white_check_mark:|
| Test Case 3 |          Deck Size: 56 cards, Exploding Kitten Card at index 56         |      Deck with Exploding Kitten at Index 55          | :white_check_mark: |
| Test Case 4 |          Deck Size: 56 cards, Exploding Kitten Card at index 57         |      Illegal Argument Exception      | :white_check_mark: |
| Test Case 5  |          Deck Size: 56 cards, Exploding Kitten Card at index -1        |      Illegal Argument Exception      | :white_check_mark: |
| Test Case 6  |          Deck Size: 56 cards, Null Card at index 1         |      Illegal Argument Exception          | :white_check_mark:|

### Method under test: `addCard()`

|              | State of the System | Expected output | Implemented?              |
|--------------|---------------------|-----------------|---------------------------|
| Test Case 1  |          empty deck, addCard(new Card(Defuse))       |      Deck Size == 1          | :white_check_mark: |
| Test Case 2  |          deck w/ 3 cards, addCard(new Card(Defuse))            |        Deck Size == 4, added card present all original cards remain         |:white_check_mark:|
| Test Case 3  |         deck w/ 56 and a Exploding Kitten Present, addCard(new Card(Exploding Kitten))               |         Deck Size == 16, removeAllCardsofType.size() == 2       | :white_check_mark: |
| Test Case 4  |          deck w/ 3 cards, addCard(null)            |        IllegalArgumentException        |:white_check_mark: |


### Method under test: `removeAllCardsofType()`

|              | State of the System | Expected output | Implemented?              |
|--------------|---------------------|-----------------|---------------------------|
| Test Case 1  |          empty deck, Defuse      |      empty list      | :white_check_mark: |
| Test Case 2  |          deck w/ 3 cards but no Defuse, Defuse          |        empty list       | :white_check_mark:  |
| Test Case 3  |         deck containing a defuse, Defuse            |        List of a Single Defuse Card      | :white_check_mark:|
| Test Case 4  |          deck w/ 3 cards, removeAllCardsofType(null)            |        IllegalArgumentException        |:white_check_mark:  |
| Test Case 5  |          deck w/ 56 cards contains two exploding kittens top and bottom of deck, Exploding_Kitten       |       List of two exploding kittens       |:white_check_mark:  |
| Test Case 6  |          deck w/ 56 cards contains two exploding kittens next to each other at top, Exploding_Kitten       |       List of two exploding kittens       |:white_check_mark:  |

### Method under test: `isEmpty()`

|              | State of the System | Expected output | Implemented?              |
|--------------|---------------------|-----------------|---------------------------|
| Test Case 1  |          empty deck      |      True     | :x: or :white_check_mark: |
| Test Case 2  |          deck w/ 56 cards          |        False     |:x: or :white_check_mark:  |
| Test Case 3 |          deck w/ 1 cards          |        False     |:x: or :white_check_mark:  |
