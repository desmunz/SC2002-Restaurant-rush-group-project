# SC2002 Restaurant Rush — Group Project

## Project Overview

Restaurant Rush is a turn-based restaurant management game developed in Java for the SC2002 Object-Oriented Design & Programming group assignment.

The player manages a restaurant over 18 turns by assigning tasks to the Waiter and Chef while balancing customer satisfaction, revenue and limited resources.

## URLs

- [Group repository](https://github.com/desmunz/SC2002-Restaurant-rush-group-project)
- [Stage 1 snapshot](https://github.com/desmunz/SC2002-Restaurant-rush-group-project/tree/stage-1)
- [Stage 2 snapshot](https://github.com/desmunz/SC2002-Restaurant-rush-group-project/tree/stage-2)

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

## Reproducible Test Runs

| Replay | Result | Served | Unhappy departures | Paid | Revenue | Average paid satisfaction | Turns |
| --- | --- | --- | --- | --- | --- | --- | --- |
| Victory | VICTORY | 5 | 0 | 5 | $76.50 | 79.6 | 18 |
| Defeat | DEFEAT | 0 | 2 | 0 | $0.00 | N/A | 18 |

To reproduce the victory, start a fresh Java process and follow the schedule below. Numbers in the action descriptions are customer IDs for seating/ordering and order IDs for serving/preparation.

To reproduce the defeat, start a fresh Java process and pass every turn.

| Turn | Waiter action | Chef action |
| --- | --- | --- |
| 1 | Seat 1 | Pass |
| 2 | Take order 1 | Prepare 1 |
| 3 | Serve 1 | Pass |
| 4 | Seat 2 | Pass |
| 5 | Take order 2 | Prepare 2 |
| 6 | Serve 2 | Pass |
| 7 | Seat 3 | Pass |
| 8 | Take order 3 | Prepare 3 |
| 9 | Serve 3 | Pass |
| 10 | Seat 4 | Pass |
| 11 | Take order 4 | Prepare 4 |
| 12 | Pass | Prepare 4 |
| 13 | Serve 4 | Pass |
| 14 | Seat 5 | Pass |
| 15 | Take order 5 | Prepare 5 |
| 16 | Serve 5 | Pass |
| 17 | Seat 6 | Pass |
| 18 | Take order 6 | Prepare 6 |

## Stage 2 Diagrams

![Stage 2 class diagram](documents/diagrams/stage-2-class-diagram.png)

[Full-resolution diagram](documents/diagrams/stage-2-class-diagram.png)

## Development Stages

### Stage 1 — Basic Restaurant Simulation

Implements regular customers, the waiting queue, tables, menu, orders, staff actions, satisfaction, abandonment, payment and victory/defeat conditions.

### Stage 2 — Customer Behaviour Extension

Adds VIP and Critic customers alongside regular customers, introducing polymorphic satisfaction decay rates, VIP discounts and distinct Critic order or service behaviour within the existing turn flow.

## Changes from Stage 1

- Enabled VIPCustomer and CriticCustomer, keeping selection, waiting-loss and discount behaviour in customer subclasses.
- Added getCustomerType() to Customer and its subclasses for display labels.
- Updated Main to register the mixed arrival schedule, and GameCLI to show customer types and correctly format order details.
- Reused the existing staff, table, pricing and turn-flow implementations. The Stage 2 runs exercised the shared preparation, serving and payment flow with the new customer types.
