#!/bin/bash
mkdir coverage
docker run --rm -v `pwd`/coverage:/coverage-out  ece651 scripts/test.sh
