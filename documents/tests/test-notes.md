# Stage 3 Test Notes

In this document, we discuss the strategies involved in reaching both Victory and Defeat endings, and also the verification of these results.

## Victory Strategy

Start a fresh game. Skip Happy Hour on turns 1–3 and activate it on turn 4. In the table, **Seat** and **Take order** use customer IDs. **Serve** and **Prepare** use order IDs. A dash means choose the staff member’s **Pass** option.

| Turn | Host | Waiter | Chef |
| ---: | --- | --- | --- |
| 1 | Seat 1 | Take order 1 | Prepare 1 |
| 2 | — | Serve 1 | — |
| 3 | — | — | — |
| 4 | Seat 2 | Take order 2 | Prepare 2 |
| 5 | — | — | Prepare 2 |
| 6 | — | — | Prepare 2 |
| 7 | Seat 3 | Serve 2 | — |
| 8 | — | Take order 3 | Prepare 3 |
| 9 | — | Serve 3 | — |
| 10 | Seat 4 | Take order 4 | Prepare 4 |
| 11 | — | — | Prepare 4 |
| 12 | — | — | Prepare 4 |
| 13 | Seat 5 | Serve 4 | — |
| 14 | — | Take order 5 | Prepare 5 |
| 15 | — | Serve 5 | — |
| 16 | Seat 6 | Take order 6 | Prepare 6 |
| 17 | — | Serve 6 | — |
| 18 | — | — | — |

Turn 4 demonstrates price locking in both directions. The Cashier collects **$15.00** for customer 1’s Burger, ordered before Happy Hour. On that same turn, the VIP orders Salad + Pasta for **$17.50**. The Chef finishes the combo on turn 6, the Waiter serves it on turn 7, and the VIP pays on turn 9 after Happy Hour has expired. Customer 6 is served on turn 17 and becomes ready to pay on turn 18, which accounts for six served but only five paid.

[Replay input](victory-input.txt)

## Defeat Strategy

On every turn, choose **2** to skip Happy Hour, **2** for the Host to pass, **4** for the Waiter to pass, and **2** for the Chef to pass. Two customers reach zero satisfaction. After turn 18, revenue is $0.00 and average paid satisfaction is `N/A`.

[Replay input](defeat-input.txt)

## Results

| Replay | Result | Served | Unhappy departures | Paid | Revenue | Average paid satisfaction | Turns |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: |
| Victory | VICTORY | 6 | 0 | 5 | $86.80 | 82.8 | 18 |
| Defeat | DEFEAT | 0 | 2 | 0 | $0.00 | N/A | 18 |


## Input and reporting checks

- Both replays printed exactly 18 end-of-turn summaries, with final statistics before the result.
- Nonnumeric action input, out-of-range actions, nonpositive IDs and nonexistent customer IDs were rejected and the game continued.
- EOF at the action prompt, ID prompt, Chef prompt and after invalid input exited with no stack trace and no victory/defeat declaration.
- Reproduction for immediate EOF in PowerShell: `@() | java -cp build/classes restaurantrush.Main`. Expected message: `Input ended. Game stopped before completion.`
