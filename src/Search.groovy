/*<directory>: The path to the directory containing files.
<original_text>: The original text or pattern to use for searching in the files from the given directory or subdirectories.
<new_text>: A new text string which will replace the original text or pattern if found in the copy file as many times as it was found in the directory, then close the file.

For example, if Search.groovy file is located in C:\Users\JaneDoe\Documents\GroovyScripts,
and you want to search for all files containing the word “hello” in C:\Users\JaneDoe\Documents\Files,
replace <directory> with C:\Users\JaneDoe\Documents\Files,
<original_text> with hello,
<new_text> with world,


Invoking from the command line should look like this:
groovy C:\Users\JaneDoe\Documents\GroovyScripts\Search.groovy C:\Users\JaneDoe\Documents\Files hello world 

This will run the script and output a list of modified files to C:\Users\JaneDoe\Documents\ModifiedFiles.txt.
The log file will be saved to C:\Users\JaneDoe\Documents\Log.txt.
*/

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.logging.Logger

class Search {
    static void main(String[] args) {
     //checks to make sure that the required arguments (directory, originalText, newText) are provided.
        // Check if the required arguments (directory, original_text, and new_text) are provided.
        if (args.length != 3) {
            println "Usage: groovy Search.groovy <directory> <original_text> <new_text>"
            return
        }

        def directory = args[0]
        def originalText = args[1]
        def newText = args[2]

        // Create a log file. Create the full path to the log file.
        def logFileName = "searchlog.txt"
        def logFilePath = Paths.get(directory, logFileName)
        def logFileWriter = new FileWriter(logFilePath.toFile())

        def logger = Logger.getLogger("SearchAndReplace")
        logger.info("Search started.")

        // Log the search start time.
        def startTime = System.currentTimeMillis()
        logFileWriter.write("Search start time: ${new Date(startTime)}\n")

        def files = []

        /*creates a list of all files in the directory and its subdirectories.
         the if (file.isFile()) check ensures that only files are processed, not directories.*/
        new File(directory).eachFileRecurse { file ->
            if (file.isFile()) {
                files << file
            }
        }
    //It iterates over the list of files. It tries to read the file contents.
        def modifiedFiles = []
        def errorFiles = []

        // Check if the output file exists. If it does, prompt the user to confirm whether they want to overwrite the file.
        /*def overwritePrompt = """
            The file (%s) already exists. Do you want to overwrite it? (Y/N)
        """

        def checkOverwrite(filePath) {
            if (Files.exists(Paths.get(filePath))) {
                print overwritePrompt.formatted(filePath)

                def userInput = System.in.readLine()
                if (userInput != "Y" && userInput != "y") {
                    return false
                }
            }
        }
        */


        files.each { file ->
            /*
            The try-catch block is used to handle any errors that may occur while processing the files.
            If an error occurs, the file path is added to the errorFiles list.
            The backupFile variable is used to store a backup copy of the original file before any modifications are done.
            The replaceAll method is used to replace all occurrences of the original text or pattern with the new text string.
            */
            try {
                def content = file.text

                // If the file contents contain the original text string
                  if (content.contains(originalText)) {
                    def backupFile = Paths.get(file.parent, "${file.name}.backup") //creates a backup copy of the file
                    Files.copy(file.toPath(), backupFile, StandardCopyOption.REPLACE_EXISTING) //replaces all occurrences of the original text string with the new text string.

                      // Automatically name the modified file.
                      def modifiedFile = Paths.get(file.parent, "${file.name}.new")
                      content = content.replaceAll(originalText, newText)
                    Files.write(file.toPath(), content.getBytes()) //writes the modified file contents back to the file.
                    modifiedFiles << file.path
                      // Log the modified file.
                      logFileWriter.write("Modified file: ${modifiedFile.path}\n")
                }
            } catch (Exception e) {
                errorFiles << file.path

                // Log the error file.
                logFileWriter.write("Error file: ${file.path}\n")
                logFileWriter.write(e.toString() + "\n")
                logger.warning("Error processing file: ${file.path}")
            }
        }

        // Create a new file to list the modified files.
        def modifiedFilesList = Paths.get(directory, "modified_files.txt")
        def modifiedFilesListFile = new FileWriter(modifiedFilesList.toFile())

        // Write the modified files to the new file.
        modifiedFiles.each { modifiedFile ->
            modifiedFilesListFile.write(modifiedFile + "\n")
        }

        // Close the new file.
        modifiedFilesListFile.close()

        // Log the modified files list file.
        logFileWriter.write("Modified files list file: ${modifiedFilesList.path}\n")

        def endTime = System.currentTimeMillis()
        logFileWriter.write("Search end time: ${new Date(endTime)}\n")


/*
The modified and error files are logged in the log file specified by the user.
The list of modified files is also written to an output file specified by the user.
*/

        def logContent = """
            Search start time: ${new Date(startTime)}
            Search end time: ${new Date(endTime)}
            Original text or pattern: $originalText
            New text string: $newText
            Modified files:
            ${modifiedFiles.join('\n')}
            Error files:
            ${errorFiles.join('\n')}
        """
        Files.write(logFilePath, logContent.getBytes())


        // Close the log file.
        logFileWriter.close()
        println "Log file has been written and closed."
        logger.info("Search complete. Modified files: ${modifiedFiles.size()}. Error files: ${errorFiles.size()}.")
    }
}
