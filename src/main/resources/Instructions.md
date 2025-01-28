# ScheCPU

## Instruction Table

| Instruction                       | Short description/Name                                   | Description                                                                                                                   |
|-----------------------------------|----------------------------------------------------------|:------------------------------------------------------------------------------------------------------------------------------|
| JMP \<ADDR\>                      | Jump to Address                                          |                                                                                                                               |
| LOAD \<REGISTER\> \<VAL\>         | Load VAL to REGISTER                                     |                                                                                                                               |
| LOADMEM \<REGISTER\> \<ADDR\>     | Load Value at ADDR to REGISTER                           |                                                                                                                               |
| STORE \<REGISTER\> \<ADDR\>       | Store REGISTER to ADDR                                   |                                                                                                                               |
| STOREREG \<ADDR\> \<REGISTER\>    | Store ADDR to REGISTER                                   |                                                                                                                               |
| STOREREGM \<TARGET\> \<REGISTER\> | Store Address at TARGET to REGISTER                      |                                                                                                                               |
| CMPM \<REGISTER\> \<ADDR\>        |                                                          | If value in Register is greater than ADDR, boolStore will set to 1, if it is the same, boolStore=0, if less boolStore=-1      |
| CMPMEM \<ADDR1\> \<ADDR2\>        |                                                          | If value in ADDR1 is greater than ADDR2, boolStore will set to 1, if it is the same, boolStore=0, if less boolStore=-1        |
| CMPR \<REGISTER\> \<REGISTER_S\>  |                                                          | If value in Register is greater than RegisterS, boolStore will set to 1, if it is the same, boolStore=0, if less boolStore=-1 |
| CMPMC \<REGISTER\>                | Same as CMPM, uses for ADDR the last STORE command value |                                                                                                                               |
| STOREMEM \<ADDR\> \<VAL\>         | Store VAL at ADDR                                        |                                                                                                                               |
| CJMP \<ADDR\>                     | if boolstore is positive JMP \<ADDR\>                    |                                                                                                                               |
| ADD \<REGISTER\> \<VAL\>          | add VAL to REGISTER value                                |                                                                                                                               |
| ADDM \<ADDR\> \<VAL\>             | add VAL to memory value                                  |                                                                                                                               |
| ADDR \<REGISTER\> \<REGISTER_S\>  | add REGISTER_S value to REGISTER value                   |                                                                                                                               |
| ADDMM \<Target\> \<Address\>      | adds memory value of address to target                   |                                                                                                                               |
| COPYR \<REGISTER\> \<REGISTER_S\> | copy REGISTER_S value to REGISTER value                  |                                                                                                                               |
| SUB \<REGISTER\> \<VAL\>          | remove VAL to REGISTER value                             |                                                                                                                               |
| SUBM \<ADDR\> \<VAL\>             | remove VAL to memory value                               |                                                                                                                               |
| MUL \<REGISTER\> \<VAL\>          | multiply VAL by REGISTER value                           |                                                                                                                               |
| MULM \<ADDR\> \<VAL\>             | mulitiply VAL by memory value                            |                                                                                                                               |
| DIV \<REGISTER\> \<VAL\>          | divide REGISTER value by VAL                             |                                                                                                                               |
| DIVM \<ADDR\> \<VAL\>             | divide memory value by VAL                               |                                                                                                                               |
| OUTW \<PORT\> \<VAL\>             | outputs val on port                                      |                                                                                                                               |
| OUTWM \<PORT\> \<ADDR\>           | outputs memory value on port                             |                                                                                                                               |
| OUTWDM \<PORT\> \<ADDR\>          | outputs memory value at value of memory address on port  | Lookup address, and outputs it                                                                                                |
| OUTWR \<PORT\> \<REGISTER\>       | outputs register value on port                           |                                                                                                                               |
| INWM \<PORT\> \<ADDR\>            | reads PORT to ADDR                                       |                                                                                                                               |
| INWDM \<PORT\> \<ADDR\>           | reads PORT to memory value (address) at ADDR             |                                                                                                                               |
| INWR \<PORT\> \<REGISTER\>        | reads PORT to REGISTER                                   |                                                                                                                               |
| CNJMP \<ADDR\>                    | if boolstore is negative JMP \<ADDR\>                    |                                                                                                                               |
| CZJMP \<ADDR\>                    | if boolstore is zero JMP \<ADDR\>                        |                                                                                                                               |
| BJMP                              | jumps back (to the position when jmp gets called)        |                                                                                                                               |
| CBJMP                             | jumps back if boolstore is positive                      |                                                                                                                               |
| CNBJMP                            | jumps back if boolstore is negative                      |                                                                                                                               |
| CZBJMP                            | jumps back if boolstore is zero                          |                                                                                                                               |
| MJMP \<Address\>                  | gets the value at \<Address\> and jumps to the value     |                                                                                                                               |
| CMJMP \<Address\>                 | does the same as MJMP but only of boolstore is positive  |                                                                                                                               |
| CNMJMP \<Address\>                | does the same as MJMP but onyl of boolstore is negative  |                                                                                                                               |
|                                   |                                                          |                                                                                                                               |
| OUTWFUNC \<Port\> \<Function\>    | outputs the function address on port                     |                                                                                                                               |
| LOADSTRM \<Address\> \<Value\>    | loads \<Value\> to \<Address\>                           | Converts to STOREMEM. use \n for newline                                                                                      |
| LOADSTRMC \<Address\> \<Value\>   | load \<Value\> to \<Address\>+1 \<Address\ˍ>             | Converts to STOREMEM. use \n for newline                                                                                      |
| FUNC \<NAME\>                     | defines a function (not part of the OPCODE)              |                                                                                                                               |

## Default OUTW Addresses

| ID | Name         | Description                                                                |
|----|--------------|----------------------------------------------------------------------------|
| 1  | RegMemDump   | Dumps Registers and the Memory to console when set to another value than 0 |
| 2  | OPCodeInfo   | Prints the name of the OPCODE when set to another value than 0             |
| 3  | SLEEP        | if set to 1, it enables sleeping                                           |
| 4  | SLEEP_CYCLES | cycles to skip                                                             |
| 5  | LAST_JMP     | last jmp address                                                           |
| 6  | RESET        | resets the cpu                                                             |
| 34 | SOUT         | Prints ascii char to console                                               |
