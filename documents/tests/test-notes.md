# Stage 1 Test Notes

In this document, we discuss the strategies involved in reaching both Victory and Defeat endings, and also the verification of these results.

## Victory Strategy

For customer n from 1 to 6, use this three-turn pattern:

1. Turn 3n - 2: Waiter 1, customer ID n; Chef 2.
2. Turn 3n - 1: Waiter 2, customer ID n; Chef 1, order ID n.
3. Turn 3n: Waiter 3, order ID n; Chef 2.

Final result: VICTORY, six customers served, zero unhappy departures, five paid customers, $75.00 revenue, average paid satisfaction 84.0, 18 turns used. Customer 6 is served but unpaid at the end. Paid customers remain included in the served count.

[Replay input](victory-input.txt)

## Defeat Strategy

For all 18 turns, choose Waiter 4 and Chef 2.

Final result: DEFEAT, zero served, two unhappy departures, zero paid, $0.00 revenue, average N/A and 18 turns used. Later arrivals have not all reached zero satisfaction by turn 18.

[Replay input](defeat-input.txt)

## Input and reporting checks

- Both replays printed exactly 18 end-of-turn summaries, with final statistics before the result.
- Nonnumeric action input, out-of-range actions, nonpositive IDs and nonexistent customer IDs were rejected and the game continued.
- EOF at the action prompt, ID prompt, Chef prompt and after invalid input exited with no stack trace and no victory/defeat declaration.
- Reproduction for immediate EOF in PowerShell: `@() | java -cp out restaurantrush.Main`. Expected message: `Input ended. Game stopped before completion.`
