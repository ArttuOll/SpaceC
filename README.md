# SpaceC
An interpreted C-like programming language

***

# Specification

This document is updated as the implementation progresses.

## Comments

```jsx
// This is a comment
/*
	This 
	is
	a
	block
	comment
*/
```

## Typing

Dynamically typed - errors are detected at runtime. Simpler to implement than static typing.

## Memory management

Carbage collection will be provided by the Java-runtime.

## Datatypes

### Booleans

```jsx
true
false
```

### Numbers

Double-precision floating point

```jsx
1234 // Integer
12.34 // Decimal
```

### Strings

```jsx
"This is a string"
"" // Empty string
```

### Nil

```jsx
nil // no value
```

## Expressions

### Arithmetic

```jsx
plus 1 1 // 2
substract 1 1 // 0
divide 1 2 // 0.5
multiply 1 2 // 2

-1
-aNumber // number negation
```

```jsx
1 + 1 // 2
1 - 1 // 0
1 / 2 // 0.5
1 * 2 // 2
3 * (1 + 2) // 9

-1
-aNumber // number negation
```

### String concatenation

```jsx
concat "Hello" "World"
```

## Comparison

```jsx
1 = 1 // true
1 >= 0 // true
1 <= 2 // true
1 != 2 // true
1 = "1" // false
```

### Logical

`and`- and `or`- operators are short-circuiting, like in JavaScript. `and` returns the left operand if it is false and doesn't even evaluate the right one. `or` returns the left operand if it is true and similarly doesn't evaluate the right one.

```jsx
not true // false
not false // true

false and true // false
false or true // true
```

## Statements

```jsx
print "Hello world!"; // print statement (for debugging)
```

## Variables

```jsx
numberOfLines: 5;
lines;

print numberOfLines // 5
print lines // nil
```

Variables can be assigned only once. They work exactly like `const` works in JavaScript.

```jsx
numberOfLines: 5;
numberOfLines: 2; // Error
```

## Control flow

### If-statements

```jsx
if (condition) {
	print "yes";
} else {
	print "no";
}
```

## while-loop

```jsx
while (condition) {
	print "yes";
}
```

## foreach-loop

```jsx
foreach (item, array) {
	print item;
}
```

## Functions

```jsx
printUserNames(names);
plus(1, 1);

function printUserNames(names) {
	foreach (name, names) {
		print name;
	}
}

function plus(operand1, operand2) {
	return operand1 + operand2;
}

// Closure is implemented
function returnFunction() {
  outside: "outside";

  function inner() {
    print outside;
  }

  return inner;
}

var fn = returnFunction();
fn(); // "outside"
```

## Classes and Objects

```jsx
class Breakfast {
	constructor(meat, bread) {
			meat, bread: this;
  }

  cook() {
    print "Eggs a-fryin'!";
  }

  serve(who) {
    print "Enjoy your breakfast, " + who + ".";
  }
}

var breakfast = Breakfast("bacon", "buns");
print breakfast; // "Breakfast instance".

breakfast.yoghurt = "yoghurt";

class Brunch inherits Breakfast {
	constructor(meat, bread, time) {
		super(meat, bread);
		time: this;
  }
  drink() {
    print "How about a Bloody Mary?";
  }
}
```

Classes follow the JavaScript syntax, but their instantiation is similar to Python. Properties can be freely added to objects. `this` can be used to refer to the current object inside of methods.
