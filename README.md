# Table of Contents
1. [About](#about)
2. [Feature Showcase](#feature-showcase)
3. [Libraries](#libraries)

# About
Pola is an interpreted object-oriented programming language written in Kotlin created for learning purposes.

# Feature Showcase
Note: Examples marked with a "*" assume that `import Standard as std` has been declared.

## Comments
`# This is a comment`

## Imports
```
import Standard as std
val array = std.array
val println = std.println
```

## Math
```
var x = 10
x = x + 10 # 20
x = x - 10 # 10
x = x / 2 # 5
x = x * 2 # 10
x = x % 2 # 0
x = ++x # 1
x = --x # 0
```

## Variables
```
var mutable = "This is mutable."
val immutable = "This is immutable."
```

## Primitives
```
val num = 0
val string = ""
val bool = true
```

## Objects *
```
val string = std.string("")
val array = std.array(0, 1)
```

## Control Flow
```
if(true) {
}

while(true) {
}

for(i = 0; i < 10; i = ++i) {
}
```

## First-Class Functions *
```
fun isEven(value) {
    return value % 2 == 0
}

val x = 5
val isXEven = isEven(x) # false

val y = std.array(1, 2, 3, 4)
val evenY = y.filter(isEven) # [2.0, 4.0]
```

## Anonymous Functions *
```
val x = std.array(0, 1, 2, 3)
val xPlusOne = x.map(fun (value) {
    return ++value
}) # [1.0, 2.0, 3.0, 4.0]
```

## Classes *
```
class Person {
    init(name, age) {
        this.name = name
        this.age = age
    }
    
    toString() {
        return std.string(this.name + " is " + this.age + " years old.")
    }
}

class JimmyCarter inherits Person {
    init() {
        super.init("Jimmy Carter", 100)
    }
}

val jimmyCarter = JimmyCarter() # Instance of JimmyCarter
```

# Libraries

## Standard
| Name    | Returns      | Parameters  | Arity    | Comments                    |
|---------|--------------|-------------|----------|-----------------------------|
| exit    | n/a          | exitValue   | 1        |                             |
| print   | string (obj) | textToPrint | infinite | Does not include a newline. |
| println | string (obj) | textToPrint | infinite | Includes a newline.         |
| array   | array        | values      | infinite | See [Arrays](#arrays)       |
| string  | string (obj) | text        | 1        | See [Strings](#strings)     |

### Arrays
| Name    | Returns | Parameters      | Arity | Comments                                                                                                                          |
|---------|---------|-----------------|-------|-----------------------------------------------------------------------------------------------------------------------------------|
| get     | any     | index           | 2     |                                                                                                                                   |
| set     | array   | index, newValue | 3     |                                                                                                                                   |
| remove  | array   | index           | 2     |                                                                                                                                   |
| filter  | array   | filteringFunc   | 2     | filteringFunc will be passed each item of the array and should return true for values to keep and false for values to discard.    |
| foreach | n/a     | applicationFunc | 2     | applicationFunc will be passed each item of the array and optionally the index of the item, returned values will be discarded.    |
| map     | array   | applicationFunc | 2     | applicationFunc will be passed each item of the array and optionally the index of the item and should return a replacement value. |

### Strings
| Name    | Returns | Parameters         | Arity | Comments                                                                                                                                     |
|---------|---------|--------------------|-------|----------------------------------------------------------------------------------------------------------------------------------------------|
| get     | string  | index              | 2     |                                                                                                                                              |
| set     | string  | index, newValue    | 3     |                                                                                                                                              |
| remove  | string  | index              | 2     |                                                                                                                                              |
| filter  | string  | filteringFunc      | 2     | filteringFunc will be passed each character of the string and should return true for values to keep and false for values to discard.         |
| foreach | n/a     | applicationFunc    | 2     | applicationFunc will be passed each character of the string and optionally the index of the character, returned values will be discarded.    |
| map     | string  | applicationFunc    | 2     | applicationFunc will be passed each character of the string and optionally the index of the character and should return a replacement value. |
| replace | string  | replacee, replacer | 3     | All occurrences of replacee in the string will be replaced with replacer.                                                                    |