# Sandbox for Scala Functions
* `object Main` is a runner
* `object Challenges` are functions to solve code challenges
* `Challenge Spec` contains unit tests against challenges

## Quickstart
Docker with Makefile
* `make build` - create Docker image
* `make test` - runs `sbt test`

See `Makefile` for additional commands.

# Cheatsheet
Common scala "gotchas".
## Collections
`import scala.collection.immutable._`
* Use `List` for arrays and sequences
* Use `Map` for quick objects

Map and List can be upserted with `updated`
* Map -> updated(key, value)
* List -> updated(possition, value)

Modify list:
* Concatenate list with `++`
* Add to end of list with `appended`

### Fold Left
FoldLeft(Accumulator Start Value) { (accumulator:Type, currentValue)
* Accumulator has a defined type and start value
* current value matches type from inside the folded collection
  * e.g. `l.FoldLeft("") { (acc:String, c) => { func } }`
  
### Slice & Sub-Collections
slice(firstIndex, lastIndex)
* returns sub-collection between first and last index
* similar:
  * `take` - returns first n elements
  * `drop` - removes first n elements
  * `head` - returns first element
  * `tail` - returns everything after the first element
  * `last` - returns the last element
  