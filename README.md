# hmod

Communicated-concept incubator.

## Installation

Add the following to your `project.clj` `:dependencies`:

  ```clojure
  [hmod "1.0.0"]
  ```    

## Usage

  ```clojure
  (defn tconcepts [] (hmod.core/retrieve-concepts "concept-directory"))
  (hmod.java-out/java-out "output-java-src-directory" "package-name" (tconcepts))
  ```

## License

Copyright © 2017 Transcendental Programming <transcendental.programming@yandex.com>

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
