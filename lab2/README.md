# Laboratory Work 2

The program:

1. creates a `Person` object with a last name, first name, and age;
2. serializes it to JSON using Jackson;
3. deserializes a `Person` from JSON;
4. compares the original and deserialized objects using `equals`.

The `Person` class also implements `hashCode`, because equal objects must have
the same hash code. The `equals` and `hashCode` contract is verified with the
EqualsVerifier library.

## Running

JDK 17 and Maven are required.

Run the tests:

```shell
mvn test
```

Run the program:

```shell
mvn compile exec:java
```
