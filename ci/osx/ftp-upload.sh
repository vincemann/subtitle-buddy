#!/bin/bash

# Check if the correct number of arguments is provided
if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <dir> <filename>"
    exit 1
fi

DIR=$1
FILE_NAME=$2
DST_IP="192.168.178.69"
FTP_USER="vince"
# Prompt the user for FTP details
read -sp "Enter FTP password: " FTP_PASS
echo

echo "dir: $DIR"
echo "filename: $FILE_NAME"
# Check if the file exists
if [ ! -f "$DIR/$FILE_NAME" ]; then
    echo "Error: File not found!"
    exit 1
fi

# Use ftp to send the file
ftp -inv $DST_IP <<EOF
user $FTP_USER $FTP_PASS
cd "$DIR"
binary
put "$FILE_NAME"
bye
EOF

echo "File transfer completed."
