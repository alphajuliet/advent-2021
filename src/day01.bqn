# day01

util ← •Import "util.bqn"

# input ← "../data/day01-test.txt"
input ← "../data/day01-input.txt"

# Read in the numbers, take the differences, and count the positive ones.
part1 ← {
  a ← +`⁼ util.ReadNumbers input
  b ← +´(0⊸<)¨ 1↓a
}

# Read in the numbers, partition into overlapping groups of 3, add the rows,
# take the differences, and count the positive ones.
part2 ← {
  a ← 3↕ util.ReadNumbers input
  b ← (+`⁼) ∘ (+´˘) a
  c ← +´(0⊸<)¨ 1↓b
} 

•Show part1
•Show part2
