# advent-2021

My solutions to the [Advent of Code 2021](https://adventofcode.com/2021).

## Commentary

My language of choice, as for 2020, is Clojure, which I am running in Doom Emacs. 
Clojure is a Lisp, it has immutable data, is very functional, fast, has an excellent set of
built-in and external functions and libraries, and the full Java ecosystem is always there if you need it. 

I've brought my little toolbox of simple utils from 2020 along to save some time. 
I also expect to need some graphs, combinatorics, parsing, and matrix manipulation along the way, so I have the `ubergraph`, `math.combinatorics`, `instaparse`, and `core.matrix` libraries respectively on standby.

### Day 1

A little warm-up: calculate a simple difference vector and count the positive
entries in part 1, and then aggregate instead with a moving window in part 2.
For the windowing we have the `partition` function.

### Day 2

We implement a little state machine to track our position according to a list of instructions.
Once we've parsed the input the transitions themselves are very basic in part 1, and fractionally less so in part 2. 
As my little challenge, I've crammed in a couple more lambda functions rather than creating separate definitions.

### Day 3

This is a problem where an array language like J or APL would have been more elegant than Clojure in filtering rows in a matrix based on most or least common bit values in columns etc. but my J skills are nowhere near up to the task. 
That's something for a long rainy day to crank through.
Here, part 1 was pretty trivial, but part 2 involved some more complicated logic and some sequential filtering to get the answer.
However, like in Day 2, I got to use my favourite `map` and `reduce`; a day without using a `for` loop is a good day.
I refactored my solution a little at the end to make it more attractive but it's still a little clunkier than I'd like it. 

### Day 4

Sometimes in these puzzles you can see a shortcut to solving the problem, in others you just have to grind out the solution. This is one of those. 
Part 1 is pretty tough, or at least the way I did it. The game of bingo is easy enough, but the scoring calculation requires keeping track of a bunch more information that became painful. Also, finding if there is a filled row or column doesn't lend itself to a nifty one-liner. Maybe I just picked the wrong data representation.

I'm really close to the answer in part 2. I can get down to the finding the second-last winning board, but haven't quite got the final one. There is some subtle error in my code that is holding me back. I am putting this one on hold, and maybe I'll come back to it.

### Day 5

Finding overlapping points between different line segments in 2D integer space is a nice little puzzle once you've parsed the input into endpoints. 
I could also leverage some code I used in Advent 2019 to solve Part 1.

For dealing with diagonal lines in part 2, I chose to rewrite the core function with a brute-force approach that although simpler, takes longer. But it gets the answer, even if you have to wait.

### Day 6

At a first pass, this looks simple enough, and part 1 certainly is. Just seed a vector from the input file, and update it against the rules for the given number of days, and count the number of items at the end. Easy. Then you get to part 2 and realise that this function is a slow exponential, and while the number of elements at 80 days is reasonable, the values at 256 days will blow your modest RAM on the _test_ set. After some thinking you realise that there is a better way: you don't need to keep the whole vector, you just need to keep the _counts_ of each value. So I refactored my initial solution to the new approach, and the part 2 answer popped out in a couple of milliseconds on my electronic abacus.

### Day 7

Both parts of this are easy: we're looking for the minimum of a vector of calculated numbers. In part 1, it's a simple linear function, in part 2 it's over a sum to n, which has a simple closed form solution. Because this is so easy, the next day will be hard.

### Day 8

And it is. Part 1 is fine, it's a straightforward exercise to calculate some frequencies, extract the ones we need, and count the elements. Part 2 is the problem. Intuitively, this seems like an [exact cover](https://en.wikipedia.org/wiki/Exact_cover) problem but I'm struggling to map the problem to that representation, and then use Knuth's Algorithm X to solve it. 

I can think of an alternative brute force approach that finds which permutation of segment swaps gives the output when multiplied by the original map but that will take a while. This is another one I'm going to pass on, and maybe come back to later on.

### Day 9

The obvious way to solve part 1 is to brute force a search over each cell, comparing it with its neighbours while handling the edges, and that's indeed what we did here. It duly returns the local minima across the matrix.

For part 2 we need to map out the size of the basins that encircle each local minima. This is equivalent to a flood fill of an area, and a quick search of flood fill algorithms confirms that recursion is a reasonable way to do this for a matrix of this size. Because I'm working with pure functions and immutable data, we need to pass some state up and down the recursion stack, to keep track of what we've already visited and counted. A little complicated, but I'm pleased with the result in 100 lines of functional code.

### Day 10

We get to wheel out the parser for this one, so I reach for `instaparse`. Once we define the input grammar, we can find the errors in the part 1 strings easily, although it does require some thought to confirm what an "incomplete" line is. Luckily, the parser gives us a clue.

Part 2 is not quite as easy because the parser won't fix your errors for you. So we incrementally find the next expected character, append it to the string, and try parsing it again until it passes. Then we work out what we appended and score it.

### Day 11

Here we have to deal with flashing octopuses, but luckily a numeric matrix is a sufficient model. This one is about iterating until a fixed point is reached, but there are three nested iterations involved, which, because I'm being functional, involves three reductions (or folds) that maintain state. Part 1 took me a while to work through the nested loops and the logic. In constrast, part 2 only took a few additional lines of code to get the answer. This is one of the few occasions that the second part is relatively simple compared to the first.

## License

Copyright © 2021 Andrew Joyner

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
