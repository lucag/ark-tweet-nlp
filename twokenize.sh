#!/bin/bash

# Only run the tokenizer.

set -eu
BASE=$(dirname $0)/ark-tweet-nlp/target
VERSION="0.3.3"

java -XX:ParallelGCThreads=2 -Xmx100m \
     -jar ${BASE}/ark-tweet-nlp-${VERSION}-with-dependencies.jar \
     --just-tokenize "$@"
