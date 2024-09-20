# PARCIAL-RECUPERACIÓN
## CalcReflex
CalcReflex is a Java-based client-server application that provides a web interface for performing mathematical calculations and array sorting operations using reflection.

## Project Structure

The project consists of three main Java files:

1. `CalcreflexBEServer.java`: The backend server that handles mathematical operations and sorting.
2. `CalcreflexFacade.java`: A facade server that acts as an intermediary between the client and the backend server.
3. `HttpConnectionExample.java`: A utility class for making HTTP requests to the backend server.

## Features

- Perform various mathematical operations using Java's `Math` class methods.
- Sort arrays using the bubble sort algorithm.
- Web interface for easy interaction with the calculator.

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- A Java IDE (e.g., IntelliJ IDEA, Eclipse) or command-line tools for compiling and running Java applications

## Setup and Running

1. Clone the repository 
  ```
   git clone https://github.com/N1CKZ3B/AREP_Recuperacion
   ```
  
2. Compile the package:
   ```
    mvn package
   ```

3. Start the backend server:
   ```
   java -cp target/CalcReflex-1.0-SNAPSHOT.jar com.mycompany.calcreflex.CalcreflexBEServer
   ```
   The backend server will start on port 36000.

4. In a separate terminal, start the facade server:
   ```
   java -cp target/CalcReflex-1.0-SNAPSHOT.jar com.mycompany.calcreflex.CalcreflexFacade
   ```
   The facade server will start on port 35000.

5. Open a web browser and navigate to `http://localhost:35000` to access the calculator interface.

## Usage

1. Enter a mathematical operation or sorting command in the input field on the web interface.

2. Click the "Calcular" button to perform the operation.

3. The result will be displayed below the input field.

   ![image](https://github.com/user-attachments/assets/03a3d624-2a76-4363-9c43-263a1738e03d)


### Supported Operations

- Mathematical operations: Use methods from Java's `Math` class (e.g., `sin`, `cos`, `tan`, `sqrt`, `pow`).
  Example: `sin(0.5)`, `pow(2,3)`

- Bubble Sort: Use the `bbl` command followed by a comma-separated list of numbers.
  Example: `bbl(5,2,8,1,9)`

  ![image](https://github.com/user-attachments/assets/fe9abde8-000a-4904-bb83-bd65e869366f)

  ![image](https://github.com/user-attachments/assets/a98cd3e3-12ca-4c20-84bb-2bb3068ff8b0)

## Architecture

The application follows a client-server architecture with an additional facade layer:

1. The client (web browser) sends requests to the facade server (CalcreflexFacade).
2. The facade server forwards the request to the backend server (CalcreflexBEServer).
3. The backend server processes the request and sends the result back to the facade server.
4. The facade server relays the result to the client.

This architecture allows for potential load balancing and additional processing at the facade layer if needed.

![image](https://github.com/user-attachments/assets/08aef8ea-cde8-4294-b19b-f12add31f1a6)

## Class Diagram

![image](https://github.com/user-attachments/assets/0773de7d-3450-400c-9522-414e5e37d19c)


The class diagram above illustrates the main structure of the CalcReflex project. Below is a description of each class and their relationships:

1. **CalcreflexBEServer**
   - This is the main backend server class.
   - Key methods:
     - `main(args: String[])`: Starts the server and handles incoming connections.
     - `computeMathCommand(command: String)`: Processes mathematical commands using reflection.
     - `bubbleSort(list: String)`: Implements the bubble sort algorithm.
     - `getDefaultResponse()`: Generates the default HTML response.
   - Responsibilities:
     - Listens for requests on port 36000.
     - Processes mathematical operations and sorting.
     - Generates HTTP responses.

2. **CalcreflexFacade**
   - This class acts as a facade between the client and the backend server.
   - Key methods:
     - `main(args: String[])`: Starts the facade server and handles incoming connections.
     - `getHtmlClient()`: Generates the HTML code for the user interface.
   - Responsibilities:
     - Listens for requests on port 35000.
     - Forwards requests to the backend server.
     - Provides the HTML user interface.

3. **HttpConnectionExample**
   - This is a utility class for making HTTP connections.
   - Key methods:
     - `getResponse(request: String)`: Performs an HTTP GET request and returns the response.
   - Responsibilities:
     - Handles HTTP communication between the facade server and the backend server.

### Relationships between classes:

- `CalcreflexFacade` uses `HttpConnectionExample` to send requests to the backend server.
- Although there's no direct dependency in the code, `CalcreflexFacade` logically communicates with `CalcreflexBEServer` through HTTP requests.

This class structure allows for a clear separation of responsibilities:
- The user interface and initial request handling are managed in `CalcreflexFacade`.
- Business logic and command processing are performed in `CalcreflexBEServer`.
- Communication between the two servers is facilitated through `HttpConnectionExample`.

This architecture provides flexibility and allows for easy expansion or modification of functionality in the future.


## Author

Nicolas Sebastian Achuri Macias
