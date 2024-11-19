<h2 align="center">
    <picture>
        <img src="assets/logo.png" width="35%"/>
    </picture>
</h2>

# Table of Contents
1. [About](#about)
2. [Feature Showcase](#feature-showcase)
3. [Libraries](#libraries)
4. [Todo](#todo)

# About
Pola is an interpreted object-oriented programming language written in Kotlin created for learning purposes. Examples can be found in the [examples directory](./examples).

# Feature Showcase
Note: Examples marked with a "*" assume that some libraries have been imported.

## Comments
```
# This is a comment
```

## Imports
```
import("io") # An instance of the class IO
import("array") # A reference to the class Arrays
val println = io.println # A reference to the function io.println
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
val bool = true
```

## Objects *
```
val str = "string"
val arr = array(0, 1)
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

val num = 5
val isNumEven = isEven(x) # false

val numArray = array(1, 2, 3, 4)
val evenNumArray = numArray.filter(isEven) # [2.0, 4.0]
```

### Anonymous Functions *
```
val x = array(0, 1, 2, 3)
val xPlusOne = x.map(fun (value) {
    return ++value
}) # [1.0, 2.0, 3.0, 4.0]
```

## Classes *
```
class Product {
    init(name, brand, price) {
        this.name = name
        this.brand = brand
        this.price = price
    }
    
    toString() {
        return "The " + this.name + " from " + this.brand + " costs $" + this.price + "."
    }
}

class MottsAppleSauce inherits Product {
    init() {
        super.init("Applesauce", "Mott's", 7)
    }
}

val applesauce = MottsAppleSauce() # Instance of MottsAppleSauce
val coke = Product("Coke", "Coca Cola", 2) # Instance of Product
```

# Libraries
## IO
| Name    | Returns | Parameters  | Arity    | Comments                             |
|---------|---------|-------------|----------|--------------------------------------|
| readln  | string  | n/a         | 0        |                                      |
| print   | string  | textToPrint | infinite | Does not include a newline.          |
| println | string  | textToPrint | infinite | Includes a newline.                  |
| file    | file    | fileName    | 1        | Does not perform any error checking. |

## Sys
| Name | Returns | Parameters | Arity | Comments |
|------|---------|------------|-------|----------|
| exit | n/a     | value      | 1     |          |

## Array
| Name    | Returns | Parameters      | Arity | Comments                                                                                                                          |
|---------|---------|-----------------|-------|-----------------------------------------------------------------------------------------------------------------------------------|
| get     | any     | index           | 2     |                                                                                                                                   |
| set     | array   | index, newValue | 3     |                                                                                                                                   |
| remove  | array   | index           | 2     |                                                                                                                                   |
| filter  | array   | filteringFunc   | 2     | filteringFunc will be passed each item of the array and should return true for values to keep and false for values to discard.    |
| foreach | n/a     | applicationFunc | 2     | applicationFunc will be passed each item of the array and optionally the index of the item, returned values will be discarded.    |
| map     | array   | applicationFunc | 2     | applicationFunc will be passed each item of the array and optionally the index of the item and should return a replacement value. |

## String
| Name    | Returns | Parameters         | Arity | Comments                                                                                                                                     |
|---------|---------|--------------------|-------|----------------------------------------------------------------------------------------------------------------------------------------------|
| get     | string  | index              | 2     |                                                                                                                                              |
| set     | string  | index, newValue    | 3     |                                                                                                                                              |
| remove  | string  | index              | 2     |                                                                                                                                              |
| filter  | string  | filteringFunc      | 2     | filteringFunc will be passed each character of the string and should return true for values to keep and false for values to discard.         |
| foreach | n/a     | applicationFunc    | 2     | applicationFunc will be passed each character of the string and optionally the index of the character, returned values will be discarded.    |
| map     | string  | applicationFunc    | 2     | applicationFunc will be passed each character of the string and optionally the index of the character and should return a replacement value. |
| replace | string  | replacee, replacer | 3     | All occurrences of replacee in the string will be replaced with replacer.                                                                    |

## File
| Name   | Returns | Parameters | Arity | Comments                                                 |
|--------|---------|------------|-------|----------------------------------------------------------|
| read   | string  | n/a        | 1     |                                                          |
| isFile | bool    | n/a        | 1     |                                                          |
| exists | bool    | n/a        | 1     |                                                          |
| delete | bool    | n/a        | 1     | This will likely be changed to return the File instance. |
| create | bool    | n/a        | 1     | See above                                                |
| write  | bool    | text       | 2     | See above                                                |

## Errors
| Name  | Returns                            | Parameters         | Arity | Comments                                                                              |
|-------|------------------------------------|--------------------|-------|---------------------------------------------------------------------------------------|
| try   | The result of tryFunc or catchFunc | tryFunc, catchFunc | 2     | Catches all errors. catchFunc is called with the error message thrown during tryFunc. |
| throw | n/a                                | message            | 1     | Throws an Error instance.                                                             |

# Todo
- Create a Javadoc
- Improve README documentation
- Remove primitives
