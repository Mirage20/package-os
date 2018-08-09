import ballerina/io;

documentation {
    The command object represents an executing process and provide methods to perform IO operation with the process.
}
public type Command object {

    @readonly public string command;
    @readonly public string workingDir;
    @readonly public string[] envp;

    documentation {
        Constructs a new Command.

        P{{command}} String representation of the command.
        P{{workingDir}} Working directory of this command. If not specified the command will executed in current working directory.
        P{{env}} Environment variables in <key>=<value> format.
    }
    public new(command, workingDir = "",string[]? env=()) {
        envp = env but {
            () =>  []
        };
        init();
    }

    extern function init();

    documentation {
        Wait until this command is finished.

        R{{}} Exit code of the process or an `error` if failed.
    }
    public extern function waitFor() returns int |error;

    documentation {
        Returns the ByteChannel connected to the stderr of this command.

        R{{}} A read only ByteChannel representing stderr of the process or 
        an `error` if failed to create a channel.
    }
    public extern function getStderrPipe() returns io:ByteChannel|error;

    documentation {
        Returns the ByteChannel connected to the stdout of this command.

        R{{}} A read only ByteChannel representing stdout of the process or 
        an `error` if failed to create a channel.
    }
    public extern function getStdoutPipe() returns io:ByteChannel|error;

    documentation {
        Returns the ByteChannel connected to the stdin of this command.

        R{{}} A write only ByteChannel representing stdin of the process or 
        an `error` if failed to create a channel.
    }
    public extern function getStdinPipe() returns io:ByteChannel|error;

    documentation {
        Kill the process specified by this command.

        R{{}} An `error` if failed to kill the process.
    }
    public extern function kill() returns error?;
};

documentation {
    Create pipes between given commands.

    P{{commands}} List of commands that need to be piped.
    R{{}} A touple indicating the output and exit code of the last command or an `error` if failed to pipe.
}
public extern function pipe(Command... commands) returns (byte[], int)|error;
