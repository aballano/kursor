# Kursor [![Build Status](https://travis-ci.org/aballano/kursor.svg?branch=master)](https://travis-ci.org/aballano/kursor)[![](https://jitpack.io/v/aballano/kursor.svg)](https://jitpack.io/#aballano/kursor)[![codecov](https://codecov.io/gh/aballano/kursor/branch/master/graph/badge.svg)](https://codecov.io/gh/aballano/kursor)

Extension functions for Android's Cursor object.

```kotlin
val kursor: Cursor = // usersDb.query(...)

kursor.forEach { row -> print(it + " ") } 

// Output: 
// 1 Daenerys Targaryen 2 John Snow
```

It is also possible to just get a row iterator:
 
```kotlin
kursor.rows() //...
```

The `toFormattedString()` function returns a table-formatted string:

```kotlin
print(kursor.toFormattedString())
```

Output:

```text
+-----------+-----------+-----------+
| ID        | NAME      | SURNAME   |
+-----------+-----------+-----------+
| 1         | Daenerys  | Targaryen |
| 2         | John      | Snow      |
+-----------+-----------+-----------+
```

The table format can be slightly adjusted:
```kotlin
// Default values:
print(kursor.toFormattedString(
        // Shows column names
        includeColumnNames = true, 
        // Column with. Less than 1 means auto-fit 
        // (note that there's a small performance penalty when enabled)
        columnWidth = 0, 
        // Adds an extra separator between rows
        rowsSeparator = false
))
```

#### Note that since DB tables usually hold more than one type, the returned value types of foreach will be `Any?` so casting is needed in order to use the values.


Because of that you can also access by position by index, assuming that the value is not null:
```kotlin
// by specifying the type
val id = kursor[0] intAt 1           // 2 (type is Int)
val name = kursor[1] stringAt 1      // "John" (type is String)

// OR with automatic casting by explicitly specifying variable's type
val id: Int = kursor[1] getAs 1      // 2
val name: String = kursor[1] getAs 1 // "John"

// OR with typical cast
val id = kursor[1][1] as Int         // 2
val name = kursor[1][1] as String    // "John"
```

Note that values can be null, in that case you should use `nullableIntAt` and it's alternatives or use smart-casting to 
nullable types.

Download
---

Just replace 'x.x.x' with the version in the Jitpack badge on the top.

With gradle: edit your build.gradle
```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}

dependencies {
        compile 'com.github.aballano:kursor:x.x.x'
}
```

Or declare it into your pom.xml

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.aballano</groupId>
    <artifactId>kursor</artifactId>
    <version>x.x.x</version>
</dependency>
```
