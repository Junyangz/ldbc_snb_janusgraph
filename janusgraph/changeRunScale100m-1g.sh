#!/bin/bash

###########
if [ "$1" = "1g" ] 
then
	sed -i 's/test-data-100m/test-data-1g/g' import-to-janusgraph.sh
	sed -i 's/test-data-100m/test-data-1g/g' ldbc_snb_interactive.properties
	echo "Change to 1g scale done!"

elif [ "$1" = "100m" ] 
then
	sed -i 's/test-data-1g/test-data-100m/g' import-to-janusgraph.sh
	sed -i 's/test-data-1g/test-data-100m/g' ldbc_snb_interactive.properties
	echo "Change to 100m run scale done!"
else
	echo "usage: input \$1 of (1g|100m)"
fi
