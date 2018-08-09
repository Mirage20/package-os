

documentation {
    Change the current working directory for the `os` package operations.

    P{{dir}} Absolute or relative directory path.
    R{{}} An `error` if failed to change the directory.
}
public extern function cd(string dir) returns error?;

documentation {
    Retrieve the current working directory.

    R{{}} The working directory.
}
public extern function pwd() returns string;

documentation {
    Creates the directory with given name.

    P{{dir}} directory name.
    P{{createParentDirs}} Indocates whether the `mkdir` should create non-exsisting parent directories.
    R{{}} An `error` if failed to create a directory.
}
public extern function mkdir(string dir, boolean createParentDirs = false) returns error?;

documentation {
    Remove a directory or file with given name.

    P{{file}} directory or file name.
    P{{recursive}} Indocates whether the `rm` should recursively remove all the file inside the given directory.
    R{{}} An `error` if failed to remove.
}
public extern function rm(string file, boolean recursive = false) returns error?;

documentation {
    Returns the files and directories in the directory current working directory.

    R{{}} An array of files and directories or an `error` if failed.
}
public extern function ls() returns string[] |error;

documentation {
    Move or rename a file or directory.

    P{{src}} source file name.
    P{{dest}} destination file name.
    R{{}} An `error` if failed to move the file.
}
public extern function mv(string src, string dest) returns error?;

documentation {
    Copy a file to destination.

    P{{src}} source file name.
    P{{dest}} destination file name.
    R{{}} An `error` if failed to copy the file.
}
public extern function cp(string src, string dest) returns error?;

//public extern function chmod(string file, string permissions) returns error?;

