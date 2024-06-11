#!/bin/bash
# ./ftp-upload.sh sshhost dir filename targetFilename
# sshhost example: 'user@192.168.178.42'


# uploads file to linux hosting pc via ftp in binary mode
# dont specify path of file, but instead the dir and filename
# need to interactively type ftp pw

if [ "$#" -ne 3 ]; then
    echo "Usage: $0 <dir> <filename> <targetFilename>"
    exit 1
fi

SSH_HOST="$1"
DIR="$2"
FILE_NAME="$3"
TARGET_FILENAME="$4"

FILE_PATH="$DIR/$FILE_NAME"

echo "dir: $DIR"
echo "filename: $FILE_NAME"
echo "filepath: $FILE_PATH"

# Check if the file exists
if [ ! -f "$FILE_PATH" ]; then
    echo "Error: File not found!"
    exit 1
fi

# Use sftp to send the file
sftp "$SSH_HOST" <<EOF
cd projects/server
pwd
ls
lcd $DIR
lls
put $FILE_NAME $TARGET_FILENAME
bye
EOF

echo "File transfer completed."

