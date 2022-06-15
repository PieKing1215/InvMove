#!/bin/bash

# Automatically runs the buildAll task for every mc version and collects the jars into build/
# That task builds the jars for all of its platforms and copies them into the version's build/
# If the build fails, it retries a few times
# Once it succeeds this script copies the jars into the root build/
# (trying to do all of this through gradle was using an absurd amount of memory)

versions=("1.16" "1.18" "1.19")

start=$(date +%s)
end=$(date +%s)

rm ./build/*
mkdir ./build/

for i in "${versions[@]}"
do

  MAX_TRIES=3
  COUNT=0
  while [ $COUNT -lt $MAX_TRIES ]; do
    echo "Building $i..."
    TERM=cygwin ./gradlew --stop
    TERM=cygwin timeout 7m ./gradlew mc"$i":buildAll -PdisableAllBut=mc"$i"
    if [ $? -eq 0 ];then
      break;
    fi
    echo "Failed building $i, trying again..."
    let COUNT=COUNT+1
  done

  if [ $COUNT -eq $MAX_TRIES ]; then
    echo "Too many failures building $i, giving up..."
    exit 1;
  fi

  cp ./mc"$i"/build/*.jar ./build/
done

end=$(date +%s)

echo "Finished in $((end - start)) seconds!"
