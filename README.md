# libhalodb

[![LICENSE](https://img.shields.io/github/license/rinx/libhalodb)](https://github.com/rinx/libhalodb/blob/master/LICENSE)

This repository shows how to call [yahoo/HaloDB](https://github.com/yahoo/HaloDB) API through [rinx/clj-halodb](https://github.com/rinx/clj-halodb) as a shared library built using GraalVM native-image.

Build
---

GraalVM 20.1.0 Java11 and `native-image` are required.

    $ make target/native/libhalodb.so

Examples
---

- CPP: examples/cpp
- Go: examples/go


Reference
---

- [Libsci](https://github.com/borkdude/sci/blob/master/doc/libsci.md)
