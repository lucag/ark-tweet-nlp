#!/bin/bash

set -eu
BASE=$(dirname $0)/ark-tweet-nlp/target
VERSION="0.3.3"

# Run the tagger (and tokenizer).
java -ea -XX:ParallelGCThreads=2 -Xmx2G \
     -jar ${BASE}/ark-tweet-nlp-${VERSION}-with-dependencies.jar "$@"
