# Pflichtenheft

Pflichtenheft template

## Interface

**Criteria:**
```
\criterion{name}{hidden_id}{numeric_id}
\criterionOpt{name}{hidden_id}{numeric_id}
\criterionDemarc{name}{hidden_id}{numeric_id}
Starts a new required/optional/demarcation criterion.

name        = Displayed name
hidden_id   = Alphanumeric ID used for referencing via \fulfills and \critLink
            Must be unique over all criteria
numeric_id  = Numeric ID displayed with prefix on the right

\critLink{criterion_hidden_id}
Creates a clickable reference to the specified criterion, displaying its
numeric ID with prefix.
```
Required and optional criteria will list the functional requirements that
fulfill (`\fulfills`) them or display a red warning text should there be none.

**Functional and non-functional requirements:**
```
\functionality{name}{hidden_id}{numeric_id}
\functionalityOpt{name}{hidden_id}{numeric_id}
\nonFunctionality{name}{hidden_id}{numeric_id}
Starts a new required/optional functional or non-functional requirement.

name        = Displayed name
hidden_id   = Alphanumeric ID used for referencing via \tests and \fncLink
              Must be unique over all functional and non-functional
              requirements
numeric_id  = Numeric ID displayed with prefix on the right

\fulfills{criterion_hidden_id}
Notes that the current requirement implements criterion specified by the first
argument. This will add the requirement to the criterion's list and vice versa.

\fncLink{functionality_hidden_id}
\nfcLink{nonfunctionality_hidden_id}
Creates a clickable reference to the specified functionality/non-functionality,
displaying its numeric ID with prefix.
```
Functional requirements will list the criteria that they implement.
Required functional requirements will also list the tests that test (`\tests`)
them or display a red warning text should there be none.

**Tests:**
```
\test{name}{hidden_id}{numeric_id}
Starts a new test.

name        = Displayed name
hidden_id   = Alphanumeric ID used for referencing via \testlink
              Must be unique over all tests
numeric_id  = Numeric ID displayed with prefix on the right

\testStep{state}{action}{reaction}
Adds a step to the current test.

state       = Description of the current state of the application
action      = Description of what the user is doing (input)
reaction    = Description of how the application changes as consequence of the
              action (output)

\tests{functionality_hidden_id}
Notes that the current test tests the functionality specified by the first
argument. This will add the test to the functionality's list and vice versa.

\testLink{test_hidden_id}
\nfcLink{nonfunctionality_hidden_id}
Creates a clickable reference to the specified test,
displaying its numeric ID with prefix.

```
