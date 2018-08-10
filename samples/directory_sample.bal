import ballerina/io;
import mirage/os;

function main(string... args) {
    io:println("Current working directory: " + os:pwd());
    
    io:println("Creating a new directory 'test'");
    check os:mkdir("test");
    
    io:println("Change the working directory in to 'test'");
    check os:cd("test");

    io:println("Current working directory: " + os:pwd());

    io:println("Creating few directories recursively 'aaa/bbb/ccc/ddd'...");
    check os:mkdir("aaa/bbb/ccc/ddd", createParentDirs = true);

    io:println("Listing files...");
    io:println(check os:ls());
   
    io:println("Removing newly created directories...");
    check os:rm("aaa", recursive = true);

    io:println("Going back to parent directory");
    check os:cd("..");

    io:println("Current working directory: " + os:pwd());

    io:println("Removing 'test' directory");
    check os:rm("test");

    io:println("Listing files...");
    io:println(check os:ls());
}
