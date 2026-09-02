# Laboratory Work 1

## Program description

The program receives one line containing words separated by spaces. It first
selects words that contain only Latin alphabet characters (`A-Z` and `a-z`).
Among those words, it selects the ones that contain an equal number of vowels
and consonants.

The Latin vowels are `a`, `e`, `i`, `o`, and `u`. Letter case does not matter.
The result is returned by the `findWords` method as a `String[]` and printed by
the program in array form.

Words containing digits, punctuation, hyphens, accented letters, or other
non-Latin characters are ignored.

## Test cases

Run the program separately for each test case.

### Test 1: Several matching words

Input:

```text
hello world java code
```

Expected output:

```text
[java, code]
```

### Test 2: No matching words

Input:

```text
cat dog sun apple
```

Expected output:

```text
[]
```

### Test 3: Uppercase words

Input:

```text
BOOK TREE SKY
```

Expected output:

```text
[BOOK, TREE]
```

### Test 4: Longer words

Input:

```text
banana tomato potato
```

Expected output:

```text
[banana, tomato, potato]
```

### Test 5: Two-letter words

Input:

```text
ab ba xy Aq
```

Expected output:

```text
[ab, ba, Aq]
```

### Test 6: Digits and punctuation

Input:

```text
code123 hello! test-case Java
```

Expected output:

```text
[Java]
```

### Test 7: Accented letters

Input:

```text
café naïve code
```

Expected output:

```text
[code]
```

### Test 8: Single-letter words

Input:

```text
a b e x
```

Expected output:

```text
[]
```

### Test 9: Mixed data

Input:

```text
test code cat dog apple hello123 Java banana BOOK tree sky ab café
```

Expected output:

```text
[code, Java, banana, BOOK, tree, ab]
```
