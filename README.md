# Groovy_ContentSearch.FilePath.v1
Search directory path for file and replace the original text with new text if it's found. 
This program will create a back up copy and create a new log each time a new search/groovy is invoked.

directory: The path to the directory containing files.
original_text: The original text or pattern to use for searching in the files from the given directory or subdirectories.
new_text: A new text string which will replace the original text or pattern if found in the copy file as many times as it was found in the directory, then close the file.


For example:
location or path of where the program is: Search.groovy, file is located in C:\Users\JaneDoe\Documents\GroovyScripts,and you want to search for all files containing the word “hello” in C:\Users\JaneDoe\Documents\Files, replace <directory> with C:\Users\JaneDoe\Documents\Files, <original_text> with hello,
<new_text> with world,

Invoking from the command line should look like this:
groovy C:\Users\JaneDoe\IdeaProject\GroovyScripts\Search.groovy C:\Users\JaneDoe\Documents\Files hello world

This will run the script and output a list of modified files to C:\Users\JaneDoe\Documents\ModifiedFiles.txt.
The log file will be saved to C:\Users\JaneDoe\Documents\searchlog.txt.
