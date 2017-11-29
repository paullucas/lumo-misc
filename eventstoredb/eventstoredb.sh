#!/bin/sh

set -e

lumo -K -c src -m "eventstoredb.core" $@
