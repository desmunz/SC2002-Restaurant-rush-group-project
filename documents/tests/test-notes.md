# Stage 2 Test Notes

In this document, we discuss the strategies involved in reaching both Victory and Defeat endings, and also the verification of these results.

## Victory Strategy

To reproduce the victory, follow this schedule. Numbers in the action descriptions are customer IDs for seating/ordering and order IDs for serving/preparation.

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

[Replay input](victory-input.txt)

## Defeat Strategy

For all 18 turns, choose Waiter 4 and Chef 2.

[Replay input](defeat-input.txt)

## Results

| Replay | Result | Served | Unhappy departures | Paid | Revenue | Average paid satisfaction | Turns |
| --- | --- | --- | --- | --- | --- | --- | --- |
| Victory | VICTORY | 5 | 0 | 5 | $76.50 | 79.6 | 18 |
| Defeat | DEFEAT | 0 | 2 | 0 | $0.00 | N/A | 18 |

In the victory case, the Critic orders Pasta, requiring two preparation turns. Customer 6 has a READY order but is not served or paid by the end. These results depend on the documented arrival schedule, menu order and random seeds.

In the defeat case, two customers abandon; later arrivals have not all reached zero satisfaction by turn 18.

The results given only cover the stated scenarios, not every possible action sequence.

## Input and reporting checks

- Both replays printed exactly 18 end-of-turn summaries, with final statistics before the result.
- Nonnumeric action input, out-of-range actions, nonpositive IDs and nonexistent customer IDs were rejected and the game continued.
- EOF at the action prompt, ID prompt, Chef prompt and after invalid input exited with no stack trace and no victory/defeat declaration.
- Reproduction for immediate EOF in PowerShell: `@() | java -cp out restaurantrush.Main`. Expected message: `Input ended. Game stopped before completion.`
