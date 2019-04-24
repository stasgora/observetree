[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.stasgora/observetree/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.stasgora/observetree)

# Observetree library
Library extends the classic _Observable pattern_ by integrating it in a tree structure. _Change events_ are propagated through the Observable tree. _Listeners_ can be assigned a priority to control the order at which they are called.

## Getting started

### Documentation
Javadoc is available at [stasgora.github.io/observetree/](https://stasgora.github.io/observetree/)

For detailed technical description see [Observable class](https://stasgora.github.io/observetree/com/github/stasgora/observetree/Observable.html)

### Installation
#### Maven

```xml
  <dependency>
    <groupId>io.github.stasgora</groupId>
    <artifactId>observetree</artifactId>
    <version>${observetree-version}</version>
  </dependency>
```
#### Manual
See the [release page](https://github.com/stasgora/observetree/releases) for _jar_ downloads. Follow your IDE instructions for detailed steps on adding external library dependencies to your project.

### Basic usage
- Create model class extending ```Observable```:
```java
class Point extends Observable {
  public int x, y;
}
```
- Internally call ```onValueChanged()```:
```java
private void set(int x, int y) {
  this.x = x;
  this.y = y;
  onValueChanged();
}
```
- Create instance and register a listener:
```java
Point p = new Point();
p.addListener(() -> {...});
```
- Change model object and allow it to call it's listeners:
```java
p.set(1, 1);
p.notifyListeners();
```
