import ballerina/io;
import ballerina/internal;
import wso2/os;

function main(string... args) {
    // Pipe between basic Linux commands
    match os:pipe(new("ls -al"), new("grep ballerina"), new("wc -l")) {
        (byte[], int) result => {
            io:println(internal:byteArrayToString(result[0], "UTF-8"));
        }
        error e => {
            io:println(e);
        }
    }
}
