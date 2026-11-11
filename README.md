# SC2002 Restaurant Rush — Group Project

## Project Overview

Restaurant Rush is a turn-based restaurant management game developed in Java for the SC2002 Object-Oriented Design & Programming group assignment.

The player manages a restaurant over 18 turns by assigning tasks to the Waiter and Chef while balancing customer satisfaction, revenue and limited resources.

## How to Run

**Requirements:** Java JDK 17 or later.

Clone the repository:

```bash
git clone https://github.com/desmunz/SC2002-Restaurant-rush-group-project.git
cd SC2002-Restaurant-rush-group-project
```

Compile:

```bash
mkdir -p out
javac -d out $(find src/main/java -name "*.java")
```

Run:

```bash
java -cp out restaurantrush.Main
```

The game runs in the terminal. Follow the on-screen instructions to choose Waiter and Chef actions.

## Game Rules

- 18 turns, 2 tables and 6 customers.
- Customers arrive at turns 1, 4, 7, 10, 13 and 16.
- Waiter: one task per turn (seat, take order, serve or wait).
- Chef: one preparation unit per turn.
- Cashier: automatically processes at most one eligible payment per turn.

### Menu

| Item | Price | Preparation Units |
|---|---:|---:|
| Salad | $9.00 | 1 |
| Burger | $15.00 | 1 |
| Pasta | $18.00 | 2 |

### Victory Conditions

At the end of turn 18, the player wins if all three conditions are satisfied:

- At least 4 paid customers.
- At least $45.00 revenue.
- Average satisfaction of paid customers at least 60.

## Development Stages

### Stage 1 — Basic Restaurant Simulation

Implements regular customers, the waiting queue, tables, menu, orders, staff actions, satisfaction, abandonment, payment and victory/defeat conditions.

**Git tag:** to be added


## Stage-by-Stage Changes

for later stages read me
