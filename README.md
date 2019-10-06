[![Maven Central](https://img.shields.io/maven-central/v/io.github.stasgora/observetree.svg)](https://search.maven.org/search?q=g:%22io.github.stasgora%22%20AND%20a:%22observetree%22)
[![Build Status](https://travis-ci.org/stasgora/observetree.svg?branch=master)](https://travis-ci.org/stasgora/observetree)
[![Last Release Date](https://img.shields.io/github/release-date/stasgora/observetree?color=orange)](https://github.com/stasgora/observetree/releases)
[![Sonar Tests](https://img.shields.io/sonar/tests/io.github.stasgora:observetree?compact_message&server=https%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/dashboard?id=io.github.stasgora%3Aobservetree)
[![Code Coverage](https://img.shields.io/coveralls/github/stasgora/observetree)](https://coveralls.io/github/stasgora/observetree?branch=master)
[![License](https://img.shields.io/github/license/stasgora/observetree?color=blueviolet)](https://github.com/stasgora/observetree/blob/master/LICENSE)
[![Sonar Quality Gate](https://img.shields.io/sonar/quality_gate/io.github.stasgora:observetree?server=https%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/dashboard?id=io.github.stasgora%3Aobservetree)

# Observetree library
Observetree is a library that extends the classic _Observable pattern_ by integrating it in a tree structure. _Change events_ are propagated through the Observable tree. _Listeners_ can be assigned a priority to control the order at which they are called.

## Getting started

### Documentation [![Javadocs](https://javadoc.io/badge/io.github.stasgora/observetree.svg)](https://javadoc.io/doc/io.github.stasgora/observetree)
Javadoc also available at [sgora.dev/observetree/](https://sgora.dev/observetree/)

For detailed technical description see [Observable class](https://sgora.dev/observetree/com/github/stasgora/observetree/Observable.html)

### Installation
#### Maven [![Maven Central](https://img.shields.io/maven-central/v/io.github.stasgora/observetree.svg)](https://search.maven.org/search?q=g:%22io.github.stasgora%22%20AND%20a:%22observetree%22)
```xml
<dependency>
  <groupId>io.github.stasgora</groupId>
  <artifactId>observetree</artifactId>
  <version>${observetree-version}</version>
</dependency>
```
SNAPSHOT versions are **not** synchronized to Central. If you want to use a snapshot version you need to add the https://oss.sonatype.org/content/repositories/snapshots/ repository to your pom.xml.
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

### Advanced functions
#### Using _Observable_ trees
- Build model structure:
```java
class Model extends Observable {
  public Point point;
  ...
  public Model() {
    addSubObservable(point);
    ...
  }
}
```
- You can subscribe to changes of objects on different levels:
```java
model.addListener(...); // Changes to model and all it's sub-observables
model.point.addListener(...);
```
- After changes to potentially many objects notify listeners of all changed objects in tree:
```java
model.notifyListeners();
```
(For detailed behaviour description see the [documentation](https://stasgora.github.io/observetree/com/github/stasgora/observetree/Observable.html))
#### Specifying listener priority
```java
p.addListener(() -> {...}, ListenerPriority.HIGH);
```
#### Creating _Settables_ out of external objects
- Declare:
```java
SettableProperty<Integer> size = new SettableProperty<>(1);
```
- Get notified when the value is set:
```java
size.set(2);
```
#### Using _Settable Observables_ with persistent listeners
- Declare:
```java
SettableObservable<Point> point = new SettableObservable<>(new Point());
```
- Register static listener:
```java
point.addStaticListener(() -> {...});
```
- After replacing the object the listeners will remain attached:
```java
point.set(new Point());
```

### Author
Stanisław Góra

### License
[MIT License](http://www.opensource.org/licenses/mit-license.php)
