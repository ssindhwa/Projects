#!/bin/bash

# Check if binary and lib.txt are provided
if [[ $# -lt 3 ]]; then
    echo "Usage: $0 <binary> <lib.txt> <output_file>"
    exit 1
fi

# Input arguments
BINARY=$1
LIBC_FUNCTIONS=$2
OUTPUT_FILE=$3

# Ensure the binary exists
if [[ ! -f $BINARY ]]; then
    echo "Error: Binary file '$BINARY' not found!"
    exit 1
fi

# Ensure the lib.txt file exists
if [[ ! -f $LIBC_FUNCTIONS ]]; then
    echo "Error: libc functions file '$LIBC_FUNCTIONS' not found!"
    exit 1
fi

# Run nm on the binary, filter and clean the output, and store in the output file
nm -D "$BINARY" | grep -Ff "$LIBC_FUNCTIONS" | sed 's/@.*//' | awk '{print $NF}' > "$OUTPUT_FILE"

# Notify the user
echo "Filtered libc function names saved to '$OUTPUT_FILE'"
