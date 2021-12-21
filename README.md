# advent-2021

My solutions to the [Advent of Code 2021](https://adventofcode.com/2021).

## Intro

My language of choice, as for 2020, is Clojure, which I am running in Doom Emacs. 
Clojure is a Lisp, it has immutable data, is very functional, fast, has an excellent set of
built-in and external functions and libraries, and the full Java ecosystem is always there if you need it. 

I've brought my little toolbox of simple utils from 2020 along to save some time. 
I also expect to need some graphs, combinatorics, parsing, and matrix manipulation along the way, so I have the `ubergraph`, `math.combinatorics`, `instaparse`, and `core.matrix` libraries respectively on standby.

## Day 1

A little warm-up: calculate a simple difference vector and count the positive
entries in part 1, and then aggregate instead with a moving window in part 2.
For the windowing we can use the nifty Clojure `partition` function.

## Day 2

We implement a little state machine to track our position according to a list of instructions.
Once we've parsed the input the transitions themselves are very basic in part 1, and fractionally less so in part 2. 
As my little challenge, I've crammed in a couple more lambda functions rather than creating separate definitions.

## Day 3

This is a problem where an array language like J or APL would have been more elegant than Clojure in filtering rows in a matrix based on most or least common bit values in columns etc. but my J skills are nowhere near up to the task. 
That's something for a long rainy day to crank through.
Here, part 1 was pretty trivial, but part 2 involved some more complicated logic and some sequential filtering to get the answer.
However, like in Day 2, I got to use my favourite `map` and `reduce`; a day without using a `for` loop is a good day.
I refactored my solution a little at the end to make it more attractive but it's still a little clunkier than I'd like it. 

## Day 4

Sometimes in these puzzles you can see a shortcut to solving the problem, in others you just have to grind out the solution. This is one of those. 
Part 1 is pretty tough, or at least the way I did it. The game of bingo is easy enough, but the scoring calculation requires keeping track of a bunch more information that became painful. 
Also, finding if there is a filled row or column doesn't lend itself to a nifty one-liner. Maybe I just picked the wrong data representation.

I'm really close to the answer in part 2. 
I can get down to the finding the second-last winning board, but haven't quite got the final one. 
There is some subtle error in my code that is holding me back. 
I am putting this one on hold, and maybe I'll come back to it.

## Day 5

Finding overlapping points between different line segments in 2D integer space is a nice little puzzle once you've parsed the input into endpoints. 
I could also leverage some code I used in Advent 2019 to solve Part 1.

For dealing with diagonal lines in part 2, I chose to rewrite the core function with a brute-force approach that although simpler, takes longer. But it gets the answer, even if you have to wait.

## Day 6

At a first pass, this looks simple enough, and part 1 certainly is. Just seed a vector from the input file, and update it against the rules for the given number of days, and count the number of items at the end. Easy. Then you get to part 2 and realise that this function is a slow exponential, and while the number of elements at 80 days is reasonable, the values at 256 days will blow your modest RAM on the _test_ set. After some thinking you realise that there is a better way: you don't need to keep the whole vector, you just need to keep the _counts_ of each value. So I refactored my initial solution to the new approach, and the part 2 answer popped out in a couple of milliseconds on my electronic abacus.

## Day 7

Both parts of this are easy: we're looking for the minimum of a vector of calculated numbers. In part 1, it's a simple linear function, in part 2 it's over a sum to n, which has a simple closed form solution. Because this is so easy, the next day will be hard.

## Day 8

And it is. Part 1 is fine, it's a straightforward exercise to calculate some frequencies, extract the ones we need, and count the elements. Part 2 is the problem. Intuitively, this seems like an [exact cover](https://en.wikipedia.org/wiki/Exact_cover) problem but I'm struggling to map the problem to that representation, and then use Knuth's Algorithm X to solve it. 

I can think of an alternative brute force approach that finds which permutation of segment swaps gives the output when multiplied by the original map but that will take a while. This is another one I'm going to pass on, and maybe come back to later on.

## Day 9

The obvious way to solve part 1 is to brute force a search over each cell, comparing it with its neighbours while handling the edges, and that's indeed what we did here. It duly returns the local minima across the matrix.

For part 2 we need to map out the size of the basins that encircle each local minima. This is equivalent to a flood fill of an area, and a quick search of flood fill algorithms confirms that recursion is a reasonable way to do this for a matrix of this size. Because I'm working with pure functions and immutable data, we need to pass some state up and down the recursion stack, to keep track of what we've already visited and counted. A little complicated, but I'm pleased with the result in 100 lines of functional code.

## Day 10

We get to wheel out the parser for this one, so I reach for [instaparse](https://github.com/engelberg/instaparse). Once we define the input grammar, we can find the errors in the part 1 strings easily, although it does require some thought to confirm what an "incomplete" line is. Luckily, the parser gives us a clue.

Part 2 is not quite as easy because the parser won't fix your errors for you. So we incrementally find the next expected character, append it to the string, and try parsing it again until it passes. Then we work out what we appended and score it.
It's not super elegant but again, it works.

## Day 11

Here we have to deal with flashing octopuses, but luckily a numeric matrix is a sufficient model. 
This one is about iterating until a fixed point is reached, but there are three nested iterations involved, which, because I'm being functional, involves three reductions (or folds) that maintain state. 
Part 1 took me a while to work through the nested loops and the logic. 
In constrast, part 2 only took a few additional lines of code to get the answer. 
This is one of the few occasions that the second part is relatively simple compared to the first.

## Day 12

This is into graph network territory, so of course we call on [ubergraph](https://github.com/Engelberg/ubergraph). 
I was hoping to use one of the path algorithms but unfortunately an appropriate one isn't available, so I need to develop my own to find all the paths from a source to destination node, but with the given constraint of being able to visit an upper-case node more than once. 
Luckily, I had some working Racket code for `find-all-paths` that I could migrate to Clojure, and add the constraint.

For part 2, it's not enough to have a binary decision on whether we've visited a cave before; we need to maintain a count, and use that to decide on if we're allowed to visit it again. We then add a series of rules to determine whether we can add the node.

## Day 13

This is an interesting one: mapping points across several folds in the plane. I thought this might get harder, but it was straightforward. The challenge was to find an expression that maps a point to a new point from the action of a fold. This is captured in the `fold` function. Reading in the input data is a bit fiddly but once you have the points and folds, you run all the points through a single fold and remove the duplicates. 

For part 2, we need to run the points through all the folds, which is trivial, and then visualise the resulting array of points. We mess about with making a matrix and turning it into strings, so that we can see the resulting answer code.
This one was quite fun.

## Day 14

This is an exercise is string generation by rules: the opposite of parsing. In part 1, we can expand the string by partitiong the string into overlapping pairs and inserting a new characater according to the rule set.

In part 2, we hit the curse of exponential growth again, like on Day 6. What works for 10 iterations will easily blow your memory for 40.
Like in Day 6, the clue is in the question: we need to calculate the difference in counts, which means we need to find a way of not holding the whole string at each step. We can do this by maintaining a map of counts of each pair, and counting the new generated pairs. 
At the end we need to extract the answer from the counts with a bit of data munging. It's a large number but it pops out pretty quickly.
I ended up refactoring my part 1 solution for part 2 using the new approach, but it still gives the right answer for both.

## Day 15

I've not really done this puzzle justice, because search is challenging topic and I naturally shy away from it. 
So, once I worked out this was a maze solving problem, and that the A* algorithm is a good solution, I lifted a solution from "The Joy of Clojure", which got me the answer to part 1 quickly. 
Somewhat worryingly, it took a little time to calculate it, so that when part 2 came along with a 25-times larger search space, I knew I'd have to find a better way. 
The trouble is that I don't pretend to understand the A* algorithm, so I'm struggling to tailor it to the problem at hand. I also can't get the right answer using the test data for part 2, so I'm already in trouble.
In the interests of moving on, I'm leaving this one "for later". That means I may or may not come back to it, but this one is not so fun.

## Day 16

This puzzle provides another parsing opportunity, so we reach again for [instaparse](https://github.com/engelberg/instaparse). 
We create a grammar for the binary stream of characters from the instructions and after some parsing and quick and dirty manipulations we can grab all the version numbers and add them up for the answer to part 1.

However, there is a lurking problem in here in part 2, and that is the matter of the variable number of subpackets. 
There is a length field that tells you how many to expect, and ignoring that messes with the tokenisation of at least one of the test data cases. 
This is not a matter of precedence, you actually need that length to be able to correctly bracket the following literals.
Now, that is outside of the power of ENBF in the general case, and so would require inserting some custom logic in the parsing.  However, `instaparse`, as far as I can tell, won't let me do that, and I'm not inclined to create a parser from scratch. That's a shame, because `instaparse` has a lovely little function called `transform` that lets you walk the output tree and evaluate it from the bottom up to get the answer. 
My code works on most test cases, but not all, and certainly not on the input data. 
I'll sit this one out. 
That's two puzzles in a row that I haven't finished; they are certainly getting harder.

## Day 17

This one is harder than it looks. It requires some modelling of the movement of an object in a space with gravity and air resistance.
The test data here is crucial to making sure you've included all the initial velocity combinations, but there is still some trial-and-error involved to tweak the edge conditions.
For part 1, we need to consider launching the probe up in the air and checking if it lands on the target at an integer time. This requires working out the bounding box of velocities that you need to search through because the total space is too large. Finding the maximum is easy after that.

Part 2 required a lot of trial and error. 
It came down to determining that there are three zones or bounding boxes for the velocities that can hit the target: 

1. Direct shots on the target at high velocity 
2. Slower ones at a horizontal or slightly downward velocity
3. Upwards (like in part 1) that will drop vertically on the target

Defining these bounding boxes takes some work, and a bit of experimentation to see whether growing them slightly increases the number of successful points (yes, it does). 
In the end the total stopped increasing, and so we got there. 
It's good to get another star for a part 2.

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
