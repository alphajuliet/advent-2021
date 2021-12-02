# advent-2021

My solutions to the [Advent of Code 2021](https://adventofcode.com/2021).

## Commentary

My language of choice, as for 2020, is Clojure, which I am running in Doom Emacs. 
Clojure is a Lisp, it has immutable data, is very functional, fast, has an excellent set of
built-in and external functions and libraries, and the full Java ecosystem is always there if you need it. 
I've also brought my little toolbox of simple utils from 2020 along to save some time.

### Day 1

A little warm-up: calculate a simple difference vector and count the positive
entries in part 1, and then aggregate instead with a moving window in part 2.
For the windowing we have the `partition` function.

### Day 2

We implement a little state machine to track our position according to a list of instructions.
Once we've parsed the input the transitions themselves are very basic in part 1, and fractionally less so in part 2. 
As my little challenge, I've crammed in a couple more lambda functions rather than creating separate definitions.

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
