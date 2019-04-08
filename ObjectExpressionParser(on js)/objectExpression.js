"use strict";
let VAR = {
    "x": 0,
    "y": 1,
    "z": 2
};

function ParseError(message) {
    this.message = message;
}

ParseError.prototype = Object.create(Error.prototype);
ParseError.prototype.name = "ParseError";
ParseError.prototype.constructor = ParseError;

let Nothing = {};
let isConst = function (arg) {
    return (arg instanceof Const);
};
let isZero = function (arg) {
    return (isConst(arg) && (arg.evaluate() === 0));
};
let isOne = function (arg) {
    return (isConst(arg) && (arg.evaluate() === 1));
};
let allConst = function (...args) {
    let ans = true;
    for (let i of args) {
        ans = ans && (isConst(i))
    }
    return ans;
};
let Expression = {
    evaluate: function (...args) {
        let ans = [];
        for (let i of this.getInput()) {
            ans.push(i.evaluate(...args));
        }
        return this.getOp()(...ans);
    },
    toString: function () {
        let ans = "";
        for (let i of this.getInput()) {
            ans += i.toString() + " ";
        }
        ans += this.getSym();
        return ans;
    },
    diff: function (name) {
        let der = [];
        for (let i of this.getInput()) {
            der.push(i.diff(name));
        }
        return this.getDerr()(...this.getInput(), ...der);
    },
    simplify: function () {
        let ans = [];
        for (let i of this.getInput()) {
            ans.push(i.simplify());
        }
        return this.getSimpl()(...ans);
    }
};

function expGenerator(operation, symbol, der, simpl) {
    let parameters = function (...args) {
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

let Add = expGenerator((x, y) => (x + y), "+", (x, y, dx, dy) => new Add(dx, dy),
    (a, b) => {
        if (allConst(a, b)) {
            return new Const((new Add(a, b).evaluate()))
        }
        if (isZero(a)) {
            return b;
        }
        if (isZero(b)) {
            return a;
        }
        return new Add(a, b);
    });
let Multiply = expGenerator((x, y) => (x * y), "*", (x, y, dx, dy) => new Add(new Multiply(x, dy), new Multiply(dx, y)),
    (a, b) => {
        if (allConst(a, b)) {
            return new Const((new Multiply(a, b).evaluate()))
        }
        if (isZero(a) || isZero(b)) {
            return ZERO;
        }
        if (isOne(a)) {
            return b;
        }
        if (isOne(b)) {
            return a;
        }
        return new Multiply(a, b);
    });
let Divide = expGenerator((x, y) => (x / y), "/", (x, y, dx, dy) =>
        new Divide(new Subtract(new Multiply(dx, y), new Multiply(x, dy)), sq(y)),
    (x, y) => {
        let a = x.simplify();
        let b = y.simplify();
        if (allConst(a, b)) {
            return new Const((new Divide(a, b).evaluate()))
        }
        if (isOne(b)) {
            return a;
        }
        if (isZero(a)) {
            return ZERO;
        }
        return new Divide(a, b);
    });
let Subtract = expGenerator((x, y) => (x - y), "-", (x, y, dx, dy) => new Subtract(dx, dy),
    (x, y) => {
        let a = x.simplify();
        let b = y.simplify();
        if (allConst(a, b)) {
            return new Const((new Subtract(a, b).evaluate()))
        }
        if (isZero(a)) {
            return new Negate(b);
        }
        if (isZero(b)) {
            return a;
        }
        return new Subtract(a, b);
    });
let Negate = expGenerator((x) => -x, "negate", (x, dx) => new Negate(dx),
    (a) => {
        if (allConst(a)) {
            return new Const((new Negate(a).evaluate()))
        }
        return new Negate(a);
    });
let ArcTan = expGenerator((x) => Math.atan(x), "atan", (x, dx) => new Divide(dx, new Add(ONE, sq(x))),
    (a) => {
        if (allConst(a)) {
            return new Const((new ArcTan(a).evaluate()))
        }
        return new ArcTan(a);
    });
let ArcTan2 = expGenerator((x, y) => Math.atan2(x, y), "atan2", (x, y, dx, dy) => new Divide(new Subtract(new Multiply(dx, y), new Multiply(dy, x)), new Add(sq(x), sq(y))),
    (a, b) => {
        if (allConst(a, b)) {
            return new Const(new ArcTan2(a, b).evaluate())
        }
        if (isZero(a)) {
            return ZERO;
        }
        if (isOne(b)) {
            return new ArcTan(a);
        }
        return new ArcTan2(a, b);
    });
let sq = function (a) {
    return new Multiply(a, a);
};
let Variable = function (name) {
    this.getVar = function () {
        return name;
    };
};
Variable.prototype = Object.create(Nothing);
Variable.prototype.evaluate = function (...args) {
    return args[VAR[this.getVar()]];
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
let Const = function (input) {
    this.getNum = function () {
        return input;
    }
};
Const.prototype = Object.create(Nothing);
Const.prototype.evaluate = function () {
    return this.getNum();
};
Const.prototype.toString = function () {
    let ans = "";
    ans += this.getNum().toString();
    return ans;
};
Const.prototype.diff = function () {
    return ZERO;
};
Const.prototype.simplify = function () {
    return this;
};
const ONE = new Const(1);
const ZERO = new Const(0);
let OP_NUM = {
    "+": 2,
    "-": 2,
    "*": 2,
    "/": 2,
    "negate": 1,
    "atan": 1,
    "atan2": 2
};
let OP = {
    "+": Add,
    "-": Subtract,
    "*": Multiply,
    "/": Divide,
    "negate": Negate,
    "atan": ArcTan,
    "atan2": ArcTan2
};
let parse = function (input) {
    return input.split(" ").filter((s) => s.length > 0).reduce((stack, i) => {
        if (i in VAR) {
            stack.push(new Variable(i))
        } else if (i in OP) {
            let args = [];
            for (let j = 0; j < OP_NUM[i]; j++) {
                args.push(stack.pop());
            }
            args.reverse();
            stack.push(new OP[i](...args));
        } else {
            stack.push(new Const(Number(i)));
        }
        return stack;
    }, []).pop();
};
