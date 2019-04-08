"use strict";
const expression = f => (...args) => (...evol) => f(...(args.map(i => i(...evol))));

const sum = function (...args) {
    let out = 0;
    for (let i of args) {
        out += i;
    }
    return out;
};

const add = expression((a, b) => a + b);
const subtract = expression((a, b) => a - b);
const multiply = expression((a, b) => a * b);
const divide = expression((a, b) => a / b);
const cnst = a => () => a;
const variable = name => (...args) => args[VARIABLES[name]];
const negate = expression(a => -a);
const abs = expression(a => Math.abs(a));
const iff = expression((...args) => (args[0] >= 0) ? args[1] : args[2]);
const one = cnst(1);
const two = cnst(2);

const parse = function (input) {
    return input.split(" ").filter((s) => s.length > 0).reduce((stack, i) => {
        if (i in CONSTS) {
            stack.push(CONSTS[i]);
        } else if (i in VARIABLES) {
            stack.push(variable(i))
        } else if (i in OP) {
            let args = [];
            for (let j = 0; j < OP_NUM[i]; j++) {
                args.push(stack.pop());
            }
            args.reverse();
            stack.push(OP[i](...args));
        } else {
            stack.push(cnst(Number(i)));
        }
        return stack;
    }, []).pop();
};

const VARIABLES = {
    "x": 0,
    "y": 1,
    "z": 2,
};

const CONSTS = {
    "one": one,
    "two": two
};

const OP = {
    "+": add,
    "-": subtract,
    "/": divide,
    "*": multiply,
    "negate": negate,
    "abs": abs,
    "iff": iff
};

const OP_NUM = {
    "+": 2,
    "-": 2,
    "*": 2,
    "/": 2,
    "negate": 1,
    "cnst": 1,
    "abs": 1,
    "iff": 3
};