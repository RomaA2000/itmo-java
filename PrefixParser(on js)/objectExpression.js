"use strict";

let VAR = {
    "x": 0,
    "y": 1,
    "z": 2
};

function ParseError(message, i) {
    this.message = message + " :" + (i + 1).toString();
}

ParseError.prototype = Object.create(Error.prototype);
ParseError.prototype.name = "ParseError";
ParseError.prototype.constructor = ParseError;

const Nothing = {};
const isConst = function (arg) {
    return (arg instanceof Const);
};
const isZero = function (arg) {
    return (isConst(arg) && (arg.evaluate() === 0));
};
const isOne = function (arg) {
    return (isConst(arg) && (arg.evaluate() === 1));
};
const allConst = function (...args) {
    let ans = true;
    for (let i of args) {
        ans = ans && (isConst(i))
    }
    return ans;
};
const Expression = {
    evaluate: function (...args) {
        return this.getOp()(...this.getInput().reduce((ans, i) => {ans.push(i.evaluate(...args));return ans}, []));
    },
    diff: function (name) {
        return this.getDerr()(...this.getInput(), ...this.getInput().reduce((ans, i) => {ans.push(i.diff(name));return ans}, []));
    },
    simplify: function () {
        let ans = [];
        for (let i of this.getInput()) {
            ans.push(i.simplify());
        }
        return this.getSimpl()(...ans);
    },
    prefix: function () {
        let ans = "(" + this.getSym();
        for (let i of this.getInput()) {
            ans += " " + i.prefix();
        }
        ans += ")";
        return ans;
    },
    postfix: function () {
        let ans = "(";
        for (let i of this.getInput()) {
            ans += i.postfix()  + " ";
        }
        if (this.getInput().length === 0) {
            ans += " ";
        }
        ans +=this.getSym() + ")";
        return ans;
    },
    toString: function () {
        let ans = "";
        for (let i of this.getInput()) {
            ans += i.toString() + " ";
        }
        ans += this.getSym();
        return ans;
    }
};

function expGenerator(operation, symbol, der, simpl) {
    const parameters = function (...args) {
        this.getDerr = function () {
            return der;
        };
        this.getOp = function () {
            return operation;
        };
        this.getInput = function () {
            return args;
        };
        this.getSym = function () {
            return symbol;
        };
        this.getSimpl = function () {
            return simpl;
        };
    };
    parameters.prototype = Object.create(Expression);
    return parameters;
}
const Add = expGenerator((x, y) => (x + y), "+", (x, y, dx, dy) => new Add(dx, dy));
const Multiply = expGenerator((x, y) => (x * y), "*", (x, y, dx, dy) => new Add(new Multiply(x, dy), new Multiply(dx, y)));
const Divide = expGenerator((x, y) => (x / y), "/", (x, y, dx, dy) =>
        new Divide(new Subtract(new Multiply(dx, y), new Multiply(x, dy)), sq(y)));
const Subtract = expGenerator((x, y) => (x - y), "-", (x, y, dx, dy) => new Subtract(dx, dy));
const Negate = expGenerator((x) => -x, "negate", (x, dx) => new Negate(dx));
const ArcTan = expGenerator((x) => Math.atan(x), "atan", (x, dx) => new Divide(dx, new Add(ONE, sq(x))));
const ArcTan2 = expGenerator((x, y) => Math.atan2(x, y), "atan2", (x, y, dx, dy) => new Divide(new Subtract(new Multiply(dx, y), new Multiply(dy, x)), new Add(sq(x), sq(y))));
const Sumsq = expGenerator((...arr) => arr.reduce((returned, val) => (val * val) + returned, 0), "sumsq",
    (...input) => {
    let half = input.length / 2;
    let ans = ZERO;
    for (let i = 0; i < half; ++i) {
        ans = new Add(ans, new Multiply(TWO, new Multiply(input[i],input[half + i])));
    }
    return ans;
});
const Length = expGenerator((...arr) => Math.sqrt(arr.reduce((returned, val) => (val * val) + returned, 0)), "length",
    (...input) => {
        let half = input.length / 2;
        if (half === 0) {
            return ZERO;
        }
        let ans = ZERO;
        let args = input.slice(0, half);
        for (let i = 0; i < half; ++i) {
            ans = new Add(ans, new Multiply(input[i], input[half + i]));
        }
        ans = new Divide(ans, new Length(...args));
        return ans;
    });
const sq = function (a) {
    return new Multiply(a, a);
};
const Variable = function (name) {
    this.getVar = function () {
        return name;
    };
    let id = VAR[name];
    this.getId = function () {
        return id;
    }
};
Variable.prototype = Object.create(Nothing);
Variable.prototype.evaluate = function (...args) {
    return args[this.getId()];
};
Variable.prototype.toString = function () {
    return this.getVar();
};
Variable.prototype.diff = function (name2) {
    return (name2 === this.getVar() ? ONE : ZERO);
};
Variable.prototype.simplify = function () {
    return this;
};
Variable.prototype.prefix = function () {
    return this.getVar();
};
Variable.prototype.postfix = function () {
    return this.getVar();
};
const Const = function (input) {
    this.getNum = function () {
        return input;
    }
};
Const.prototype = Object.create(Nothing);
Const.prototype.evaluate = function () {
    return this.getNum();
};
Const.prototype.toString = function () {
    return this.getNum().toString();
};
Const.prototype.diff = function () {
    return ZERO;
};
Const.prototype.simplify = function () {
    return this;
};
Const.prototype.prefix = function () {
    return this.toString();
};
Const.prototype.postfix = function () {
    return this.toString();
};
const ONE = new Const(1);
const ZERO = new Const(0);
const TWO = new Const(2);
const OP_NUM = {
    "+": 2,
    "-": 2,
    "*": 2,
    "/": 2,
    "negate": 1,
    "atan": 1,
    "atan2": 2,
    "sumsq" : -1,
    "length" : -1
};
const OP = {
    "+": Add,
    "-": Subtract,
    "*": Multiply,
    "/": Divide,
    "negate": Negate,
    "atan": ArcTan,
    "atan2": ArcTan2,
    "sumsq": Sumsq,
    "length" : Length
};
const parse = function (input) {
    let stack = [];
    input.split(" ").filter(s => s.length > 0).forEach(function (i) {
        if (i in VAR) {
            stack.push(new Variable(i))
        } else if (i in OP) {
            let args = [];
            for (let j = 0; j < OP_NUM[OP[i]]; j++) {
                args.push(stack.pop());
            }
            args.reverse();
            stack.push(new OP[i](...args));
        } else {
            stack.push(new Const(i));
        }
    });
    return stack.pop();
};
const parsePrefix = function(input) {
    return parseAll(input, "pref");
};
const parsePostfix = function (input) {
    return parseAll(input, "post");
};
const parseAll = function (input, mode) {
    const empty = function (args) {
        return (args.length === 0);
    };
    const oneElement = function (args) {
        return (args.length === 1);
    };
    let balance = 0;
    let i = 0;
    let wasBrace = 0;
    let wasOp = 0;
    const recParse = function () {
        let nowOperands = [];
        let nowOperation = [];
        for (skipWS(); i < input.length; skipWS()) {
            if (input[i] === "(") {
                ++balance;
                wasBrace = 1;
                ++i;
                nowOperands.push(recParse());
                if (!(empty(nowOperation)) && (mode === "post")) {
                    throw new ParseError("Not a postfix", i);
                }
            } else if (input[i] === ")") {
                --balance;
                ++i;
                break;
            } else {
                let op = getOp();
                if (op in VAR) {
                    nowOperands.push(new Variable(op));
                } else if (op in OP) {
                    wasOp = 1;
                    nowOperation.push(op);
                    if (!(empty(nowOperands)) && (mode === "pref")) {
                        throw new ParseError("Not a prefix", i);
                    }
                } else {
                    let num = Number(op);
                    if (isNaN(num)) {
                        throw new ParseError("Not a number", i);
                    }
                    nowOperands.push(new Const(num));
                }
            }
        }
        if (balance < 0) {
            throw new ParseError("Negative bracket balance", i);
        }
        if (empty(nowOperation) && oneElement(nowOperands)) {
            let now = nowOperands[0];
            if ((i === input.length) && !((wasOp === 0) && (wasBrace === 1))) {
                return now;
            } else{
                throw new ParseError("Incorrect argument", i);
            }
        }
        if (!oneElement(nowOperation)) {
            throw new ParseError("Incorrect number of operations", i);
        }
        let now = nowOperation[0];
        if ((OP_NUM[now] !== nowOperands.length) && (OP_NUM[now] !== -1))  {
            throw new ParseError("Incorrect number of operands", i);
        }
        return new OP[now](...nowOperands);
    };
    const getOp = function () {
        let op = "";
        for (; (i < input.length) && !/\s/.test(input[i])
               && !(input[i] === ")") && !(input[i] === "("); i++) {
            op += input[i];
        }
        return op;
    };
    const skipWS = function () {
        while ((i < input.length) && (/\s/.test(input[i]))) {
            ++i;
        }
    };
    let ans = recParse();
    if (balance > 0) {
        throw new ParseError("Incorrect bracket balance", i);
    }
    skipWS();
    if (i < input.length) {
        throw new ParseError("Expected end of file", i);
    }
    return ans;
};