#!/bin/bash

# Check if the correct number of arguments is provided
if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <file-path>"
    exit 1
fi

FILE_PATH=$1

DST_IP="192.168.178.69"
FTP_USER="vince"
# Prompt the user for FTP details
read -sp "Enter FTP password: " FTP_PASS
echo

# Check if the file exists
if [ ! -f "$FILE_PATH" ]; then
    echo "Error: File not found!"
    exit 1
fi

# Use ftp to send the file
ftp -inv $DST_IP <<EOF
user $FTP_USER $FTP_PASS
binary
put "$FILE_PATH"
bye
EOF

echo "File transfer completed."

