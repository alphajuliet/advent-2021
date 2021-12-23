(defproject advent-2021 "0.1.0-SNAPSHOT"
  :description "Advent of Code 2021"
  :url "http://alphajuliet.com/ns/advent-2021/"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.clojure/math.combinatorics "0.1.6"]
                 [org.clojure/core.match "1.0.0"]
                 [instaparse "1.4.10"]
                 [com.rpl/specter "1.1.3"]
                 [meander/epsilon "0.0.650"]
                 [net.mikera/core.matrix "0.62.0"]
                 [ubergraph "0.8.2"]]
  :repl-options {:init-ns advent-2021.util})
