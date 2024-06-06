#!/bin/bash
# ./ftp-upload.sh dir filename targetFilename


# uploads file to linux hosting pc via ftp in binary mode
# dont specify path of file, but instead the dir and filename
# need to interactively type ftp pw

if [ "$#" -ne 3 ]; then
    echo "Usage: $0 <dir> <filename> <targetFilename>"
    exit 1
fi

DIR="$1"
FILE_NAME="$2"
TARGET_FILENAME="$3"
DST_IP="192.168.178.69"
FTP_USER="subtitle-buddy"
# Prompt the user for FTP details
read -sp "Enter FTP password: " FTP_PASS
echo

FILE_PATH="$DIR/$FILE_NAME"

echo "dir: $DIR"
echo "filename: $FILE_NAME"
echo "filepath: $FILE_PATH"

# Check if the file exists
if [ ! -f $FILE_PATH ]; then
    echo "Error: File not found!"
    exit 1
fi

# Use ftp to send the file
ftp -inv $DST_IP <<EOF
user $FTP_USER $FTP_PASS
cd projects/server
lcd "$DIR"
binary
put "$FILE_NAME" "$TARGET_FILENAME"
bye
EOF

echo "File transfer completed."

