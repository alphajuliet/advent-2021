# BQN utilities

⟨StringToInt, ReadNumbers⟩⇐

StringToInt ← 10⊸×⊸+˜´∘⌽-⟜'0'
ReadNumbers ← StringToInt¨ ∘ •file.Lines

# The End
