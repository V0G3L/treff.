#!/bin/bash

fswatch --event=Updated $1 2> /dev/null | xargs -n 1 --no-run-if-empty plantuml;
