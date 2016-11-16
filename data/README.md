This directory contains:

- some data-massaging scripts
- 2 files full of scores
- a blacklist of artificial faults that shouldn't be analyzed
- a specification of how many passing/failing tests each fault has.



Scoring Files
-------------

- `scores_artificial_vs_real.csv.gz`

    Contains scores for prior-work techniques, run on real and artificial faults:

    + 4 debugging scenarios: first, last, mean, median
    + 7 FLTs: Tarantula, Ochiai, Barinel, Op2, DStar, Metallaxis, MUSE
    + all real/artificial faults with 32h-timeout killmaps, provided there is a corresponding artificial/real fault with a 32h-timeout killmaps.

    We use these scores for sections 3-4.


- `scores_real_exploration.csv.gz`

    Contains scores for all the techniques mentioned in the paper, run on real faults:

    + 4 debugging scenarios: first, last, mean, median
    + all FLTs: the whole design space, plus our experimental techniques
    + all real faults for which we could generate killmaps, imposing no timeout


Blacklist
---------

This blacklist file lists the artificial faults that have any of the following problems:

1. it won't compile
2. the tests time out
3. no tests detect it
4. a test-class that fails rather than just a test

Ideally, these problems would be detected by Major when it's generating artificial faults, and the problematic faults would be discarded; but Major runs the test suites all at once, while we run them one-at-a-time, so due to test-dependency issues, some of these problems slip by.

(At the time of writing, issues 1-3 are indicated by an empty or one-line `trigger_tests` file in D4J; issue 4 is indicated by a line in the `trigger_tests` file naming a whole *class* that fails rather than a method.)
