## Package Overview

Run OS system commands.

This package provides the functionality to invoke system commands and do the file operations.

## Installation Guide


1. Run `ballerina pull mirage/os`
2. 

### Commands and working directory

In this package, all of the functions use a separate working directory which is independent from the BVM working directory.
To change the working directory use, 
        
    `os:cd("dir")`

## Samples

### Operating with directories

```ballerina
import ballerina/io;
import mirage/os;

function main(string... args) {
    // Print the current working directory
    io:println(os:pwd());

    // Create a directory called 'foo' and cd into it.
    error? err = os:mkdir("foo");
    err = os:cd("foo");
    io:println(os:pwd());

    // Create couple of directories and remove.
    err = os:mkdir("aaa/bbb/ccc/ddd",createParentDirs = true);
    err = os:rm("aaa",recursive = true);
    
    // Go back to the parent directory
    err = os:cd("..");

    // List the files
    match os:ls() {
        string[] result => {
            io:println(result);
        }
        error e => {
            io:println(e);
        }
    }
}
```
### Playing with workers

Current working directory is local to a worker.

```ballerina
import ballerina/io;
import mirage/os;

function main(string... args) {    
    worker w1 {
        io:println(os:pwd());
        error? err = os:mkdir("foo");
        err = os:cd("foo");
        io:println(os:pwd());
        
    }
    worker w2 {
        io:println(os:pwd());
        error? err = os:mkdir("bar");
        err = os:cd("bar");
        io:println(os:pwd());
    }
}
```
### Interacting with system commands

```ballerina
import ballerina/io;
import mirage/os;

function main(string... args) {
    string[] env = ["FOO=X","BAR=Y"];
    os:Command command = new("grep ballerina", workingDir = ".", env = env);
  
    // Write to stdin
    var stdin = command.getStdinPipe();
    io:ByteChannel inp = check stdin;
    io:CharacterChannel inCharChannel = new io:CharacterChannel(inp, "utf-8");
    var ignore = inCharChannel.write("ballerina\njava\ngo\n", 0);
    var err = inCharChannel.close();
    
    // Read from stdout
    var stdout = command.getStdoutPipe();
    io:ByteChannel outp = check stdout;
    io:CharacterChannel outCharChannel = new io:CharacterChannel(outp, "utf-8");
    io:println(outCharChannel.read(200));
           
}
```
### Pipe system command into another

```ballerina
import ballerina/io;
import ballerina/mime;
import mirage/os;

function main(string... args) {
    match os:pipe(new("ls -al"), new("grep ballerina"), new("wc -l")) {
        (byte[], int) result => {
            io:println(mime:byteArrayToString(result[0], "utf-8"));
        }
        error err => {
            io:println(err);
        }
    }
}
```
