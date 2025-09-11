# Tennis Game

A Kotlin application that simulates a tennis game scoring system, following the standard tennis scoring rules.

## What the Project Does

This application implements a tennis scoring system with the following features:
- Track scores for tennis matches between two players
- Award points to server or receiver
- Format scores according to tennis rules (Love, 15, 30, 40, Deuce, Advantage)
- Reset game scores
- Store player and game information in a MySQL database

The application follows a hexagonal architecture with:
- Domain models (Player, Game, GameState, Advantage)
- Application layer with ports and adapters
- Infrastructure layer with MySQL repositories

## Prerequisites

Before running the application, you need to set up:

1. **Java Development Kit (JDK) 21**
   - The project is configured to use Java 21

2. **MySQL Database**
   - Install MySQL Server
   - Create a database named "tennis"
   - The application uses the following default connection settings:
     - URL: jdbc:mysql://localhost:3306/tennis?useSSL=false
     - Username: YourUserName
     - Password: YourPassword
   - You can modify these settings in `src/main/kotlin/config/DatabaseConfig.kt`

3. **Database Initialization**
   - Run the SQL scripts in the following order:
     1. `src/main/resources/ddl.sql` (creates tables)
     2. `src/main/resources/dml.sql` (inserts initial data)
     3. `src/main/resources/procedures.sql` (creates procedures)

## How to Build and Run

### Important Note
Make sure you are in the project root directory (`tennis-game`), not in the `src` directory, when running Gradle commands.

### Building the Project

```bash
# Clone the repository
git clone https://github.com/yourusername/tennis-game.git
cd tennis-game

# Build with Gradle
./gradlew build
```

### Running the Application

#### Using IntelliJ IDEA (Recommended)
1. Open the project in IntelliJ IDEA
2. Click on the "Gradle" elephant icon on the right side to sync the project
3. Navigate to `src/main/kotlin/Main.kt`
4. Click the green play button next to the `main()` function or right-click and select "Run 'MainKt'"

#### Using Gradle (Alternative)
The project is configured with the Gradle application plugin, which enables running the application directly from the command line:

```bash
# Make sure you're in the project root directory, not in src
cd /path/to/tennis-game  # Not /path/to/tennis-game/src
./gradlew run
```

This command will compile the code if needed and run the main class (MainKt).

## How to Run Tests

The project uses JUnit 5 with Kotest assertions for testing.

### Using IntelliJ IDEA (Recommended)
1. Open the project in IntelliJ IDEA
2. Navigate to the test file you want to run (e.g., `src/test/kotlin/application/adapters/TennisScoreAdapterTest.kt`)
3. Right-click on the file or a specific test method and select "Run"
4. To run all tests, right-click on the `src/test/kotlin` directory and select "Run Tests in..."

### Using Gradle (Alternative)
Make sure you're in the project root directory, not in the src directory:

```bash
# Make sure you're in the project root directory
cd /path/to/tennis-game  # Not /path/to/tennis-game/src

# Run all tests
./gradlew test

# Run a specific test class
./gradlew test --tests "application.adapters.TennisScoreFormatAdapterTest"
```

Test results will be available in the `build/reports/tests/test` directory or in the IntelliJ IDEA test runner window.

## Project Structure

- `src/main/kotlin/` - Main source code
  - `application/` - Application layer (adapters and ports)
  - `domain/` - Domain models and DTOs
  - `infrastructure/` - Database repositories
  - `config/` - Configuration classes
- `src/main/resources/` - SQL scripts for database setup
- `src/test/kotlin/` - Test source code
