<h1 align="center">Welcome to Currency Transaction System 👋</h1>
<p>
</p>

> A Peer to Peer blockchain network used for currency transactions implemented using Spring Boot API



## Usage

1. Establish Blockchain 

Run the following command simuntaneously on different port(8050,8051,8052) to run different blockchain:
```sh
 mvn spring-boot:run -Drun.arguments=--server.port=PORT_NAME
```
Once the different chains are up, use the below command for each to check if all blockchains are working properly:

```sh
http://127.0.0.1:PORT_NAME/blockchain/get_chain
```
2. Establish P2P Network

Now we need to connect all 3 blockchain, so we need to prepare a json request.
So to connect chain running on port 8050 we will pass the following request:
```sh
{
	"node":["http://127.0.0.1:8051",   "http://127.0.0.1:8052"]
}
```
Now pass the above request and hit the below url:

use the following command:

```sh
http://127.0.0.1:8050/blockchain/connect_node
```
After this your chain running on 8050 will connect to chains running on port 8051, 8052. But to connect all of them you will need to do the same for each of the other chains as well. 

3. Make a Transaction

Let's now make a transaction on a block, so we need to prepare a json request:

```sh
{
  "sender": "SenderName",
  "receiver": "ReceiverName",
  "amount": 100
 }
``` 
Now pass the above request and hit the below url:

```sh
http://127.0.0.1:PORT_NAME/blockchain/add_transaction
``` 
Now, the transaction is done but not complete unles mining is done, so will need to mine in order to add it to blockchain.
So for mining lhit the below url:

```sh
http://127.0.0.1:PORT_NAME/blockchain/mine_block
```
Now the transaction is complete.

And if you will try to access the blockchain you will get the updated chain.

## Author

👤 **Rajat Wadhwa**

* Github: [@Rajatw97](https://github.com/Rajatw97)
* LinkedIn: [@rajat-wadhwa-a21326163](https://linkedin.com/in/rajat-wadhwa-a21326163)

