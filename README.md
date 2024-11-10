# ScheCPUEmulator

URL: https://github.com/schemil053/ScheCPUEmulator

## Languages
- [English](#english)
- German (Coming soon)

## English

## Table of contents
1. [Introduction](#introduction)
2. [Project Goals](#project-goals)
3. [Emulator Design](#emulator-design)
   - [CPU Architecture](#cpu-architecture)
   - [Instruction Set](#instruction-set)
   - [Registers](#registers)
   - [Memory Management](#memory-management)
4. [Usage](#usage)
5. [Examples](#examples)
6. [Future Improvements](#future-improvements)

## Introduction
This project is a Java-based CPU emulator that aims to simulate fundamental CPU operations for educational purposes. The emulator is designed to simplify understanding of how CPUs function, making it accessible and easy to learn for those new to computing.

This emulator runs in a "locked-down" mode: programs are loaded at compile-time, meaning the memory and instructions cannot be modified at runtime. This limitation allows beginners to focus on core CPU mechanics without additional complexity.


## Project Goals
- Teach the Fundamentals of CPU Design: The primary purpose of this emulator is to help users understand how a CPU operates.
- Learning Through a Simple, Understandable Architecture
- Introduction to Machine Language and Assembly Concepts
- Provide an Interactive Learning Experience

## Emulator Design
## CPU Architecture
**1. Word Size**:

- The CPU in this emulator uses an 32-bit word size. That's because an integer in java is normally an 32-bit sized object (4 bytes). This means, the max number of memory addresses, memory values or register values is 2^31-1 (2147483647).

**2. Registers**
- The CPU includes a set of registers that act as small, fast storage locations used to hold data and instructions during execution.

**3. Memory**

- The emulator uses a fixed-size memory block where data is stored. The CPU can access this memory directly through addresses.
- A value at the memory is a 32-bit integer. 2^31-1 is the maximum limit and -2^31 the minimum limit.
- This setup avoids complexities like memory segmentation and paging, focusing on direct memory access.

**4. Instruction Set Architecture (ISA)**
- The CPU operates on a basic set of instructions (e.g., MOV, ADD, SUB, JMP) that control data movement, arithmetic, logic operations, and program flow.


### Instruction Set
- You can find the complete instruction table in the file [src/main/resources/Instructions.md](src/main/resources/Instructions.md)

### Registers
- The CPU has 5 registers:
   - A, B, C, D: currently unused (highlang uses them, but I want to add more functions that rely on them in the future)
   - BOOL: The bool register stores the result of the last operation

### Memory management
- The memory management in this emulator is simplified but includes a distinct separation between program instructions and data. 
- This setup allows users to utilize the entire memory for data storage without the risk of modifying program instructions during runtime, making it easier to experiment with memory operations.
- A value at the memory is a 32-bit integer. 2^31-1 is the maximum limit and -2^31 the minimum limit (Java integer limit).

## Usage
There are a few ways to import this project to your project.
Here is a list of recommended ways:

- [Jitpack](#jitpack)
- [Push to you own repository](#push-to-you-own-repository)
- [Install locally](#install-locally)



### Adding as dependency
#### Jitpack
1. Get the latest version at jitpack: [jitpack.io/#schemil053/ScheCPUEmulator/](https://jitpack.io/#schemil053/ScheCPUEmulator/)
2. Add the jitpack repository:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
3. Add this repository (replace VERSION with the version from jitpack):
```xml
<dependency>
    <groupId>com.github.schemil053</groupId>
    <artifactId>ScheCPUEmulator</artifactId>
    <version>VERSION</version>
    <scope>compile</scope>
</dependency>
```

#### Push to you own repository
1. Add your own repository in the pom.xml file:
```xml
    <profiles>
        <profile>
            <id>example-repo</id>
            <distributionManagement>
                <repository>
                    <id>example-repo</id>
                    <url>https://maven.example.com/repo</url>
                </repository>
            </distributionManagement>
        </profile>
    </profiles>
```
2. Build and deploy:
```bash
mvn deploy -P example-repo
```

3. Add as a dependency:
```xml
    <repository>
        <id>jitpack.io</id>
        <url>https://maven.example.com/repo</url>
    </repository>
```

```xml
<dependency>
    <groupId>de.emilschlampp</groupId>
    <artifactId>ScheCPUEmulator</artifactId>
    <version>1.0-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
```


### Install locally
1. Build and install
```bash
mvn install
```

2. Add as a dependency
```xml
<dependency>
    <groupId>de.emilschlampp</groupId>
    <artifactId>ScheCPUEmulator</artifactId>
    <version>1.0-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
```

## Examples
### Schessembler Examples
- [Hello world (char by char)](src/test/resources/hello world.sasm)
- [Hello world (loadstrm)](src/test/resources/simple-loadstrm.sasm)

### Implementation
- [Simple Compiler (Schessembler)](src/test/java/de/emilschlampp/scheCPU/compile/CompilerTest.java)
- [Simple Compiler (Highlang)](src/test/java/de/emilschlampp/scheCPU/high/HighCompilerTest.java)
- [Executor (Bytecode)](src/test/java/de/emilschlampp/scheCPU/emulator/CPUEmulatorTest.java)


## Future Improvements
- [ ] Create a Minecraft-Port of the emulator to run inside Minecraft and add Mechanics to control Redstone via an CPU
- [ ] Create a higher level language than schessembler (started, unfinished yet)
- [ ] Add a debugger
- [ ] Add a way to modify the program at runtime
- [ ] More ways to restrict the use of the CPU to make it save for running in a prod environment
- [ ] More ways to use registers (implement functions/instructions?)
