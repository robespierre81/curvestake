# CurveStake Blockchain

CurveStake is a proof-of-stake (PoS) blockchain built with Java, featuring smart contract support. This blockchain includes a unique `BlackjackContract` smart contract, which allows users to play a game of Blackjack on the blockchain, making it a fun and engaging platform inspired by Bender from *Futurama*.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Smart Contract: Blackjack](#smart-contract-blackjack)
- [Configuration](#configuration)
- [Contributing](#contributing)
- [License](#license)

## Features

- **Proof of Stake Consensus:** Efficient and environmentally friendly consensus mechanism.
- **Smart Contract Support:** Deploy and execute smart contracts directly on the blockchain.
- **BlackjackContract:** A fun and unique smart contract allowing users to play Blackjack on the blockchain.
- **Stakeholders and Validators:** Participate in the blockchain by staking tokens and validating transactions.
- **Flexible Configuration:** Easily configurable parameters such as port number through external files.

## Prerequisites

- **Java 11 or later**
- **Maven 3.6.0 or later**

## Installation

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/yourusername/curvestake.git
   cd curvestake
    ```

2. **Build the Project:**

Use Maven to compile the project:

    ```bash
    mvn clean install
    ```

3. **Configure the Blockchain:**

Ensure your config.properties file is correctly set up in the src/main/resources directory. This file should include configuration like the port number:

    ```properties
    port=5000

3. **Usage**
Running the Blockchain
Start the CurveStake Server:

Run the CurveStakeServer to start your blockchain:

bash
Copy code
java -cp target/your-app.jar com.bodiva.curvestake.Main
Deploy and Interact with Smart Contracts:

You can deploy and interact with the BlackjackContract through your blockchain. The Main class in the project demonstrates how to deploy the contract and start playing:

bash
Copy code
java -cp target/your-app.jar com.bodiva.curvestake.BlackjackInteraction
Smart Contract: Blackjack
The BlackjackContract is a smart contract on the CurveStake blockchain that allows players to engage in a game of Blackjack. Players can:

Place Bets: Stake tokens to start a game.
Hit: Draw additional cards in the game.
Stand: End your turn and see if you can beat the dealer.
Example:
java
Copy code
// Create a player wallet and fund it
Wallet playerWallet = new Wallet();
blackjackContract.addFunds(playerWallet.getPublicKey(), 100);

// Place a bet and play
Transaction betTransaction = new Transaction(playerWallet.getPublicKey(), null, 0, 0, 0, new TransactionInput[0]);
betTransaction.setFunction("placeBet");
betTransaction.setArgs(new String[]{"10"});
server.executeSmartContract(blackjackAddress, betTransaction);
Configuration
The blockchain parameters, such as port numbers, are stored in the config.properties file located in the src/main/resources directory. You can modify this file to suit your needs.

Sample config.properties:
properties
Copy code
port=5000
This file can be externalized and loaded from outside the JAR file to allow runtime configuration changes.

Contributing
Contributions are welcome! Please fork the repository and submit a pull request with your changes. Ensure your code follows the existing coding style and includes appropriate tests.

License
This project is licensed under the MIT License - see the LICENSE file for details.

