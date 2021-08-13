# Point Bank

This applications simulates the ability to earn, spend, and get the balance of points.

## Setup

### Git

Make sure that Git is installed on your system. Running `git config -l` will list your git configuration
if it is installed.

If not, follow the instructions on the download page for your Operating System at https://git-scm.com/downloads

### Gradle

* A version of gradle has been bundled with the repository.

### Java 11

* Make sure that you have at least Java 11 installed on your system.
    * in a command prompt or terminal type `java -version` to see which version of java you have
      installed
    * it should say something to the effect of

```
java -version
openjdk version "11.0.12" 2021-07-20 LTS
OpenJDK Runtime Environment Corretto-11.0.12.7.1 (build 11.0.12+7-LTS)
OpenJDK 64-Bit Server VM Corretto-11.0.12.7.1 (build 11.0.12+7-LTS, mixed mode)
```

* If your version isn't 11+ or says `java` was not found follow the instructions for your Operating
  System
    * **Linux**: https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/linux-info.html
    * **Mac**: https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/macos-install.html
    * **Windows**: https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/windows-7-install.html
* After following any installation instructions confirm your version of Java is at 11 or higher.

### IDE (Optional)

* Although not necessary to run the application, if you have an IDE that is capable of running Java
  applications you can also use that.
* Follow the necessary steps in your IDE to run the `PointBankApplication` class.

## Cloning the Repo

From a terminal or command prompt run `git clone https://github.com/TraumaER/point-bank.git` this will clone the repository 
into a directory called `point-bank` rooted at the current working directory of your terminal window.

## Running the application

These instructions will be focused on running the application via the terminal.

1. Open a new terminal or command prompt to the project directory.
2. Confirm your java version is 11+ `java -version`
   * Perform instructions above if this is not the case
3. Run one of the following commands
   * **Windows Command Prompt**: `gradlew.bat bootRun`
   * **Linux/Mac Terminal**: `./gradlew bootRun`
     * this will also work if you are running a MinTTY terminal on Windows. 

That's it! The application should now be running in your terminal window and accessible
from `localhost:8080` with the following API endpoints.

If the logs state it failed to start and exited because the port `8080` was already taken. Stop any processes
that are running on that port and try again.

## API Endpoints

### `GET /balances`

Returns all point balances for existing Payers. If no payers exist an empty object will be returned.

#### Response

Response is sorted by Payer name.

```typescript
type Integer = number
type Balances = {
  [key: string]: Integer
}
```

_Example Response_

```json
{
  "Pizza Hut": 1000,
  "Target": 1000,
  "Walmart": 1000
}
```

### `POST /add-transaction`

Adds a transaction to the system.

#### Request

```typescript
type Integer = number
type TransactionRequest = {
  payer: string
  points: Integer
  timestamp?: string
}
```

| Prop | Type | Description |
| ---- | ---- | ----------- |
| `payer` | `String` | Display name for the payer of transaction. |
| `points` | `Integer` | Number of points the transaction earned; Must be greater than `0` |
| `timestamp` | `ZonedDateTime` | Optional timestamp for the transaction; Defaults to current datetime; When passed in request body, string must be formatted in `ZonedDateTime` format. E.g.`2020-12-25T15:00:00Z` |

#### Response

| Status | Description |
| ------ | ----------- |
| `200`  | Successfully added the transaction |

### `POST /spend`

Allows a user to spend points. Points will be spent starting with the oldest points first.

#### Request

```typescript
type Integer = number
type SpendRequest = {
  points: Integer
}
```

| Prop | Type | Description |
| ---- | ---- | ----------- |
| `points` | `Integer` | Number of points to spend; Must be greater than `0` |

#### Response

```typescript
type Integer = number
type SpendTransaction = {
  payer: string
  points: Integer
}

type SpendResponse = SpendTransaction[]
```

| Prop | Type | Description |
| ---- | ---- | ----------- |
| `payer` | `String` | Name of payer who's points were spent. |
| `points` | `Integer` | Number of points spent from this payer; Will be a negative number |

_Example Response_

```json
[
  {
    "payer": "Target",
    "points": -500
  }
]
```

## Running Tests

A handful of integration tests have been written to ensure the `BankService` performs its duties.

To perform these tests ensure that your setup is configured to run the application. Note the application does
not need to be running to run the tests.

`./gradlew test` on Linux or Mac, `gradlew.bat test` on Windows