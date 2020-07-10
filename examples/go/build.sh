#!/bin/sh

go build -o call_from_go --ldflags "-s -w -linkmode 'external'"
