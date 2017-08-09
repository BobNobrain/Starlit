#!/bin/bash

# http://stackoverflow.com/questions/59895/getting-the-source-directory-of-a-bash-script-from-within
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

cd $DIR

if ! [ -d lib ]; then
    mkdir lib
fi

cd lib

echo ":: Downloading Frontend Libraries ::"

deps=("https://raw.githubusercontent.com/eligrey/FileSaver.js/master/FileSaver.min.js" \
      "https://code.jquery.com/jquery-3.2.1.min.js" \
)

if [[ "$1." == "--force." ]]; then
    rm ".gitignore" 2> /dev/null
fi

for dep in ${deps[@]}; do
    fname=$(echo "$dep" | grep -oE '[^/]+$')
    if [[ "$1." == "--force." ]]; then
        rm "$fname" 2> /dev/null
    fi
    if ! [[ -e "$fname" ]]; then
        echo "Loading: " "$fname"
        wget "$dep"
        echo "$fname" >> ./.gitignore
    else
        echo "$fname" "already exists"
    fi
done

cd ..

# now it's time to compile java and form json
echo " "
echo ":: Forming Actual Data from Java Code ::"

if ! [ -d generated ]; then
    mkdir generated
fi

# copy fonts so they will be accessible for page
echo " "
echo ":: Copying Stuff ::"
cp "../../app/src/main/assets/fonts/symbols.ttf" "generated/symbols.ttf"
cp "../../app/src/main/assets/fonts/jura.ttf"    "generated/jura.ttf"
