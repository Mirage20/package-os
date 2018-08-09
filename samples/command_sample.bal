import ballerina/io;
import wso2/os;

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