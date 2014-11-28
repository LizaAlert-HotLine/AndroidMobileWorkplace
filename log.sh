cat ~/ll |  sed 's/..AndroidRuntime([0-9]*)://g' | retrace.sh `find . -name mapping.txt` 
